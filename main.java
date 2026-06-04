package lab;

import java.util.ArrayList;
import java.util.List;

/**
 * Точка входу програми, що демонструє роботу таксопарку.
 *
 * <p>Виконує такі дії:</p>
 * <ol>
 *   <li>Формує автопарк з різних типів легкових автомобілів.</li>
 *   <li>Обчислює загальну вартість автопарку.</li>
 *   <li>Сортує автомобілі за витратами палива (зростання).</li>
 *   <li>Знаходить автомобілі у заданому діапазоні максимальної швидкості.</li>
 * </ol>
 *
 * @author Sivash
 * @version 1.0
 */
final class TaxiparkDemo {

    private TaxiparkDemo() {
    }

    /**
     * Виконавчий метод програми.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(final String[] args) {
        try {
            final Car[] fleet = {
                new Sedan(    "Toyota",     "Camry",   2021, 1_150_000.0,  8.5, 210, 480),
                new Sedan(    "Honda",      "Accord",  2022, 1_240_000.0,  7.9, 225, 520),
                new SUV(      "Toyota",     "RAV4",    2021, 1_560_000.0, 10.2, 195, 220),
                new SUV(      "Ford",       "Explorer",2020, 1_980_000.0, 13.5, 205, 210),
                new Hatchback("Volkswagen", "Golf",    2022,   910_000.0,  6.8, 200, 5),
                new Hatchback("Skoda",      "Fabia",   2023,   780_000.0,  5.9, 180, 3),
                new Minivan(  "Honda",      "Odyssey", 2020, 1_730_000.0, 12.1, 185, 8)
            };

            final Taxipark park = new Taxipark(fleet);

            System.out.println("=== Автомобілі таксопарку ===");
            printCars(park.getCars());

            System.out.printf("%nЗагальна вартість автопарку: %.2f грн%n",
                    park.getTotalCost());

            park.sortByFuelConsumption();
            System.out.println(
                    "\nАвтомобілі після сортування за витратами палива:");
            printCars(park.getCars());

            final double minSpeed = 190.0;
            final double maxSpeed = 210.0;
            final Car[] found = park.findBySpeedRange(minSpeed, maxSpeed);

            System.out.printf(
                    "%nАвтомобілі з максимальною швидкістю від %.0f"
                    + " до %.0f км/год:%n", minSpeed, maxSpeed);
            if (found.length == 0) {
                System.out.println("  (не знайдено)");
            } else {
                printCars(found);
            }

        } catch (final IllegalArgumentException e) {
            System.err.println("Помилка вхідних даних: " + e.getMessage());
        } catch (final Exception e) {
            System.err.println("Непередбачена помилка: " + e.getMessage());
        }
    }

    /**
     * Виводить масив автомобілів, нумеруючи кожен рядок.
     *
     * @param cars масив автомобілів для виведення; не може бути {@code null}
     */
    private static void printCars(final Car[] cars) {
        for (int i = 0; i < cars.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, cars[i]);
        }
    }
}

// ---------------------------------------------------------------------------
// Ієрархія автомобілів
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

    /** Найбільш ранній рік, коли міг бути виготовлений автомобіль. */
    private static final int MIN_YEAR = 1886;

    /** Найбільш пізній допустимий рік випуску. */
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
     *                        [{@value #MIN_YEAR}, {@value #MAX_YEAR}]
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
    String getMake() {
        return make;
    }

    /**
     * Повертає модель автомобіля.
     *
     * @return модель
     */
    String getModel() {
        return model;
    }

    /**
     * Повертає рік випуску автомобіля.
     *
     * @return рік випуску
     */
    int getYear() {
        return year;
    }

    /**
     * Повертає ціну автомобіля в гривнях.
     *
     * @return ціна (&gt;&nbsp;0)
     */
    double getPrice() {
        return price;
    }

    /**
     * Повертає витрати палива автомобіля в літрах на 100 км.
     *
     * @return витрати палива (&gt;&nbsp;0)
     */
    double getFuelConsumption() {
        return fuelConsumption;
    }

    /**
     * Повертає максимальну швидкість автомобіля в км/год.
     *
     * @return максимальна швидкість (&gt;&nbsp;0)
     */
    double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Повертає рядкове представлення автомобіля із зазначенням типу,
     * марки, моделі, року, ціни, витрат палива та максимальної швидкості.
     *
     * @return відформатований рядок з даними автомобіля
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
 *
 * <p>Додаткова характеристика: об'єм багажника в літрах.</p>
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

    /**
     * {@inheritDoc}
     *
     * @return {@code "Седан"}
     */
    @Override
    String getType() {
        return "Седан";
    }

    /**
     * Повертає об'єм багажника в літрах.
     *
     * @return об'єм багажника (&gt;&nbsp;0)
     */
    int getTrunkVolume() {
        return trunkVolume;
    }

    /**
     * Повертає рядкове представлення седана з додатковим зазначенням
     * об'єму багажника.
     *
     * @return відформатований рядок з даними седана
     */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | багажник %d л", trunkVolume);
    }
}

/**
 * Позашляховик (SUV) &mdash; легковий автомобіль підвищеної прохідності.
 *
 * <p>Додаткові характеристики: дорожній просвіт (кліренс) у міліметрах
 * та наявність повного приводу.</p>
 */
class SUV extends Car {

    /** Дорожній просвіт (кліренс) у міліметрах. */
    private final int groundClearance;

    /** Ознака наявності повного приводу. */
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

    /**
     * {@inheritDoc}
     *
     * @return {@code "Позашляховик"}
     */
    @Override
    String getType() {
        return "Позашляховик";
    }

    /**
     * Повертає дорожній просвіт автомобіля в міліметрах.
     *
     * @return кліренс (&gt;&nbsp;0)
     */
    int getGroundClearance() {
        return groundClearance;
    }

    /**
     * Повертає {@code true}, якщо автомобіль має повний привід.
     *
     * @return {@code true} для повного приводу, {@code false} для переднього
     */
    boolean isAllWheelDrive() {
        return allWheelDrive;
    }

    /**
     * Повертає рядкове представлення позашляховика з додатковим зазначенням
     * кліренсу та типу приводу.
     *
     * @return відформатований рядок з даними позашляховика
     */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | кліренс %d мм | %s",
                        groundClearance, allWheelDrive ? "4WD" : "2WD");
    }
}

/**
 * Хетчбек &mdash; компактний легковий автомобіль із суміщеним
 * пасажирським та вантажним відсіком.
 *
 * <p>Додаткова характеристика: кількість дверей (3 або 5).</p>
 */
class Hatchback extends Car {

    /** Мінімально допустима кількість дверей хетчбека. */
    private static final int MIN_DOORS = 3;

    /** Максимально допустима кількість дверей хетчбека. */
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
     * @param doorCount       кількість дверей; допустимі значення:
     *                        {@value #MIN_DOORS} або {@value #MAX_DOORS}
     * @throws IllegalArgumentException якщо {@code doorCount} не дорівнює
     *                                  {@value #MIN_DOORS} або
     *                                  {@value #MAX_DOORS}, або порушено
     *                                  обмеження базового класу
     */
    Hatchback(final String make, final String model, final int year,
            final double price, final double fuelConsumption,
            final double maxSpeed, final int doorCount) {
        super(make, model, year, price, fuelConsumption, maxSpeed);
        if (doorCount != MIN_DOORS && doorCount != MAX_DOORS) {
            throw new IllegalArgumentException(
                    "Кількість дверей хетчбека має бути "
                    + MIN_DOORS + " або " + MAX_DOORS
                    + ", отримано: " + doorCount);
        }
        this.doorCount = doorCount;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code "Хетчбек"}
     */
    @Override
    String getType() {
        return "Хетчбек";
    }

    /**
     * Повертає кількість дверей хетчбека.
     *
     * @return кількість дверей ({@value #MIN_DOORS} або {@value #MAX_DOORS})
     */
    int getDoorCount() {
        return doorCount;
    }

    /**
     * Повертає рядкове представлення хетчбека з додатковим зазначенням
     * кількості дверей.
     *
     * @return відформатований рядок з даними хетчбека
     */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | %d дверей", doorCount);
    }
}

/**
 * Мінівен &mdash; легковий автомобіль з розширеним пасажирським салоном.
 *
 * <p>Додаткова характеристика: кількість пасажирських місць.</p>
 */
class Minivan extends Car {

    /** Мінімальна кількість місць у мінівені. */
    private static final int MIN_SEATS = 5;

    /** Максимальна кількість місць у мінівені. */
    private static final int MAX_SEATS = 9;

    /** Кількість пасажирських місць (включно з місцем водія). */
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
     *                        [{@value #MIN_SEATS}, {@value #MAX_SEATS}]
     * @throws IllegalArgumentException якщо {@code seats} виходить за межі
     *                                  або порушено обмеження базового класу
     */
    Minivan(final String make, final String model, final int year,
            final double price, final double fuelConsumption,
            final double maxSpeed, final int seats) {
        super(make, model, year, price, fuelConsumption, maxSpeed);
        if (seats < MIN_SEATS || seats > MAX_SEATS) {
            throw new IllegalArgumentException(
                    "Кількість місць мінівена має бути в межах ["
                    + MIN_SEATS + ", " + MAX_SEATS
                    + "], отримано: " + seats);
        }
        this.seats = seats;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@code "Мінівен"}
     */
    @Override
    String getType() {
        return "Мінівен";
    }

    /**
     * Повертає кількість пасажирських місць мінівена.
     *
     * @return кількість місць (від {@value #MIN_SEATS} до
     *         {@value #MAX_SEATS})
     */
    int getSeats() {
        return seats;
    }

    /**
     * Повертає рядкове представлення мінівена з додатковим зазначенням
     * кількості місць.
     *
     * @return відформатований рядок з даними мінівена
     */
    @Override
    public String toString() {
        return super.toString()
                + String.format(" | %d місць", seats);
    }
}

// ---------------------------------------------------------------------------
// Таксопарк
// ---------------------------------------------------------------------------

/**
 * Таксопарк &mdash; колекція легкових автомобілів ({@link Car}).
 *
 * <p>Надає операції:</p>
 * <ul>
 *   <li>{@link #getTotalCost()} &mdash; загальна вартість автопарку;</li>
 *   <li>{@link #sortByFuelConsumption()} &mdash; сортування за витратами
 *       палива (метод бульбашки, за зростанням);</li>
 *   <li>{@link #findBySpeedRange(double, double)} &mdash; пошук автомобілів
 *       у заданому діапазоні максимальної швидкості.</li>
 * </ul>
 */
class Taxipark {

    /** Масив автомобілів таксопарку. */
    private final Car[] cars;

    /**
     * Створює таксопарк із заданого масиву автомобілів.
     *
     * <p>Масив копіюється захисним чином, щоб подальші зміни зовнішнього
     * масиву не впливали на стан таксопарку.</p>
     *
     * @param cars масив автомобілів; не може бути {@code null}, порожнім
     *             або містити {@code null}-елементи
     * @throws IllegalArgumentException якщо {@code cars} є {@code null},
     *                                  порожнім або містить
     *                                  {@code null}-елементи
     */
    Taxipark(final Car[] cars) {
        if (cars == null) {
            throw new IllegalArgumentException(
                    "Масив автомобілів не може бути null.");
        }
        if (cars.length == 0) {
            throw new IllegalArgumentException(
                    "Таксопарк не може бути порожнім.");
        }
        for (int i = 0; i < cars.length; i++) {
            if (cars[i] == null) {
                throw new IllegalArgumentException(
                        "Елемент масиву з індексом " + i
                        + " є null.");
            }
        }
        this.cars = cars.clone();
    }

    /**
     * Повертає захисну копію масиву автомобілів таксопарку.
     *
     * @return масив {@link Car}; не {@code null}, не порожній
     */
    Car[] getCars() {
        return cars.clone();
    }

    /**
     * Обчислює загальну вартість усіх автомобілів таксопарку в гривнях.
     *
     * @return сума цін усіх автомобілів (&gt;&nbsp;0)
     */
    double getTotalCost() {
        double total = 0.0;
        for (int i = 0; i < cars.length; i++) {
            total += cars[i].getPrice();
        }
        return total;
    }

    /**
     * Сортує автомобілі таксопарку за зростанням витрат палива
     * методом бульбашки.
     *
     * <p>Після виклику порядок елементів у внутрішньому масиві змінюється;
     * наступний виклик {@link #getCars()} поверне відсортований масив.</p>
     */
    void sortByFuelConsumption() {
        for (int i = 0; i < cars.length - 1; i++) {
            for (int j = 0; j < cars.length - 1 - i; j++) {
                if (cars[j].getFuelConsumption()
                        > cars[j + 1].getFuelConsumption()) {
                    final Car temp = cars[j];
                    cars[j] = cars[j + 1];
                    cars[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Знаходить автомобілі, чия максимальна швидкість входить у діапазон
     * [{@code minSpeed}, {@code maxSpeed}] включно.
     *
     * @param minSpeed нижня межа діапазону швидкості (км/год); має бути
     *                 &ge;&nbsp;0
     * @param maxSpeed верхня межа діапазону швидкості (км/год); має бути
     *                 &ge;&nbsp;{@code minSpeed}
     * @return масив автомобілів, що відповідають діапазону; може бути
     *         порожнім, але не {@code null}
     * @throws IllegalArgumentException якщо {@code minSpeed} &lt;&nbsp;0 або
     *                                  {@code minSpeed} &gt;&nbsp;{@code maxSpeed}
     */
    Car[] findBySpeedRange(final double minSpeed, final double maxSpeed) {
        if (minSpeed < 0) {
            throw new IllegalArgumentException(
                    "Мінімальна швидкість не може бути від'ємною, отримано: "
                    + minSpeed);
        }
        if (minSpeed > maxSpeed) {
            throw new IllegalArgumentException(
                    "Мінімальна швидкість (" + minSpeed
                    + ") не може перевищувати максимальну (" + maxSpeed
                    + ").");
        }

        final List<Car> result = new ArrayList<Car>();
        for (int i = 0; i < cars.length; i++) {
            final double speed = cars[i].getMaxSpeed();
            if (speed >= minSpeed && speed <= maxSpeed) {
                result.add(cars[i]);
            }
        }
        return result.toArray(new Car[result.size()]);
    }
}
