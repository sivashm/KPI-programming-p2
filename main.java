import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * Виконавчий клас програми.
 *
 * <p>У методі {@link #main(String[])} створюється масив об'єктів класу
 * {@link Airplane}, який сортується одночасно за двома критеріями:
 * за виробником — за зростанням, а за максимальною швидкістю —
 * за спаданням. Сортування виконується стандартним засобом
 * {@link java.util.Arrays#sort(Object[], Comparator)}.</p>
 *
 * <p>Після сортування у масиві відшукується об'єкт, ідентичний
 * заданому, за допомогою методу {@link Airplane#equals(Object)}.</p>
 *
 * <p>Цей клас оголошено першим у файлі, тому програму можна запускати
 * як у режимі компіляції ({@code javac main.java && java AirplaneApp}),
 * так і в режимі одного файла-джерела ({@code java main.java}).</p>
 *
 * @author Maksym Sivash
 * @version 1.0
 */
final class AirplaneApp {

    /**
     * Закритий конструктор: клас є утилітарним і не призначений
     * для створення екземплярів.
     */
    private AirplaneApp() {
    }

    /**
     * Точка входу в програму (виконавчий метод).
     *
     * <p>Усі змінні оголошені та ініціалізовані безпосередньо тут.</p>
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(final String[] args) {

        // Створення та ініціалізація масиву об'єктів-літаків.
        final Airplane[] airplanes = {
                new Airplane("Boeing 737", "Boeing", 876, 215, 2.5),
                new Airplane("A320", "Airbus", 871, 180, 2.4),
                new Airplane("A380", "Airbus", 945, 853, 12.0),
                new Airplane("Boeing 747", "Boeing", 988, 660, 11.0),
                new Airplane("E190", "Embraer", 829, 114, 1.8),
                new Airplane("A320", "Airbus", 950, 180, 2.4)
        };

        // Вивід вихідного (невідсортованого) масиву.
        System.out.println("Вихідний масив:");
        printArray(airplanes);

        // Комбінований компаратор:
        //   1) за виробником — за зростанням (алфавітний порядок);
        //   2) за максимальною швидкістю — за спаданням (reversed).
        final Comparator<Airplane> comparator = Comparator
                .comparing(Airplane::getManufacturer)
                .thenComparing(Comparator
                        .comparingInt(Airplane::getMaxSpeed)
                        .reversed());

        // Сортування масиву стандартним засобом.
        Arrays.sort(airplanes, comparator);

        // Вивід відсортованого масиву.
        System.out.println("\nВідсортований масив "
                + "(виробник \u2191, швидкість \u2193):");
        printArray(airplanes);

        // Заданий об'єкт-зразок, ідентичний якому слід знайти у масиві.
        final Airplane target =
                new Airplane("Boeing 747", "Boeing", 988, 660, 11.0);

        // Пошук об'єкта, ідентичного заданому.
        final int foundIndex = findIdentical(airplanes, target);

        // Вивід результату пошуку.
        System.out.println("\nПошук об'єкта: " + target);
        if (foundIndex >= 0) {
            System.out.println("Знайдено за індексом " + foundIndex
                    + ": " + airplanes[foundIndex]);
        } else {
            System.out.println("Ідентичний об'єкт у масиві не знайдено.");
        }
    }

    /**
     * Відшукує у масиві перший об'єкт, ідентичний заданому.
     *
     * @param airplanes масив літаків для пошуку
     * @param target    об'єкт-зразок
     * @return індекс знайденого об'єкта або {@code -1}, якщо його немає
     */
    private static int findIdentical(final Airplane[] airplanes,
                                     final Airplane target) {
        for (int i = 0; i < airplanes.length; i++) {
            if (airplanes[i].equals(target)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Виводить усі елементи масиву літаків у консоль.
     *
     * @param airplanes масив літаків для виводу
     */
    private static void printArray(final Airplane[] airplanes) {
        for (final Airplane airplane : airplanes) {
            System.out.println("  " + airplane);
        }
    }
}

/**
 * Клас-модель, що описує літак.
 *
 * <p>Об'єкт класу є незмінним (immutable): усі поля оголошені як
 * {@code final} і задаються один раз через конструктор. Це гарантує
 * коректну роботу методів {@link #equals(Object)} та {@link #hashCode()}
 * під час сортування й пошуку елементів у масиві.</p>
 *
 * <p>Клас містить п'ять полів, що характеризують літак:
 * модель, виробник, максимальна швидкість, пасажиромісткість
 * та витрата палива.</p>
 *
 * @author Студент
 * @version 1.1
 */
final class Airplane {

    /** Назва (модель) літака, наприклад {@code "Boeing 737"}. */
    private final String model;

    /** Назва компанії-виробника літака. */
    private final String manufacturer;

    /** Максимальна швидкість літака, км/год. */
    private final int maxSpeed;

    /** Пасажиромісткість (кількість місць) літака. */
    private final int passengerCapacity;

    /** Середня витрата палива, літрів на 100 км. */
    private final double fuelConsumption;

    /**
     * Створює новий об'єкт {@code Airplane} із заданими характеристиками.
     *
     * @param model             модель літака
     * @param manufacturer      виробник літака
     * @param maxSpeed          максимальна швидкість, км/год
     * @param passengerCapacity пасажиромісткість, кількість місць
     * @param fuelConsumption   витрата палива, л/100 км
     */
    Airplane(final String model,
             final String manufacturer,
             final int maxSpeed,
             final int passengerCapacity,
             final double fuelConsumption) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.maxSpeed = maxSpeed;
        this.passengerCapacity = passengerCapacity;
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * Повертає модель літака.
     *
     * @return модель літака
     */
    public String getModel() {
        return model;
    }

    /**
     * Повертає виробника літака.
     *
     * @return виробник літака
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Повертає максимальну швидкість літака.
     *
     * @return максимальна швидкість, км/год
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Повертає пасажиромісткість літака.
     *
     * @return кількість пасажирських місць
     */
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    /**
     * Повертає витрату палива літака.
     *
     * @return витрата палива, л/100 км
     */
    public double getFuelConsumption() {
        return fuelConsumption;
    }

    /**
     * Порівнює цей літак із заданим об'єктом на повну ідентичність.
     *
     * <p>Два літаки вважаються рівними, якщо всі п'ять полів збігаються.
     * Метод використовується для пошуку об'єкта, ідентичного заданому.</p>
     *
     * @param o об'єкт для порівняння
     * @return {@code true}, якщо об'єкти ідентичні, інакше {@code false}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Airplane airplane = (Airplane) o;
        return maxSpeed == airplane.maxSpeed
                && passengerCapacity == airplane.passengerCapacity
                && Double.compare(fuelConsumption, airplane.fuelConsumption) == 0
                && Objects.equals(model, airplane.model)
                && Objects.equals(manufacturer, airplane.manufacturer);
    }

    /**
     * Обчислює хеш-код об'єкта на основі всіх його полів.
     *
     * @return хеш-код об'єкта
     */
    @Override
    public int hashCode() {
        return Objects.hash(model, manufacturer, maxSpeed,
                passengerCapacity, fuelConsumption);
    }

    /**
     * Повертає рядкове подання літака у зручному для виводу форматі.
     *
     * @return рядок із характеристиками літака
     */
    @Override
    public String toString() {
        return String.format(
                "Airplane{модель='%s', виробник='%s', "
                        + "макс. швидкість=%d км/год, місць=%d, "
                        + "витрата палива=%.1f л/100км}",
                model, manufacturer, maxSpeed,
                passengerCapacity, fuelConsumption);
    }
}