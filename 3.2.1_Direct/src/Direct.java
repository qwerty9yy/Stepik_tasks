import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Direct extends JPanel implements ActionListener {
    private Image image;
    private int x = 0;
    private int y = 0;
    private Timer timer;
    private int speed = 2;

    public Direct(){
        try
        {
            String imageURL = "https://avatars.mds.yandex.net/i?id=7c04df84d6f4ea3e839938c6d076b0c7_sr-8244093-images-thumbs&n=13";
            ImageIcon imageIcon = new ImageIcon(new URL(imageURL));
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
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        if(image != null) {
            g.drawImage(image, x, y, this);
        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        x += speed;
        y += speed;
        if(x >= getWidth() || y >= getHeight()) {
            x = -100;
            y = -100;
        }
        repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Секретик");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Direct());
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}