import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Repetition extends JPanel {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int TEXTURE_SIZE = 50;

    private Image grassTexture;

    public Repetition() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        try {
            ImageIcon icon = new ImageIcon(new URL("https://i.pinimg.com/originals/7c/b5/1f/7cb51ff2195ea17b6a321d9226626785.jpg"));
            grassTexture = icon.getImage().getScaledInstance(TEXTURE_SIZE, TEXTURE_SIZE, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            e.printStackTrace();
            grassTexture = createFallbackTexture();
        }
    }

    private Image createFallbackTexture() {
        BufferedImage texture = new BufferedImage(TEXTURE_SIZE, TEXTURE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = texture.createGraphics();
        g.setColor(new Color(100, 200, 100)); // Зелёный цвет
        g.fillRect(0, 0, TEXTURE_SIZE, TEXTURE_SIZE);
        g.dispose();
        return texture;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grassTexture != null) {
            // Заполняем всё окно повторяющейся текстурой
            for (int y = 0; y < HEIGHT; y += TEXTURE_SIZE) {
                for (int x = 0; x < WIDTH; x += TEXTURE_SIZE) {
                    g.drawImage(grassTexture, x, y, this);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->{
            JFrame frame = new JFrame("Трава");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Repetition());
            frame.pack();
            frame.setVisible(true);
        });
    }
}
