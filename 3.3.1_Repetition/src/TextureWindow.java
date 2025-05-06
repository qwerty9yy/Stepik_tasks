import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TextureWindow extends JPanel {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int TEXTURE_SIZE = 50;

    // Текстура травы (будет загружена один раз)
    private static BufferedImage grassTexture;

    static {
        // Блок статической инициализации - выполняется один раз при загрузке класса
        try {
            // URL текстуры травы (можно заменить на локальный путь)
            URL textureUrl = new URL("https://i.pinimg.com/originals/7c/b5/1f/7cb51ff2195ea17b6a321d9226626785.jpg"); // Замените на реальный URL
            grassTexture = javax.imageio.ImageIO.read(textureUrl);

            // Масштабируем текстуру до нужного размера
            grassTexture = scaleImage(grassTexture, TEXTURE_SIZE, TEXTURE_SIZE);
        } catch (IOException e) {
            System.err.println("Ошибка загрузки текстуры: " + e.getMessage());
            // Создаем простую текстуру, если загрузка не удалась
            grassTexture = createFallbackTexture();
        }
    }

    public TextureWindow() {
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (grassTexture != null) {
            // Рисуем текстуру, заполняя все окно
            for (int y = 0; y < WINDOW_HEIGHT; y += TEXTURE_SIZE) {
                for (int x = 0; x < WINDOW_WIDTH; x += TEXTURE_SIZE) {
                    g.drawImage(grassTexture, x, y, this);
                }
            }
        }
    }

    // Метод для масштабирования изображения
    private static BufferedImage scaleImage(BufferedImage original, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return scaled;
    }

    // Создание простой текстуры, если загрузка не удалась
    private static BufferedImage createFallbackTexture() {
        BufferedImage texture = new BufferedImage(TEXTURE_SIZE, TEXTURE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = texture.createGraphics();

        // Зеленый фон
        g2d.setColor(new Color(34, 139, 34)); // Цвет травы
        g2d.fillRect(0, 0, TEXTURE_SIZE, TEXTURE_SIZE);

        // Добавляем некоторые детали
        g2d.setColor(new Color(50, 205, 50));
        for (int i = 0; i < 10; i++) {
            int x1 = (int)(Math.random() * TEXTURE_SIZE);
            int y1 = (int)(Math.random() * TEXTURE_SIZE);
            int x2 = (int)(Math.random() * TEXTURE_SIZE);
            int y2 = (int)(Math.random() * TEXTURE_SIZE);
            g2d.drawLine(x1, y1, x2, y2);
        }

        g2d.dispose();
        return texture;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Окно с текстурой травы");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new TextureWindow());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}