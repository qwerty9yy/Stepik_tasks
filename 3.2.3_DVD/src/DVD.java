import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class DVD extends JPanel implements ActionListener {
    private Timer timer;
    private Image image;
    private int x = 0;
    private int y = 0;
    private int xSpeed = 2;
    private int ySpeed = 2;
    private Color color = Color.WHITE;

    public DVD(){
        try
        {
            String imageURl = "https://avatars.mds.yandex.net/i?id=e6a13077ba69091a497c1135943b88db_l-4476821-images-thumbs&n=13";
            ImageIcon imageIcon = new ImageIcon(new URL(imageURl));
            image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            image = null;
            e.printStackTrace();
        }
        timer = new Timer(16, this);
        timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.SrcAtop);
            g2d.setColor(color);
            g2d.fillRect(x, y, 50, 50);
            g2d.drawImage(image, x, y, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, 50, 50);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
            x += xSpeed;
            y += ySpeed;

            if (x <= 0 || x + 50 >= getWidth()) {
                xSpeed = -xSpeed;
                changeColor();
            }
            if (y <= 0 || y + 50 >= getHeight()) {
                ySpeed = -ySpeed;
                changeColor();
            }
            repaint();
    }
    public void changeColor() {
        float hue = (float) Math.random();
        color = Color.getHSBColor(hue, 0.8f, 1.0f);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DVD");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new DVD());
            frame.setSize(500, 500);
            frame.setVisible(true);
        });
    }
}
