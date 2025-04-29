import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class Earth extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int ORBIT_RADIUS = 150;

    private Timer timer;
    private double speed = 0;
    private BufferedImage earthImage;
    private BufferedImage sunImage;

    public Earth() {
        try {
            // Загрузка оригинальных изображений
            BufferedImage originalEarth = ImageIO.read(new File("C:\\Users\\Lite\\IdeaProjects\\Stepik_tasks\\3.2.4_Earth\\src\\E.png"));
            BufferedImage originalSun = ImageIO.read(new File("C:\\Users\\Lite\\IdeaProjects\\Stepik_tasks\\3.2.4_Earth\\src\\S.png"));

            // Уменьшение изображений в 2 раза
            earthImage = resizeImage(originalEarth, 0.03);
            sunImage = resizeImage(originalSun, 0.15);

        } catch (Exception e) {
            e.printStackTrace();
            // Создаем изображения по умолчанию уже уменьшенного размера
            earthImage = createDefaultImage(30, Color.BLUE);
            sunImage = createDefaultImage(50, Color.YELLOW);
        }

        timer = new Timer(15, e -> {
            speed += 0.02;
            repaint();
        });
        timer.start();
    }

    // Метод для масштабирования изображения с сохранением прозрачности
    private BufferedImage resizeImage(BufferedImage original, double scale) {
        if (original == null) return null;

        int newWidth = (int)(original.getWidth() * scale);
        int newHeight = (int)(original.getHeight() * scale);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();

        // Настройки для лучшего качества уменьшения
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(original, 0, 0, newWidth, newHeight, null);
        g2d.dispose();

        return resized;
    }

    private BufferedImage createDefaultImage(int r, Color color) {
        BufferedImage img = new BufferedImage(r * 2, r * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(color);
        g2d.fillOval(0, 0, r * 2, r * 2);
        g2d.dispose();
        return img;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        int centerX = WIDTH / 2;
        int centerY = HEIGHT / 2;

        int sunX = centerX - sunImage.getWidth() / 2;
        int sunY = centerY - sunImage.getHeight() / 2;
        g.drawImage(sunImage, sunX, sunY, null);

        int earthX = (int)(centerX + ORBIT_RADIUS * Math.cos(speed)) - earthImage.getWidth() / 2;
        int earthY = (int)(centerY + ORBIT_RADIUS * Math.sin(speed)) - earthImage.getHeight() / 2;

        g.drawImage(earthImage, earthX, earthY, null);

        // Опционально: рисуем орбиту
        g.setColor(new Color(255, 255, 255, 100));
        g.drawOval(centerX - ORBIT_RADIUS, centerY - ORBIT_RADIUS,
                ORBIT_RADIUS * 2, ORBIT_RADIUS * 2);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Солнце подкатывает к земле, или наоборот?");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Earth());
            frame.setSize(WIDTH, HEIGHT);
            frame.setVisible(true);
        });
    }
}