import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SolarSystemWithImages extends JPanel {

    // Размеры окна
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final int SUN_RADIUS = 70;

    // Таймер для анимации
    private Timer timer;

    // Список планет
    private List<Planet> planets = new ArrayList<>();

    // Изображения
    private BufferedImage sunImage;
    private BufferedImage backgroundImage;

    class Planet {
        String name;            // Название планеты
        String imagePath;       // Путь к изображению
        double orbitRadius;     // Радиус орбиты (в астрономических единицах, масштабировано)
        double orbitSpeed;      // Скорость обращения (радиан/тик)
        double angle;           // Текущий угол положения на орбите
        int displaySize;        // Размер для отображения
        BufferedImage image;    // Загруженное изображение

        // Конструктор планеты
        Planet(String name, String imagePath, double orbitRadius, double orbitPeriod, int displaySize) {
            this.name = name;
            this.imagePath = imagePath;
            this.orbitRadius = orbitRadius * 60; // Масштабирование для визуализации
            this.orbitSpeed = 0.02 / orbitPeriod;
            this.angle = Math.random() * 2 * Math.PI;
            this.displaySize = displaySize;
            this.image = loadPlanetImage(imagePath, displaySize);
        }

        // Обновление положения планеты
        void update() {
            angle += orbitSpeed;
        }

        // Получение текущих координат планеты
        Point getPosition() {
            int centerX = WIDTH / 2;
            int centerY = HEIGHT / 2;
            int x = (int)(centerX + orbitRadius * Math.cos(angle));
            int y = (int)(centerY + orbitRadius * Math.sin(angle));
            return new Point(x, y);
        }
    }

    public SolarSystemWithImages() {
        // Загрузка фонового изображения (по желанию)
        try {
            backgroundImage = ImageIO.read(new File(""));
        } catch (Exception e) {
            backgroundImage = null; // Если не удалось - будет черный фон
        }

        // Загрузка изображения Солнца
        try {
            sunImage = ImageIO.read(new File("Planets/Image/sun.png"));
            sunImage = resizeImage(sunImage, SUN_RADIUS * 2, SUN_RADIUS * 2);
        } catch (Exception e) {
            sunImage = createDefaultImage(SUN_RADIUS, Color.YELLOW);
        }

        // Создание планет с указанием путей к изображениям
        planets.add(new Planet("Меркурий", "Planets/Image/merkuriy.png", 1.6, 0.24, 20));
        planets.add(new Planet("Венера", "Planets/Image/venera.png", 2, 0.62, 30));
        planets.add(new Planet("Земля", "Planets/Image/earth.png", 2.5, 1.0, 30));
        planets.add(new Planet("Марс", "Planets/Image/mars.png", 3, 1.88, 25));
        planets.add(new Planet("Юпитер", "Planets/Image/youpiter.png", 3.8, 11.86, 60));
        planets.add(new Planet("Сатурн", "Planets/Image/saturn.png", 4.6, 29.46, 55));
        planets.add(new Planet("Уран", "Planets/Image/uran.png", 6, 84.01, 40));
        planets.add(new Planet("Нептун", "Planets/Image/neptun.png", 7.5, 164.8, 40));

        // Настройка таймера для анимации
        timer = new Timer(20, e -> {
            planets.forEach(Planet::update);
            repaint();
        });
        timer.start();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    private BufferedImage loadPlanetImage(String path, int size) {
        try {
            BufferedImage original = ImageIO.read(new File(path));
            return resizeImage(original, size, size);
        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение: " + path);
            return createDefaultImage(size/2, Color.GRAY);
        }
    }

    private BufferedImage resizeImage(BufferedImage original, int width, int height) {
        if (original == null) return null;

        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }

    private BufferedImage createDefaultImage(int r, Color color) {
        BufferedImage img = new BufferedImage(r * 2, r * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.fillOval(0, 0, r * 2, r * 2);
        g2d.dispose();
        return img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Рисуем фон
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
        }

        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        // Рисуем Солнце
        int sunX = centerX - sunImage.getWidth() / 2;
        int sunY = centerY - sunImage.getHeight() / 2;
        g2d.drawImage(sunImage, sunX, sunY, null);

        // Рисуем орбиты и планеты
        for (Planet planet : planets) {
            // Орбита
            g2d.setColor(new Color(255, 255, 255, 50));
            int orbitDiameter = (int)(planet.orbitRadius * 2);
            g2d.drawOval(centerX - (int)planet.orbitRadius, centerY - (int)planet.orbitRadius,
                    orbitDiameter, orbitDiameter);

            // Планета
            Point pos = planet.getPosition();
            g2d.drawImage(planet.image, pos.x - planet.displaySize/2, pos.y - planet.displaySize/2, null);

            // Название планеты
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString(planet.name, pos.x + planet.displaySize/2, pos.y);
        }

        // Отображаем подсказку
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Солнечная система - симуляция планет", 20, 20);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Солнечная система с изображениями планет");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new SolarSystemWithImages());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}