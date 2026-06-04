package lab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Точка входу програми, що демонструє роботу {@link CarLinkedList}.
 *
 * <p>Формує автомобілі з ієрархії лабораторної роботи №6 ({@link Car}),
 * розміщує їх у типізованій колекції-двозв'язному списку та виконує
 * основні операції {@link List}.</p>
 *
 * @author Sivash
 * @version 1.0
 */
final class CarListDemo {

    private CarListDemo() {
    }

    /**
     * Виконавчий метод програми.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(final String[] args) {
        try {
            final Car camry   = new Sedan(
                "Toyota",     "Camry",    2021, 1_150_000.0, 8.5,  210, 480
            );
            final Car accord  = new Sedan(
                "Honda",      "Accord",   2022, 1_240_000.0, 7.9,  225, 520
            );
            final Car rav4    = new SUV(
                "Toyota",     "RAV4",     2021, 1_560_000.0, 10.2, 195, 220
            );
            final Car explorer= new SUV(
                "Ford",       "Explorer", 2020,1_980_000.0,  13.5, 205, 210
            );
            final Car golf    = new Hatchback(
                "Volkswagen", "Golf",     2022, 910_000.0,   6.8,  200, 5
            );
            final Car fabia   = new Hatchback(
                "Skoda",      "Fabia",    2023, 780_000.0,   5.9,  180, 3
            );
            final Car odyssey = new Minivan(
                "Honda",      "Odyssey",  2020, 1_730_000.0, 12.1, 185, 8
            );

            // --- Конструктор 1: порожній список ---
            final CarLinkedList empty = new CarLinkedList();
            System.out.println("Порожній список. isEmpty: " + empty.isEmpty());

            // --- Конструктор 2: список з одного автомобіля ---
            final CarLinkedList single = new CarLinkedList(camry);
            System.out.println("Список з одного елемента. Розмір: "
                    + single.size());

            // --- Конструктор 3: список зі стандартної колекції ---
            final List<Car> source = java.util.Arrays.asList(
                    camry, accord, rav4, explorer, golf, fabia, odyssey);
            final CarLinkedList fleet = new CarLinkedList(source);
            System.out.println("Список зі стандартної колекції. Розмір: "
                    + fleet.size());

            // --- Перебір (iterator) ---
            System.out.println("\nУсі автомобілі:");
            int n = 1;
            for (final Car c : fleet) {
                System.out.printf("  %d. %s%n", n++, c);
            }

            // --- get / set ---
            System.out.println("\nget(2): " + fleet.get(2));
            final Car old = fleet.set(2, odyssey);
            System.out.println("set(2, odyssey), старий: " + old);
            System.out.println("get(2) після set: " + fleet.get(2));

            // --- add(index, element) ---
            fleet.add(0, fabia);
            System.out.println("\nПісля add(0, fabia). Розмір: "
                    + fleet.size());
            System.out.println("get(0): " + fleet.get(0));

            // --- indexOf / lastIndexOf ---
            System.out.println("\nindexOf(odyssey):     "
                    + fleet.indexOf(odyssey));
            System.out.println("lastIndexOf(odyssey): "
                    + fleet.lastIndexOf(odyssey));

            // --- contains / remove(Object) ---
            System.out.println("\ncontains(rav4): " + fleet.contains(rav4));
            fleet.remove(rav4);
            System.out.println("Після remove(rav4). Розмір: " + fleet.size());

            // --- remove(int) ---
            final Car removed = fleet.remove(0);
            System.out.println("remove(0): " + removed);

            // --- subList ---
            final List<Car> sub = fleet.subList(0, 2);
            System.out.println("\nsubList(0, 2):");
            for (final Car c : sub) {
                System.out.println("  " + c);
            }

            // --- ListIterator у зворотному напрямку ---
            System.out.println("\nЗворотний перебір (ListIterator):");
            final ListIterator<Car> it = fleet.listIterator(fleet.size());
            while (it.hasPrevious()) {
                System.out.println("  " + it.previous());
            }

            // --- toArray ---
            final Object[] arr = fleet.toArray();
            System.out.println("\ntoArray().length: " + arr.length);

            // --- clear ---
            fleet.clear();
            System.out.println("Після clear(). isEmpty: " + fleet.isEmpty());

        } catch (final IllegalArgumentException e) {
            System.err.println("Помилка вхідних даних: " + e.getMessage());
        } catch (final IndexOutOfBoundsException e) {
            System.err.println("Вихід за межі: " + e.getMessage());
        } catch (final Exception e) {
            System.err.println("Непередбачена помилка: " + e.getMessage());
        }
    }
}

// ---------------------------------------------------------------------------
// Ієрархія автомобілів (лабораторна робота №6)
// ---------------------------------------------------------------------------

/**
 * Абстрактний базовий клас, що описує легковий автомобіль.
 *
 * <p>Містить загальні характеристики, спільні для всіх типів легкових
 * автомобілів: марку, модель, рік випуску, ціну, витрати палива та
 * максимальну швидкість.</p>
 *
 * <p>Підкласи зобов'язані реалізувати метод {@link #getType()}, що повертає
 * рядкову назву типу (наприклад, {@code "Седан"}).</p>
 */
abstract class Car {

    /** Найбільш ранній допустимий рік випуску автомобіля. */
    private static final int MIN_YEAR = 1886;

    /** Найбільш пізній допустимий рік випуску автомобіля. */
    private static final int MAX_YEAR = 2027;

    /** Марка автомобіля (наприклад, {@code "Toyota"}). */
    private final String make;

    /** Модель автомобіля (наприклад, {@code "Camry"}). */
    private final String model;

    /** Рік випуску автомобіля. */
    private final int year;

    /** Ціна автомобіля в гривнях. */
    private final double price;

    /** Витрати палива в літрах на 100 км у змішаному циклі. */
    private final double fuelConsumption;

    /** Максимальна швидкість автомобіля в км/год. */
    private final double maxSpeed;

    /**
     * Ініціалізує спільні поля автомобіля.
     *
     * @param make            марка; не може бути {@code null} або порожньою
     * @param model           модель; не може бути {@code null} або порожньою
     * @param year            рік випуску; має бути в межах
     *                        [{@value #MIN_YEAR},&nbsp;{@value #MAX_YEAR}]
     * @param price           ціна в гривнях; має бути &gt;&nbsp;0
     * @param fuelConsumption витрати палива (л/100 км); має бути &gt;&nbsp;0
     * @param maxSpeed        максимальна швидкість (км/год); має бути
     *                        &gt;&nbsp;0
     * @throws IllegalArgumentException якщо будь-який параметр порушує
     *                                  вказані обмеження
     */
    Car(final String make, final String model, final int year,
            final double price, final double fuelConsumption,
            final double maxSpeed) {
        if (make == null || make.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Марка автомобіля не може бути порожньою.");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Модель автомобіля не може бути порожньою.");
        }
        if (year < MIN_YEAR || year > MAX_YEAR) {
            throw new IllegalArgumentException(
                    "Рік випуску має бути в межах ["
                    + MIN_YEAR + ", " + MAX_YEAR + "], отримано: " + year);
        }
        if (price <= 0) {
            throw new IllegalArgumentException(
                    "Ціна має бути більшою за 0, отримано: " + price);
        }
        if (fuelConsumption <= 0) {
            throw new IllegalArgumentException(
                    "Витрати палива мають бути більшими за 0, отримано: "
                    + fuelConsumption);
        }
        if (maxSpeed <= 0) {
            throw new IllegalArgumentException(
                    "Максимальна швидкість має бути більшою за 0, отримано: "
                    + maxSpeed);
        }
        this.make = make.trim();
        this.model = model.trim();
        this.year = year;
        this.price = price;
        this.fuelConsumption = fuelConsumption;
        this.maxSpeed = maxSpeed;
    }

    /**
     * Повертає рядкову назву типу автомобіля.
     *
     * @return назва типу (наприклад, {@code "Седан"})
     */
    abstract String getType();

    /**
     * Повертає марку автомобіля.
     *
     * @return марка
     */
    String getMake() { return make; }

    /**
     * Повертає модель автомобіля.
     *
     * @return модель
     */
    String getModel() { return model; }

    /**
     * Повертає рік випуску автомобіля.
     *
     * @return рік випуску
     */
    int getYear() { return year; }

    /**
     * Повертає ціну автомобіля в гривнях.
     *
     * @return ціна (&gt;&nbsp;0)
     */
    double getPrice() { return price; }

    /**
     * Повертає витрати палива автомобіля (л/100 км).
     *
     * @return витрати палива (&gt;&nbsp;0)
     */
    double getFuelConsumption() { return fuelConsumption; }

    /**
     * Повертає максимальну швидкість автомобіля в км/год.
     *
     * @return максимальна швидкість (&gt;&nbsp;0)
     */
    double getMaxSpeed() { return maxSpeed; }

    /**
     * Повертає рядкове представлення автомобіля.
     *
     * @return відформатований рядок з основними даними
     */
    @Override
    public String toString() {
        return String.format(
                "[%s] %s %s %d | %.2f грн | %.1f л/100км | %.0f км/год",
                getType(), make, model, year, price, fuelConsumption,
                maxSpeed);
    }
}

/**
 * Седан &mdash; легковий автомобіль з окремим закритим багажником.
 */
class Sedan extends Car {

    /** Об'єм багажника в літрах. */
    private final int trunkVolume;

    /**
     * Створює седан із заданими параметрами.
     *
     * @param make            марка
     * @param model           модель
     * @param year            рік випуску
     * @param price           ціна в гривнях
     * @param fuelConsumption витрати палива (л/100 км)
     * @param maxSpeed        максимальна швидкість (км/год)
     * @param trunkVolume     об'єм багажника в літрах; має бути &gt;&nbsp;0
     * @throws IllegalArgumentException якщо {@code trunkVolume} &le;&nbsp;0
     *                                  або порушено обмеження базового класу
     */
    Sedan(final String make, final String model, final int year,
            final double price, final double fuelConsumption,
            final double maxSpeed, final int trunkVolume) {
        super(make, model, year, price, fuelConsumption, maxSpeed);
        if (trunkVolume <= 0) {
            throw new IllegalArgumentException(
                    "Об'єм багажника має бути більшим за 0, отримано: "
                    + trunkVolume);
        }
        this.trunkVolume = trunkVolume;
    }

    /** @return {@code "Седан"} */
    @Override
    String getType() { return "Седан"; }

    /**
     * Повертає об'єм багажника в літрах.
     *
     * @return об'єм багажника (&gt;&nbsp;0)
     */
    int getTrunkVolume() { return trunkVolume; }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | багажник %d л", trunkVolume);
    }
}

/**
 * Позашляховик (SUV) &mdash; легковий автомобіль підвищеної прохідності.
 */
class SUV extends Car {

    /** Дорожній просвіт (кліренс) у міліметрах. */
    private final int groundClearance;

    /** Ознака наявності повного приводу (завжди {@code true}). */
    private final boolean allWheelDrive;

    /**
     * Створює позашляховик із заданими параметрами.
     *
     * @param make            марка
     * @param model           модель
     * @param year            рік випуску
     * @param price           ціна в гривнях
     * @param fuelConsumption витрати палива (л/100 км)
     * @param maxSpeed        максимальна швидкість (км/год)
     * @param groundClearance кліренс у мм; має бути &gt;&nbsp;0
     * @throws IllegalArgumentException якщо {@code groundClearance} &le;&nbsp;0
     *                                  або порушено обмеження базового класу
     */
    SUV(final String make, final String model, final int year,
            final double price, final double fuelConsumption,
            final double maxSpeed, final int groundClearance) {
        super(make, model, year, price, fuelConsumption, maxSpeed);
        if (groundClearance <= 0) {
            throw new IllegalArgumentException(
                    "Кліренс має бути більшим за 0, отримано: "
                    + groundClearance);
        }
        this.groundClearance = groundClearance;
        this.allWheelDrive = true;
    }

    /** @return {@code "Позашляховик"} */
    @Override
    String getType() { return "Позашляховик"; }

    /**
     * Повертає дорожній просвіт у міліметрах.
     *
     * @return кліренс (&gt;&nbsp;0)
     */
    int getGroundClearance() { return groundClearance; }

    /**
     * Повертає {@code true}, якщо автомобіль має повний привід.
     *
     * @return {@code true} для повного приводу
     */
    boolean isAllWheelDrive() { return allWheelDrive; }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | кліренс %d мм | %s",
                        groundClearance, allWheelDrive ? "4WD" : "2WD");
    }
}

/**
 * Хетчбек &mdash; компактний автомобіль із суміщеним пасажирським та
 * вантажним відсіком.
 */
class Hatchback extends Car {

    /** Мінімально допустима кількість дверей. */
    private static final int MIN_DOORS = 3;

    /** Максимально допустима кількість дверей. */
    private static final int MAX_DOORS = 5;

    /** Кількість дверей (3 або 5). */
    private final int doorCount;

    /**
     * Створює хетчбек із заданими параметрами.
     *
     * @param make            марка
     * @param model           модель
     * @param year            рік випуску
     * @param price           ціна в гривнях
     * @param fuelConsumption витрати палива (л/100 км)
     * @param maxSpeed        максимальна швидкість (км/год)
     * @param doorCount       кількість дверей: {@value #MIN_DOORS} або
     *                        {@value #MAX_DOORS}
     * @throws IllegalArgumentException якщо {@code doorCount} некоректний
     *                                  або порушено обмеження базового класу
     */
    Hatchback(final String make, final String model, final int year,
            final double price, final double fuelConsumption,
            final double maxSpeed, final int doorCount) {
        super(make, model, year, price, fuelConsumption, maxSpeed);
        if (doorCount != MIN_DOORS && doorCount != MAX_DOORS) {
            throw new IllegalArgumentException(
                    "Кількість дверей має бути " + MIN_DOORS + " або "
                    + MAX_DOORS + ", отримано: " + doorCount);
        }
        this.doorCount = doorCount;
    }

    /** @return {@code "Хетчбек"} */
    @Override
    String getType() { return "Хетчбек"; }

    /**
     * Повертає кількість дверей.
     *
     * @return {@value #MIN_DOORS} або {@value #MAX_DOORS}
     */
    int getDoorCount() { return doorCount; }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | %d дверей", doorCount);
    }
}

/**
 * Мінівен &mdash; автомобіль з розширеним пасажирським салоном.
 */
class Minivan extends Car {

    /** Мінімальна кількість місць. */
    private static final int MIN_SEATS = 5;

    /** Максимальна кількість місць. */
    private static final int MAX_SEATS = 9;

    /** Кількість пасажирських місць (включно з водієм). */
    private final int seats;

    /**
     * Створює мінівен із заданими параметрами.
     *
     * @param make            марка
     * @param model           модель
     * @param year            рік випуску
     * @param price           ціна в гривнях
     * @param fuelConsumption витрати палива (л/100 км)
     * @param maxSpeed        максимальна швидкість (км/год)
     * @param seats           кількість місць; має бути в межах
     *                        [{@value #MIN_SEATS},&nbsp;{@value #MAX_SEATS}]
     * @throws IllegalArgumentException якщо {@code seats} поза межами
     *                                  або порушено обмеження базового класу
     */
    Minivan(final String make, final String model, final int year,
            final double price, final double fuelConsumption,
            final double maxSpeed, final int seats) {
        super(make, model, year, price, fuelConsumption, maxSpeed);
        if (seats < MIN_SEATS || seats > MAX_SEATS) {
            throw new IllegalArgumentException(
                    "Кількість місць має бути в межах ["
                    + MIN_SEATS + ", " + MAX_SEATS
                    + "], отримано: " + seats);
        }
        this.seats = seats;
    }

    /** @return {@code "Мінівен"} */
    @Override
    String getType() { return "Мінівен"; }

    /**
     * Повертає кількість місць.
     *
     * @return кількість місць (від {@value #MIN_SEATS} до
     *         {@value #MAX_SEATS})
     */
    int getSeats() { return seats; }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | %d місць", seats);
    }
}

// ---------------------------------------------------------------------------
// Типізована колекція — двозв'язний список
// ---------------------------------------------------------------------------

/**
 * Типізована колекція, що зберігає об'єкти {@link Car} і реалізує
 * інтерфейс {@link List}.
 *
 * <p>Внутрішня структура &mdash; двозв'язний список із двома сторожовими
 * вузлами ({@code head} і {@code tail}), що спрощує вставку та видалення
 * на межах списку та робить ці операції рівномірними по всьому списку.</p>
 *
 * <p>Обхід у позиційних операціях ({@link #get(int)}, {@link #add(int, Car)}
 * тощо) виконується від найближчого кінця, тобто за O(n/2).</p>
 */
class CarLinkedList implements List<Car> {

    /**
     * Вузол двозв'язного списку.
     */
    private static final class Node {

        /** Автомобіль, що зберігається у вузлі. */
        Car data;

        /** Посилання на попередній вузол. */
        Node prev;

        /** Посилання на наступний вузол. */
        Node next;

        /**
         * Створює вузол із заданим автомобілем.
         *
         * @param data автомобіль
         */
        Node(final Car data) {
            this.data = data;
        }
    }

    /** Сторожовий вузол на початку списку (не містить даних). */
    private final Node head;

    /** Сторожовий вузол у кінці списку (не містить даних). */
    private final Node tail;

    /** Поточна кількість елементів. */
    private int size;

    // -----------------------------------------------------------------------
    // Конструктори
    // -----------------------------------------------------------------------

    /**
     * Створює порожній список автомобілів.
     */
    CarLinkedList() {
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.prev = head;
    }

    /**
     * Створює список з одного автомобіля.
     *
     * @param car початковий автомобіль; не може бути {@code null}
     * @throws NullPointerException якщо {@code car} дорівнює {@code null}
     */
    CarLinkedList(final Car car) {
        this();
        add(Objects.requireNonNull(car, "Автомобіль не може бути null."));
    }

    /**
     * Створює список, що містить усі елементи заданої стандартної колекції
     * у порядку, визначеному її ітератором.
     *
     * @param c стандартна колекція автомобілів; не може бути {@code null}
     *          або містити {@code null}-елементи
     * @throws NullPointerException     якщо {@code c} дорівнює {@code null}
     * @throws IllegalArgumentException якщо {@code c} містить
     *                                  {@code null}-елемент
     */
    CarLinkedList(final Collection<? extends Car> c) {
        this();
        addAll(Objects.requireNonNull(c,
                "Колекція-джерело не може бути null."));
    }

    // -----------------------------------------------------------------------
    // Допоміжні приватні методи
    // -----------------------------------------------------------------------

    /**
     * Повертає вузол за вказаним індексом, обходячи від найближчого кінця.
     *
     * @param index індекс (0-based); має бути в межах [0,&nbsp;size)
     * @return вузол з відповідним індексом
     * @throws IndexOutOfBoundsException якщо {@code index} поза допустимим
     *                                   діапазоном
     */
    private Node nodeAt(final int index) {
        checkIndex(index);
        Node curr;
        if (index < size / 2) {
            curr = head.next;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
        } else {
            curr = tail.prev;
            for (int i = size - 1; i > index; i--) {
                curr = curr.prev;
            }
        }
        return curr;
    }

    /**
     * Перевіряє коректність індексу для операцій доступу.
     *
     * @param index індекс для перевірки
     * @throws IndexOutOfBoundsException якщо {@code index < 0} або
     *                                   {@code index >= size}
     */
    private void checkIndex(final int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Індекс: " + index + ", Розмір: " + size);
        }
    }

    /**
     * Перевіряє коректність індексу для операцій вставки.
     *
     * @param index індекс для перевірки
     * @throws IndexOutOfBoundsException якщо {@code index < 0} або
     *                                   {@code index > size}
     */
    private void checkIndexForAdd(final int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Індекс: " + index + ", Розмір: " + size);
        }
    }

    /**
     * Вставляє новий вузол перед {@code next}.
     *
     * @param data автомобіль нового вузла
     * @param next вузол, перед яким виконується вставка
     */
    private void linkBefore(final Car data, final Node next) {
        final Node prev = next.prev;
        final Node newNode = new Node(data);
        newNode.prev = prev;
        newNode.next = next;
        prev.next = newNode;
        next.prev = newNode;
        size++;
    }

    /**
     * Вилучає вузол зі списку та повертає його дані.
     *
     * @param node вузол для вилучення (не сторожовий)
     * @return автомобіль вилученого вузла
     */
    private Car unlink(final Node node) {
        final Car data = node.data;
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.prev = null;
        node.next = null;
        size--;
        return data;
    }

    // -----------------------------------------------------------------------
    // Методи інтерфейсу List
    // -----------------------------------------------------------------------

    /** {@inheritDoc} */
    @Override
    public int size() {
        return size;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(final Object o) {
        return indexOf(o) >= 0;
    }

    /** {@inheritDoc} */
    @Override
    public Iterator<Car> iterator() {
        return listIterator();
    }

    /** {@inheritDoc} */
    @Override
    public Object[] toArray() {
        final Object[] arr = new Object[size];
        int i = 0;
        for (Node curr = head.next; curr != tail; curr = curr.next) {
            arr[i++] = curr.data;
        }
        return arr;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(final T[] a) {
        Objects.requireNonNull(a, "Масив не може бути null.");
        final T[] result = a.length >= size ? a
                : (T[]) java.lang.reflect.Array.newInstance(
                        a.getClass().getComponentType(), size);
        int i = 0;
        for (Node curr = head.next; curr != tail; curr = curr.next) {
            ((Object[]) result)[i++] = curr.data;
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException якщо {@code e} дорівнює {@code null}
     */
    @Override
    public boolean add(final Car e) {
        if (e == null) {
            throw new IllegalArgumentException(
                    "Автомобіль не може бути null.");
        }
        linkBefore(e, tail);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean remove(final Object o) {
        for (Node curr = head.next; curr != tail; curr = curr.next) {
            if (Objects.equals(curr.data, o)) {
                unlink(curr);
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean containsAll(final Collection<?> c) {
        for (final Object e : c) {
            if (!contains(e)) {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final Collection<? extends Car> c) {
        return addAll(size, c);
    }

    /** {@inheritDoc} */
    @Override
    public boolean addAll(final int index,
            final Collection<? extends Car> c) {
        checkIndexForAdd(index);
        Objects.requireNonNull(c, "Колекція не може бути null.");
        final Node succ = (index == size) ? tail : nodeAt(index);
        boolean modified = false;
        for (final Car e : c) {
            if (e == null) {
                throw new IllegalArgumentException(
                        "Колекція містить null-елемент.");
            }
            linkBefore(e, succ);
            modified = true;
        }
        return modified;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeAll(final Collection<?> c) {
        Objects.requireNonNull(c, "Колекція не може бути null.");
        boolean modified = false;
        Node curr = head.next;
        while (curr != tail) {
            final Node next = curr.next;
            if (c.contains(curr.data)) {
                unlink(curr);
                modified = true;
            }
            curr = next;
        }
        return modified;
    }

    /** {@inheritDoc} */
    @Override
    public boolean retainAll(final Collection<?> c) {
        Objects.requireNonNull(c, "Колекція не може бути null.");
        boolean modified = false;
        Node curr = head.next;
        while (curr != tail) {
            final Node next = curr.next;
            if (!c.contains(curr.data)) {
                unlink(curr);
                modified = true;
            }
            curr = next;
        }
        return modified;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Посилання у вузлах обнуляються для допомоги збирачу сміття.</p>
     */
    @Override
    public void clear() {
        Node curr = head.next;
        while (curr != tail) {
            final Node next = curr.next;
            curr.data = null;
            curr.prev = null;
            curr.next = null;
            curr = next;
        }
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Car get(final int index) {
        return nodeAt(index).data;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Car set(final int index, final Car element) {
        Objects.requireNonNull(element, "Автомобіль не може бути null.");
        final Node node = nodeAt(index);
        final Car old = node.data;
        node.data = element;
        return old;
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     * @throws IllegalArgumentException  якщо {@code element} є {@code null}
     */
    @Override
    public void add(final int index, final Car element) {
        Objects.requireNonNull(element, "Автомобіль не може бути null.");
        checkIndexForAdd(index);
        final Node succ = (index == size) ? tail : nodeAt(index);
        linkBefore(element, succ);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException {@inheritDoc}
     */
    @Override
    public Car remove(final int index) {
        return unlink(nodeAt(index));
    }

    /** {@inheritDoc} */
    @Override
    public int indexOf(final Object o) {
        int i = 0;
        for (Node curr = head.next; curr != tail; curr = curr.next, i++) {
            if (Objects.equals(curr.data, o)) {
                return i;
            }
        }
        return -1;
    }

    /** {@inheritDoc} */
    @Override
    public int lastIndexOf(final Object o) {
        int i = size - 1;
        for (Node curr = tail.prev; curr != head; curr = curr.prev, i--) {
            if (Objects.equals(curr.data, o)) {
                return i;
            }
        }
        return -1;
    }

    /** {@inheritDoc} */
    @Override
    public ListIterator<Car> listIterator() {
        return listIterator(0);
    }

    /**
     * {@inheritDoc}
     *
     * @throws IndexOutOfBoundsException якщо {@code index < 0} або
     *                                   {@code index > size}
     */
    @Override
    public ListIterator<Car> listIterator(final int index) {
        checkIndexForAdd(index);
        return new ListIter(index);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Повертає новий {@link CarLinkedList} з елементами позицій
     * [{@code fromIndex},&nbsp;{@code toIndex}). Зміни у підсписку
     * не відображаються в оригінальному списку.</p>
     *
     * @throws IndexOutOfBoundsException якщо {@code fromIndex < 0},
     *                                   {@code toIndex > size} або
     *                                   {@code fromIndex > toIndex}
     */
    @Override
    public List<Car> subList(final int fromIndex, final int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException(
                    "fromIndex=" + fromIndex + ", toIndex=" + toIndex
                    + ", size=" + size);
        }
        final CarLinkedList sub = new CarLinkedList();
        Node curr = (fromIndex == size) ? tail : nodeAt(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            sub.add(curr.data);
            curr = curr.next;
        }
        return sub;
    }

    /**
     * Повертає рядкове представлення списку у форматі
     * {@code [element0, element1, ...]}.
     *
     * @return рядок зі списком автомобілів
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        Node curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            if (curr.next != tail) {
                sb.append(",\n ");
            }
            curr = curr.next;
        }
        return sb.append(']').toString();
    }

    // -----------------------------------------------------------------------
    // Внутрішній клас ListIterator
    // -----------------------------------------------------------------------

    /**
     * Реалізація {@link ListIterator} для {@link CarLinkedList}.
     *
     * <p>Підтримує двосторонній обхід та операції {@link #add},
     * {@link #set}, {@link #remove} під час ітерування.</p>
     */
    private final class ListIter implements ListIterator<Car> {

        /**
         * Останній вузол, повернений {@link #next()} або
         * {@link #previous()}; {@code null} до першого виклику або після
         * {@link #add}/{@link #remove}.
         */
        private Node lastReturned;

        /** Вузол, що буде повернений наступним {@link #next()}. */
        private Node nextNode;

        /** Індекс вузла, що буде повернений {@link #next()}. */
        private int nextIndex;

        /**
         * Створює ітератор, встановлений на позицію {@code index}.
         *
         * @param index стартова позиція (від 0 до {@code size} включно)
         */
        ListIter(final int index) {
            nextNode = (index == size) ? tail : nodeAt(index);
            nextIndex = index;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        /**
         * {@inheritDoc}
         *
         * @throws NoSuchElementException якщо немає наступного елемента
         */
        @Override
        public Car next() {
            if (!hasNext()) {
                throw new NoSuchElementException(
                        "Немає наступного елемента.");
            }
            lastReturned = nextNode;
            nextNode = nextNode.next;
            nextIndex++;
            return lastReturned.data;
        }

        /** {@inheritDoc} */
        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        /**
         * {@inheritDoc}
         *
         * @throws NoSuchElementException якщо немає попереднього елемента
         */
        @Override
        public Car previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException(
                        "Немає попереднього елемента.");
            }
            lastReturned = nextNode = nextNode.prev;
            nextIndex--;
            return lastReturned.data;
        }

        /** {@inheritDoc} */
        @Override
        public int nextIndex() {
            return nextIndex;
        }

        /** {@inheritDoc} */
        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        /**
         * {@inheritDoc}
         *
         * @throws IllegalStateException якщо {@link #next()} або
         *                               {@link #previous()} ще не викликався
         *                               або після виклику вже відбувся
         *                               {@link #remove()} чи {@link #add}
         */
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException(
                        "Немає активного елемента для видалення.");
            }
            final Node lastNext = lastReturned.next;
            unlink(lastReturned);
            if (nextNode == lastReturned) {
                nextNode = lastNext;
            } else {
                nextIndex--;
            }
            lastReturned = null;
        }

        /**
         * {@inheritDoc}
         *
         * @throws IllegalStateException якщо {@link #next()} або
         *                               {@link #previous()} ще не викликався
         */
        @Override
        public void set(final Car e) {
            if (lastReturned == null) {
                throw new IllegalStateException(
                        "Немає активного елемента для заміни.");
            }
            Objects.requireNonNull(e, "Автомобіль не може бути null.");
            lastReturned.data = e;
        }

        /**
         * {@inheritDoc}
         *
         * <p>Вставляє елемент перед поточним {@code next}.</p>
         *
         * @throws IllegalArgumentException якщо {@code e} є {@code null}
         */
        @Override
        public void add(final Car e) {
            Objects.requireNonNull(e, "Автомобіль не може бути null.");
            lastReturned = null;
            linkBefore(e, nextNode);
            nextIndex++;
        }
    }
}