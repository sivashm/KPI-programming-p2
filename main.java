package lab;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас містить виконавчий метод, який знаходить таке слово в першому реченні
 * заданого тексту, якого немає в жодному з наступних речень.
 *
 * <p>Згідно з умовою для роботи з рядками використовується лише тип
 * {@link StringBuffer}. Перетворення {@code StringBuffer -> String} і навпаки
 * виконується тільки під час введення та виведення даних.</p>
 */
public final class UniqueWordFinder {

    /** Символи, що завершують речення. */
    private static final String SENTENCE_DELIMITERS = ".!?";

    private UniqueWordFinder() {
    }

    public static void main(final String[] args) {
        try {
            final StringBuffer text = new StringBuffer(
                    "Quick brown fox jumps over the lazy dog. "
                    + "The brown bear sleeps in a den. "
                    + "A fox is clever but the dog is loyal.");

            final StringBuffer result = findUniqueWord(text);

            if (result.length() == 0) {
                System.out.println("Слова, якого немає в наступних реченнях, "
                        + "не знайдено.");
            } else {
                System.out.println("Шукане слово: " + result.toString());
            }
        } catch (final NullPointerException e) {
            System.out.println("Помилка: текст не задано (null).");
        } catch (final Exception e) {
            System.out.println("Виникла непередбачена помилка: "
                    + e.getMessage());
        }
    }

    private static StringBuffer findUniqueWord(final StringBuffer text) {
        if (text == null) {
            throw new NullPointerException("Вхідний текст не може бути null.");
        }

        final List<StringBuffer> sentences = splitIntoSentences(text);
        final StringBuffer answer = new StringBuffer();

        if (sentences.size() < 2) {
            return answer;
        }

        final List<StringBuffer> firstWords =
                splitIntoWords(sentences.get(0));

        final List<StringBuffer> otherWords = new ArrayList<StringBuffer>();
        for (int i = 1; i < sentences.size(); i++) {
            otherWords.addAll(splitIntoWords(sentences.get(i)));
        }

        for (int i = 0; i < firstWords.size(); i++) {
            final StringBuffer word = firstWords.get(i);
            if (!containsWord(otherWords, word)) {
                answer.append(word);
                break;
            }
        }

        return answer;
    }

    private static List<StringBuffer> splitIntoSentences(
            final StringBuffer text) {
        final List<StringBuffer> sentences = new ArrayList<StringBuffer>();
        StringBuffer current = new StringBuffer();

        for (int i = 0; i < text.length(); i++) {
            final char symbol = text.charAt(i);
            if (SENTENCE_DELIMITERS.indexOf(symbol) >= 0) {
                if (hasLetters(current)) {
                    sentences.add(current);
                }
                current = new StringBuffer();
            } else {
                current.append(symbol);
            }
        }

        if (hasLetters(current)) {
            sentences.add(current);
        }

        return sentences;
    }

    private static List<StringBuffer> splitIntoWords(
            final StringBuffer sentence) {
        final List<StringBuffer> words = new ArrayList<StringBuffer>();
        StringBuffer current = new StringBuffer();

        for (int i = 0; i < sentence.length(); i++) {
            final char symbol = sentence.charAt(i);
            if (Character.isLetterOrDigit(symbol)) {
                current.append(symbol);
            } else if (current.length() > 0) {
                words.add(current);
                current = new StringBuffer();
            }
        }

        if (current.length() > 0) {
            words.add(current);
        }

        return words;
    }

    private static boolean containsWord(final List<StringBuffer> words,
            final StringBuffer word) {
        for (int i = 0; i < words.size(); i++) {
            if (equalsIgnoreCase(words.get(i), word)) {
                return true;
            }
        }
        return false;
    }

    private static boolean equalsIgnoreCase(final StringBuffer first,
            final StringBuffer second) {
        if (first.length() != second.length()) {
            return false;
        }
        for (int i = 0; i < first.length(); i++) {
            final char left = Character.toLowerCase(first.charAt(i));
            final char right = Character.toLowerCase(second.charAt(i));
            if (left != right) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasLetters(final StringBuffer buffer) {
        for (int i = 0; i < buffer.length(); i++) {
            if (Character.isLetterOrDigit(buffer.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}