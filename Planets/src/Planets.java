import javax.swing.*;
import java.awt.*;

public class Planets {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Солнечная система");
        CanvasPlanets cp = new CanvasPlanets(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(cp);
        frame.setVisible(true);
    }
}
