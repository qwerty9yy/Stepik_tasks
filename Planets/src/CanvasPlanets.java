import javax.swing.*;

public class CanvasPlanets extends JPanel {
    private int width;
    private int heigth;
    private int ORBIT_RADIUS;

    private Timer timer;
    private double speed;



    public CanvasPlanets(int width, int heigth) {
        this.width = width;
        this.heigth = heigth;
    }
}
