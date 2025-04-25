import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Direct extends JPanel implements ActionListener {
    private Timer timer;
    private Image image;
    private int x = 0;
    private int y = 0;
    private int speed = 2;

    public Direct() {
        try
        {
            ImageIcon imageIcon = new ImageIcon(new URL("https://avatars.mds.yandex.net/i?id=292f631bae0be49b7b81c97c94ae50ef_l-5235303-images-thumbs&n=13"));
            image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);

            timer = new Timer(30, this);
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (image != null) {
            g.drawImage(image, x, y, this);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        x += speed;
        y += speed;

        if (x >= getWidth() || y >= getHeight()) {
            x = -150;
            y = -150;
        }
        repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("RAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Direct());
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}