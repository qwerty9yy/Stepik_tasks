import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;

public class DVD extends JPanel implements ActionListener {
    private Timer timer;
    private Image image;
    private int x = 0;
    private int y = 0;
    private int speed;

    public DVD(){
        try
        {
            String imageURl = "https://avatars.mds.yandex.net/i?id=e6a13077ba69091a497c1135943b88db_l-4476821-images-thumbs&n=13";
            ImageIcon imageIcon = new ImageIcon(new URL(imageURl));
            image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            timer = new Timer(30, this);
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override

}
