import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Car {
    private final BufferedImage originalImage;          // Исходное изображение машины
    private final BufferedImage rotateImage;            // Повёрнутое изображение машины
    private int x, y;                                   // Позиция машины на экране
    private final int number;                           // Номер машины (1-5)
    private boolean finished;                           // Флаг, достигла ли машина финиша
    private static final Random random = new Random();  // Генератор случайных чисел

    /*Объяснение переменных:
    originalImage - хранит оригинальное изображение машины до поворота
    rotateImage - хранит изображение после поворота (используется для отрисовки)
    x, y - координаты машины на экране (левый верхний угол изображения)
    number - уникальный номер машины (не изменяется после создания)
    finished - флаг, показывающий, завершила ли машина гонку
    random - общий для всех машин генератор случайных чисел для движения*/

    public Car(BufferedImage image, int x, int y, int number) {
        this.originalImage = image;
        this.rotateImage = rotateImage(image, 90); // Поворачиваем изображение
        this.x = x;
        this.y = y;
        this.number = number;
        this.finished = false;
    }
    /*Логика:
    Принимает исходное изображение, начальные координаты и номер
    Сразу создаёт повёрнутую версию изображения (на 90 градусов)
    Устанавливает начальное состояние - гонка не завершена (finished = false)*/

    // Метод для поворота изображения
    private BufferedImage rotateImage(BufferedImage img, double angle) {
        // Переводим градусы в радианы
        double rads = Math.toRadians(angle);
        // Вычисляем синус и косинус угла поворота
        double sin = Math.abs(Math.sin(rads));
        double cos = Math.abs(Math.cos(rads));
        // Рассчитываем новые размеры изображения после поворота
        int newWidth = (int) Math.floor(img.getWidth() * cos + img.getHeight() * sin);
        int newHeight = (int) Math.floor(img.getHeight() * cos + img.getWidth() * sin);

        // Создаём новое изображение для повёрнутой картинки
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        // Настраиваем преобразование
        AffineTransform at = new AffineTransform();
        at.translate(newWidth / 2, newHeight / 2);      // Перемещаем в центр
        at.rotate(rads);                                       // Поворачиваем
        at.translate(-img.getWidth() / 2, -img.getHeight() / 2); // Возвращаем обратно

        g2d.setTransform(at); // Применяем преобразование
        g2d.drawImage(img, 0, 0, null); // Рисуем исходное изображение с преобразованием
        g2d.dispose(); // Освобождаем ресурсы

        return rotated;

        /*Почему именно так:
        Перевод в радианы нужен, потому что тригонометрические функции в Java работают с радианами
        Новые размеры рассчитываются с учётом того, что при повороте изображение может занимать больше места
        AffineTransform - стандартный способ в Java для геометрических преобразований
        Последовательность преобразований (перемещение-поворот-перемещение) позволяет повернуть изображение вокруг центра*/
    }

    public void move() {
        if (!finished) {
            x += random.nextInt(5) + 1;
        }
    }
    /*Логика:
    Машина движется только если не достигла финиша
    Каждое движение - случайное число от 1 до 5 пикселей вправо
    random.nextInt(5) даёт значения 0-4, поэтому +1 для диапазона 1-5*/

    public BufferedImage getImage() { return rotateImage; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getNumber() { return number; }
    public boolean isFinished() { return finished; }
    public void setFinished(boolean finished) { this.finished = finished; }
}

public class Races extends JPanel {
    private static final int WIDTH = 1000;       // Ширина окна
    private static final int HEIGHT = 400;      // Высота окна
    private static final int CAR_COUNT = 5;     // Количество машин
    private static final int START_X = 50;      // Стартовая позиция X
    private static final int START_Y = 50;      // Стартовая позиция Y первой машины
    private static final int Y_OFFSET = 70;     // Интервал между машинами по вертикали
    private static final int FINISH_LINE = WIDTH - 50;  // Позиция финишной линии
    private static final int MAX_CAR_WIDTH = 100;  // Макс. ширина изображения машины
    private static final int MAX_CAR_HEIGHT = 60; // Макс. высота изображения машины

    private List<Car> cars;      // Список всех машин
    private Timer timer;         // Таймер анимации гонки
    private int winner = -1;     // Номер победителя (-1 - гонка идёт)
    private int countdown = 3;   // Обратный отсчёт перед стартом
    private Timer countdownTimer; // Таймер обратного отсчёта


    public Races() {
        cars = createCars();    // Создаём машины
        setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Устанавливаем размер окна

        // Таймер для обратного отсчета
        countdownTimer = new Timer(1000, e -> {
            countdown--;    // Уменьшаем счётчик
            repaint();      // Перерисовываем экран
            if (countdown <= 0) {   // Если отсчёт закончился
                ((Timer)e.getSource()).stop();  // Останавливаем таймер
                startRace();    // Начинаем гонку
            }
        });
        countdownTimer.start(); // Запускаем отсчёт
    }

    /*Логика работы:
    Создаёт машины через createCars()
    Устанавливает размеры окна
    Настраивает таймер, который срабатывает каждую секунду
    При каждом срабатывании уменьшает счётчик и перерисовывает окно
    Когда счётчик достигает 0, запускает гонку*/

    // Метод для пропорционального уменьшения изображения
    private BufferedImage resizeImage(BufferedImage originalImage) {
        // Получаем исходные размеры
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Вычисляем новые размеры с сохранением пропорций
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        // Если ширина больше максимальной - масштабируем по ширине
        if (originalWidth > MAX_CAR_WIDTH) {
            newWidth = MAX_CAR_WIDTH;
            newHeight = (newWidth * originalHeight) / originalWidth;    // Сохраняем пропорции
        }

        // Если после масштабирования высота всё ещё большая - масштабируем по высоте
        if (newHeight > MAX_CAR_HEIGHT) {
            newHeight = MAX_CAR_HEIGHT;
            newWidth = (newHeight * originalWidth) / originalHeight;    // Сохраняем пропорции
        }

        // Создаём новое изображение нужного размера
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        // Устанавливаем режим интерполяции для качественного масштабирования
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // Рисуем исходное изображение с масштабированием
        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();    // Освобождаем ресурсы
        return resizedImage;
    }
    /*Почему именно так:
    Сначала проверяем ширину, потом высоту, чтобы гарантировать, что ни один размер не превысит максимум
    Пропорции сохраняются за счёт пересчёта одного размера на основе другого
    TYPE_INT_ARGB - формат изображения с прозрачностью
    Билинейная интерполяция даёт лучшее качество при масштабировании*/

    private List<Car> createCars() {
        List<Car> cars = new ArrayList<>(); // Создаём список для машин
        try {
            // Создаём 5 машин
            for (int i = 0; i < CAR_COUNT; i++) {
                // Загружаем изображение (ожидаются файлы car1.png, car2.png и т.д.)
                BufferedImage originalimage = ImageIO.read(new File("3.2.6_Races/Image/car" + (i+1) + ".png"));
                // Масштабируем изображение
                BufferedImage resizedImage = resizeImage(originalimage);
                // Создаём машину и добавляем в список
                cars.add(new Car(resizedImage, START_X, START_Y + i * Y_OFFSET, i + 1));
            }
        } catch (IOException e) {
            // В случае ошибки показываем сообщение и завершаем программу
            JOptionPane.showMessageDialog(this, "Ошибка загрузки изображений машин", "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        return cars;
    }
    /*Особенности:
    Ожидает, что изображения лежат в папке 3.2.6_Races/Image/
    Нумерация машин начинается с 1 (car1.png, car2.png и т.д.)
    Располагает машины вертикально с интервалом Y_OFFSET
    Обрабатывает ошибки загрузки изображений*/

    private void startRace() {
        // Создаём таймер с интервалом 15 мс (около 60 FPS)
        timer = new Timer(15, e -> {
            // Для каждой машины
            for (Car car : cars) {
                if (!car.isFinished()) {  // Если ещё не финишировала
                    car.move();           // Двигаем машину
                    // Проверяем, достигла ли финиша (с учётом ширины изображения)
                    if (car.getX() + car.getImage().getWidth() / 2 >= FINISH_LINE) {
                        car.setFinished(true);  // Помечаем как финишировавшую
                        winner = car.getNumber();  // Запоминаем победителя
                        timer.stop();         // Останавливаем гонку
                    }
                }
            }
            repaint();  // Перерисовываем экран
        });
        timer.start();  // Запускаем таймер
    }
    /*Детали реализации:
    Таймер срабатывает каждые 15 мс для плавной анимации (~60 кадров/сек)
    Финиш проверяется по центру машины (getWidth() / 2)
    Как только одна машина достигает финиша, гонка останавливается
    repaint() вызывает перерисовку окна после каждого движения*/

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Важно вызывать метод родителя
        // Используем Graphics2D для более продвинутой графики
        Graphics2D g2d = (Graphics2D) g;
        // Настраиваем сглаживание
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Фон
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // Дорожная разметка
        g2d.setColor(new Color(200, 200, 200));
        for (int i = 0; i < CAR_COUNT + 1; i++) {
            int y = START_Y - 30 + i * Y_OFFSET;
            g2d.drawLine(START_X, y, FINISH_LINE, y);
        }

        // Финишная черта
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                0, new float[]{10, 10}, 0));  // Пунктирная линия
        g2d.setColor(Color.RED);
        g2d.drawLine(FINISH_LINE, 0, FINISH_LINE, HEIGHT);

        // Рисуем машины
        for (Car car : cars) {
            g2d.drawImage(car.getImage(), car.getX(), car.getY(), this);
        }

        // Если идёт обратный отсчёт - показываем цифру
        if (countdown > 0) {
            g2d.setFont(new Font("Arial", Font.BOLD, 72));
            g2d.setColor(Color.RED);
            String countdownText = Integer.toString(countdown);
            int textWidth = g2d.getFontMetrics().stringWidth(countdownText);
            g2d.drawString(countdownText, (WIDTH - textWidth)/2, HEIGHT/2);
        }
        // Если есть победитель - показываем сообщение
        if (winner != -1) {
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.setColor(Color.YELLOW);
            String text = "Победитель: Машина " + winner;
            int textWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawString(text, (WIDTH - textWidth)/2, HEIGHT/2);
        }
    }
    /*Ключевые моменты:
    super.paintComponent(g) обязателен для правильной работы Swing
    Преобразование в Graphics2D даёт доступ к дополнительным функциям
    Сглаживание делает графику более качественной
    Разметка рисуется между и вокруг машин
    Пунктирная линия создаётся с помощью BasicStroke с паттерном
    Сообщения центрируются по вычисленной ширине текста*/

    public static void main(String[] args) {
        // Запускаем в потоке обработки событий Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Гонки машин");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // Закрытие окна завершает программу
            frame.add(new Races()); // Добавляем наш компонент
            frame.pack();           // Устанавливаем оптимальный размер
            frame.setLocationRelativeTo(null);  // Центрируем окно
            frame.setVisible(true);             // Делаем видимым
        });
    }
    /*Особенности:
    SwingUtilities.invokeLater() гарантирует правильный запуск в потоке Swing
    pack() автоматически подбирает размер окна под предпочтительный размер содержимого
    setLocationRelativeTo(null) центрирует окно на экране*/
}