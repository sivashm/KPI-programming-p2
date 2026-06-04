package lab;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sivash
 * @version 1.0
 */
final class UniqueWordFinder {

    /**
     * Рядок із символами, що завершують речення ({@code .}, {@code !},
     * {@code ?}).
     */
    private static final String SENTENCE_TERMINATORS = ".!?";

    /**
     * Закритий конструктор: клас є утилітним і не призначений для
     * інстанціювання.
     */
    private UniqueWordFinder() {
    }

    /**
     * Точка входу програми.
     *
     * <p>Визначає вхідний текст, будує об'єктну модель {@link Text},
     * виводить нормалізований текст і результат пошуку унікального слова.</p>
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(final String[] args) {
        final StringBuffer input = new StringBuffer(
                "Quick brown  fox\tjumps over the lazy dog. "
                + "The brown bear sleeps\t\tin a den. "
                + "A fox is clever but the dog is loyal.");

        System.out.println("Вхідний текст:");
        System.out.println(input);

        final Text text = Text.parse(input);

        System.out.println("\nНормалізований текст:");
        System.out.println(text);

        final Word result = findUniqueWord(text);

        System.out.println();
        if (result == null) {
            System.out.println(
                    "Слова, якого немає в наступних реченнях, не знайдено.");
        } else {
            System.out.println("Шукане слово: " + result);
        }
    }

    /**
     * Знаходить перше слово з першого речення {@code text}, якого немає
     * в жодному з наступних речень.
     *
     * <p>Порівняння слів виконується без урахування регістру за допомогою
     * {@link Word#equalsIgnoreCase(Word)}.</p>
     *
     * @param text текст, що містить щонайменше одне речення
     * @return перше унікальне слово або {@code null}, якщо текст містить
     *         менше двох речень або всі слова першого речення присутні
     *         у наступних
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

    /**
     * Перевіряє, чи містить список {@code words} слово {@code target}
     * (без урахування регістру).
     *
     * @param words  список слів для пошуку
     * @param target шукане слово
     * @return {@code true}, якщо {@code target} знайдено у {@code words};
     *         {@code false} інакше
     */
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

// ---------------------------------------------------------------------------
// Модельні класи
// ---------------------------------------------------------------------------

/**
 * Маркерний інтерфейс для елементів речення.
 *
 * <p>Реалізується класами {@link Word} та {@link Punctuation}, що дозволяє
 * зберігати їх разом в масиві {@link Sentence} зі збереженням порядку.</p>
 */
interface SentenceElement {
}

/**
 * Літера &mdash; найменша неподільна одиниця тексту.
 *
 * <p>Зберігає один символ, що є літерою або цифрою
 * ({@link Character#isLetterOrDigit(char)} повертає {@code true}).</p>
 */
class Letter {

    /** Символьне значення літери. */
    private final char value;

    /**
     * Створює літеру із заданим символом.
     *
     * @param value символ літери (буква або цифра)
     */
    Letter(final char value) {
        this.value = value;
    }

    /**
     * Повертає символьне значення цієї літери.
     *
     * @return символ літери
     */
    char getValue() {
        return value;
    }

    /**
     * Повертає нову {@link Letter} з символом, приведеним до нижнього
     * регістру за допомогою {@link Character#toLowerCase(char)}.
     *
     * @return літера в нижньому регістрі
     */
    Letter toLowerCase() {
        return new Letter(Character.toLowerCase(value));
    }

    /**
     * Повертає рядкове представлення літери (один символ).
     *
     * @return рядок довжиною 1, що містить символ цієї літери
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

/**
 * Розділовий знак &mdash; елемент речення, що не є словом.
 *
 * <p>Реалізує {@link SentenceElement}. Охоплює пунктуаційні символи
 * (кома, крапка з комою тощо), пробіл-роздільник між словами, а також
 * символи-термінатори речення ({@code .}, {@code !}, {@code ?}).</p>
 */
class Punctuation implements SentenceElement {

    /** Символьне значення розділового знака. */
    private final char value;

    /**
     * Створює розділовий знак із заданим символом.
     *
     * @param value символ розділового знака
     */
    Punctuation(final char value) {
        this.value = value;
    }

    /**
     * Повертає символьне значення цього розділового знака.
     *
     * @return символ розділового знака
     */
    char getValue() {
        return value;
    }

    /**
     * Повертає рядкове представлення розділового знака (один символ).
     *
     * @return рядок довжиною 1, що містить символ цього розділового знака
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

/**
 * Слово &mdash; впорядкований масив {@linkplain Letter літер}.
 *
 * <p>Реалізує {@link SentenceElement}, що дозволяє зберігати слова разом із
 * {@link Punctuation} у масиві {@link Sentence}.</p>
 */
class Word implements SentenceElement {

    /** Масив літер, що утворюють слово. */
    private final Letter[] letters;

    /**
     * Створює слово з масиву літер.
     *
     * <p>Масив копіюється захисним чином, тому подальші зміни в
     * {@code letters} не впливають на об'єкт.</p>
     *
     * @param letters масив літер слова; не повинен бути {@code null}
     *                або порожнім
     */
    Word(final Letter[] letters) {
        this.letters = letters.clone();
    }

    /**
     * Повертає кількість літер у слові.
     *
     * @return кількість літер (&ge; 1)
     */
    int length() {
        return letters.length;
    }

    /**
     * Порівнює це слово з {@code other} без урахування регістру.
     *
     * <p>Два слова вважаються рівними, якщо вони мають однакову довжину
     * і кожна пара відповідних літер збігається після приведення до
     * нижнього регістру за допомогою
     * {@link Character#toLowerCase(char)}.</p>
     *
     * @param other слово для порівняння; не повинно бути {@code null}
     * @return {@code true}, якщо слова збігаються без урахування регістру;
     *         {@code false} інакше
     */
    boolean equalsIgnoreCase(final Word other) {
        if (letters.length != other.letters.length) {
            return false;
        }
        for (int i = 0; i < letters.length; i++) {
            final char a = letters[i].toLowerCase().getValue();
            final char b = other.letters[i].toLowerCase().getValue();
            if (a != b) {
                return false;
            }
        }
        return true;
    }

    /**
     * Повертає рядкове представлення слова, утворене конкатенацією
     * символів усіх його {@linkplain Letter літер}.
     *
     * @return рядок, що відповідає цьому слову
     */
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
 * Речення &mdash; впорядкована послідовність {@linkplain SentenceElement
 * елементів}: {@linkplain Word слів} та {@linkplain Punctuation розділових
 * знаків}.
 *
 * <p>Порядок елементів відповідає порядку їх появи в оригінальному тексті,
 * що забезпечує точне відтворення речення через {@link #toString()}.</p>
 */
class Sentence {

    /**
     * Масив елементів речення ({@link Word} та {@link Punctuation})
     * у порядку появи.
     */
    private final SentenceElement[] elements;

    /**
     * Створює речення з масиву елементів.
     *
     * <p>Масив копіюється захисним чином.</p>
     *
     * @param elements масив елементів речення; не повинен бути {@code null}
     */
    Sentence(final SentenceElement[] elements) {
        this.elements = elements.clone();
    }

    /**
     * Повертає масив лише тих елементів речення, що є {@link Word}.
     *
     * <p>Масив формується в порядку появи слів у реченні.
     * {@link Punctuation}-елементи пропускаються.</p>
     *
     * @return масив слів речення; може бути порожнім, але не {@code null}
     */
    Word[] getWords() {
        final List<Word> words = new ArrayList<Word>();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] instanceof Word) {
                words.add((Word) elements[i]);
            }
        }
        return words.toArray(new Word[words.size()]);
    }

    /**
     * Повертає рядкове представлення речення, утворене конкатенацією
     * рядкових представлень усіх його елементів.
     *
     * @return рядок, що відповідає цьому реченню
     */
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
 * Текст &mdash; впорядкований масив {@linkplain Sentence речень}.
 *
 * <p>Є кореневим елементом об'єктної моделі тексту. Метод {@link #toString()}
 * відновлює повний нормалізований текст конкатенацією речень.</p>
 */
class Text {

    /** Масив речень тексту в порядку появи. */
    private final Sentence[] sentences;

    /**
     * Створює текст із масиву речень.
     *
     * <p>Масив копіюється захисним чином.</p>
     *
     * @param sentences масив речень; не повинен бути {@code null}
     */
    Text(final Sentence[] sentences) {
        this.sentences = sentences.clone();
    }

    /**
     * Розбирає {@link StringBuffer} на об'єктну модель {@link Text}.
     *
     * <p>Алгоритм:</p>
     * <ol>
     *   <li>Нормалізує пробіли: кожна послідовність пробілів і табуляцій
     *       замінюється одним пробілом.</li>
     *   <li>Обходить символи зліва направо:
     *     <ul>
     *       <li>Літера або цифра &mdash; накопичується у поточному слові.</li>
     *       <li>Символ-термінатор ({@code .}, {@code !}, {@code ?})
     *           &mdash; завершує поточне слово та речення; термінатор
     *           зберігається як {@link Punctuation}.</li>
     *       <li>Будь-який інший символ &mdash; завершує поточне слово
     *           (якщо є) і додається як {@link Punctuation}.</li>
     *     </ul>
     *   </li>
     *   <li>Залишок після останнього термінатора утворює окреме
     *       {@link Sentence}.</li>
     * </ol>
     *
     * @param input вхідний {@link StringBuffer}; не змінюється
     * @return об'єктне представлення тексту
     */
    static Text parse(final StringBuffer input) {
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

    /**
     * Замінює кожну послідовність пробілів ({@code ' '}) і табуляцій
     * ({@code '\t'}) одним пробілом.
     *
     * @param input вхідний {@link StringBuffer}; не змінюється
     * @return новий {@link StringBuffer} з нормалізованими пробілами
     */
    private static StringBuffer normalizeWhitespace(
            final StringBuffer input) {
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
     * Якщо список {@code letters} не порожній, створює {@link Word} і
     * додає його до {@code elements}, після чого очищає {@code letters}.
     *
     * @param letters  акумулятор літер поточного слова; після виклику
     *                 буде очищений
     * @param elements список елементів поточного речення
     */
    private static void flushWord(final List<Letter> letters,
            final List<SentenceElement> elements) {
        if (!letters.isEmpty()) {
            final Letter[] arr =
                    letters.toArray(new Letter[letters.size()]);
            elements.add(new Word(arr));
            letters.clear();
        }
    }

    /** Символи, що завершують речення. */
    private static final String SENTENCE_TERMINATORS = ".!?";

    /**
     * Повертає захисну копію масиву речень тексту.
     *
     * @return масив {@link Sentence}; може бути порожнім, але не {@code null}
     */
    Sentence[] getSentences() {
        return sentences.clone();
    }

    /**
     * Повертає рядкове представлення тексту, утворене конкатенацією
     * рядкових представлень усіх його {@linkplain Sentence речень}.
     *
     * @return рядок, що відповідає цьому тексту
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < sentences.length; i++) {
            sb.append(sentences[i]);
        }
        return sb.toString();
    }
}