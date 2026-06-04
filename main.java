package lab;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас містить виконавчий метод, який знаходить таке слово в першому реченні
 * заданого тексту, якого немає в жодному з наступних речень.
 *
 * <p>Текст розбивається на ієрархію об'єктів: Text → Sentence → Word/Punctuation
 * → Letter. Послідовності пробілів і табуляцій замінюються одним пробілом.</p>
 */
final class UniqueWordFinder {

    /** Символи, що завершують речення. */
    private static final String SENTENCE_TERMINATORS = ".!?";

    private UniqueWordFinder() {
    }

    public static void main(final String[] args) {
        final StringBuffer input = new StringBuffer(
                "Quick brown  fox\tjumps over the lazy dog. "
                + "The brown bear sleeps\t\tin a den. "
                + "A fox is clever but the dog is loyal.");

        System.out.println("Вхідний текст:");
        System.out.println(input);

        final Text text = parseText(input);

        System.out.println("\nНормалізований текст:");
        System.out.println(text);

        final Word result = findUniqueWord(text);

        System.out.println();
        if (result == null) {
            System.out.println("Слова, якого немає в наступних реченнях, "
                    + "не знайдено.");
        } else {
            System.out.println("Шукане слово: " + result);
        }
    }

    /**
     * Замінює кожну послідовність пробілів і табуляцій одним пробілом.
     */
    private static StringBuffer normalizeWhitespace(final StringBuffer input) {
        final StringBuffer result = new StringBuffer();
        boolean prevSpace = false;
        for (int i = 0; i < input.length(); i++) {
            final char ch = input.charAt(i);
            if (ch == ' ' || ch == '\t') {
                if (!prevSpace) {
                    result.append(' ');
                }
                prevSpace = true;
            } else {
                result.append(ch);
                prevSpace = false;
            }
        }
        return result;
    }

    /**
     * Розбирає StringBuffer у об'єкт Text.
     * Речення завершуються символами з SENTENCE_TERMINATORS.
     * Пробіли та розділові знаки стають Punctuation; послідовності
     * літер/цифр — Word із масивом Letter.
     */
    private static Text parseText(final StringBuffer input) {
        final StringBuffer normalized = normalizeWhitespace(input);

        final List<Sentence> sentences = new ArrayList<Sentence>();
        final List<SentenceElement> currentElements =
                new ArrayList<SentenceElement>();
        final List<Letter> currentLetters = new ArrayList<Letter>();

        for (int i = 0; i < normalized.length(); i++) {
            final char ch = normalized.charAt(i);

            if (Character.isLetterOrDigit(ch)) {
                currentLetters.add(new Letter(ch));
            } else {
                flushWord(currentLetters, currentElements);

                if (SENTENCE_TERMINATORS.indexOf(ch) >= 0) {
                    currentElements.add(new Punctuation(ch));
                    final SentenceElement[] elems = currentElements.toArray(
                            new SentenceElement[currentElements.size()]);
                    sentences.add(new Sentence(elems));
                    currentElements.clear();
                } else {
                    currentElements.add(new Punctuation(ch));
                }
            }
        }

        flushWord(currentLetters, currentElements);
        if (!currentElements.isEmpty()) {
            final SentenceElement[] elems = currentElements.toArray(
                    new SentenceElement[currentElements.size()]);
            sentences.add(new Sentence(elems));
        }

        return new Text(sentences.toArray(new Sentence[sentences.size()]));
    }

    /** Якщо накопичено літери — створює Word і додає до списку елементів. */
    private static void flushWord(final List<Letter> letters,
            final List<SentenceElement> elements) {
        if (!letters.isEmpty()) {
            final Letter[] arr = letters.toArray(new Letter[letters.size()]);
            elements.add(new Word(arr));
            letters.clear();
        }
    }

    /**
     * Шукає перше слово з першого речення, якого немає в жодному
     * з наступних речень.
     */
    private static Word findUniqueWord(final Text text) {
        final Sentence[] sentences = text.getSentences();
        if (sentences.length < 2) {
            return null;
        }

        final Word[] firstWords = sentences[0].getWords();

        final List<Word> otherWords = new ArrayList<Word>();
        for (int i = 1; i < sentences.length; i++) {
            final Word[] words = sentences[i].getWords();
            for (int j = 0; j < words.length; j++) {
                otherWords.add(words[j]);
            }
        }

        for (int i = 0; i < firstWords.length; i++) {
            if (!containsWord(otherWords, firstWords[i])) {
                return firstWords[i];
            }
        }

        return null;
    }

    private static boolean containsWord(final List<Word> words,
            final Word target) {
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * Маркерний інтерфейс для елементів речення: слова або розділового знака.
 */
interface SentenceElement {
}

/**
 * Літера — окремий символ (буква або цифра).
 */
class Letter {
    private final char value;

    Letter(final char value) {
        this.value = value;
    }

    char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

/**
 * Розділовий знак — один символ (пунктуація або пробіл).
 */
class Punctuation implements SentenceElement {
    private final char value;

    Punctuation(final char value) {
        this.value = value;
    }

    char getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

/**
 * Слово — масив літер.
 */
class Word implements SentenceElement {
    private final Letter[] letters;

    Word(final Letter[] letters) {
        this.letters = letters.clone();
    }

    int length() {
        return letters.length;
    }

    boolean equalsIgnoreCase(final Word other) {
        if (letters.length != other.letters.length) {
            return false;
        }
        for (int i = 0; i < letters.length; i++) {
            final char a = Character.toLowerCase(letters[i].getValue());
            final char b = Character.toLowerCase(other.letters[i].getValue());
            if (a != b) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < letters.length; i++) {
            sb.append(letters[i].getValue());
        }
        return sb.toString();
    }
}

/**
 * Речення — масив слів та розділових знаків у порядку появи.
 */
class Sentence {
    private final SentenceElement[] elements;

    Sentence(final SentenceElement[] elements) {
        this.elements = elements.clone();
    }

    Word[] getWords() {
        final List<Word> words = new ArrayList<Word>();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] instanceof Word) {
                words.add((Word) elements[i]);
            }
        }
        return words.toArray(new Word[words.size()]);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < elements.length; i++) {
            sb.append(elements[i]);
        }
        return sb.toString();
    }
}

/**
 * Текст — масив речень.
 */
class Text {
    private final Sentence[] sentences;

    Text(final Sentence[] sentences) {
        this.sentences = sentences.clone();
    }

    Sentence[] getSentences() {
        return sentences.clone();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sentences.length; i++) {
            sb.append(sentences[i]);
        }
        return sb.toString();
    }
}