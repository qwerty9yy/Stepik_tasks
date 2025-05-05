import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CarDisplay extends JPanel {
    private List<CarInfo> cars = new ArrayList<>();

    // Внутренний класс для хранения информации о машине и ее масштабе
    class CarInfo {
        ImageIcon icon;
        double scale;

        public CarInfo(String path, double scale) {
            this.icon = new ImageIcon(path);
            this.scale = scale;
        }
    }

    public CarDisplay() {
        // Добавляем машины с разными масштабами
        cars.add(new CarInfo("3.2.6_Races/Image/1.png", 0.03));
        cars.add(new CarInfo("3.2.6_Races/Image/2.png", 0.01));
        cars.add(new CarInfo("3.2.6_Races/Image/3.png", 0.01));
        cars.add(new CarInfo("3.2.6_Races/Image/4.png", 0.01));
        cars.add(new CarInfo("3.2.6_Races/Image/5.png", 0.01));

        // Проверка загрузки изображений
        for (CarInfo car : cars) {
            if (car.icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                System.err.println("Ошибка загрузки изображения: " + car.icon);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelHeight = getHeight();
        int panelWidth = getWidth();
        int yBaseLine = panelHeight - 50; // Базовый уровень для машин (50px от нижнего края)

        // Рассчитываем позиции для равномерного распределения
        int totalWidth = 0;
        int[] carWidths = new int[cars.size()];

        // Сначала вычисляем ширину всех машин
        for (int i = 0; i < cars.size(); i++) {
            CarInfo car = cars.get(i);
            carWidths[i] = (int)(car.icon.getIconWidth() * car.scale);
            totalWidth += carWidths[i];
        }

        // Рассчитываем отступы между машинами
        int spacing = (panelWidth - totalWidth) / (cars.size() + 1);
        int xPosition = spacing;

        // Рисуем каждую машину
        for (int i = 0; i < cars.size(); i++) {
            CarInfo car = cars.get(i);
            Image image = car.icon.getImage();

            int carWidth = (int)(car.icon.getIconWidth() * car.scale);
            int carHeight = (int)(car.icon.getIconHeight() * car.scale);

            // Вычисляем Y-позицию так, чтобы все машины стояли на одной линии
            int yPosition = yBaseLine - carHeight;

            g.drawImage(image, xPosition, yPosition, carWidth, carHeight, this);
            xPosition += carWidth + spacing;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Машины с разными масштабами");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 800);
            frame.add(new CarDisplay());
            frame.setVisible(true);
        });
    }
}