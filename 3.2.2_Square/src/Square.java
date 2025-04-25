import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Square extends JPanel implements ActionListener {
    private Timer timer;
    private Image image;
    private int x = 0;
    private int y = 0;
    private int speed = 2;
    private int direc = 0;

    public Square() {
        try
        {
            String imageURL = "https://avatars.mds.yandex.net/i?id=c4d2e43724a3c7f322b2f57b2e7b0e44d37949f8-5650733-images-thumbs&n=13";
            ImageIcon imageIcon = new ImageIcon(new URL(imageURL));
            image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

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
        switch (direc) {
            case 0:
                x += speed;
                if (x + 50 >= getWidth()) {
                    x = getWidth() - 50;
                    direc = 1;
                }
                break;
            case 1:
                y += speed;
                if (y + 50 >= getHeight()) {
                    y = getHeight() - 50;
                    direc = 2;
                }
                break;
            case 2:
                x -= speed;
                if (x <= 0) {
                    x = 0;
                    direc = 3;
                }
                break;
            case 3:
                y -= speed;
                if (y <= 0) {
                    y = 0;
                    direc = 0;
                }
        }
        repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(":)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Square());
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}
