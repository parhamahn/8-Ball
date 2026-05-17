import view.MenuPanel;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("8-Ball");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            MenuPanel menuPanel = new MenuPanel();
            frame.add(menuPanel);

            frame.setVisible(true);
        });
    }
}
