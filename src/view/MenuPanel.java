package view;
import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton startButton = new JButton("Start");
        JButton settingsButton = new JButton("Settings");
        JButton recordsButton = new JButton("Records");
        JButton exitButton = new JButton("Exit");

        Dimension buttonSize = new Dimension(200, 50);
        startButton.setPreferredSize(buttonSize);
        settingsButton.setPreferredSize(buttonSize);
        recordsButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        add(startButton, gbc);
        add(settingsButton, gbc);
        add(recordsButton, gbc);
        add(exitButton, gbc);
    }
}
