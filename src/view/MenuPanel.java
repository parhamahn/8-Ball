package view;
import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private JButton startButton;
    private JButton settingsButton;
    private JButton recordsButton;
    private JButton exitButton;
    private final Dimension buttonSize = new Dimension(200, 50);

    public MenuPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("8-Ball");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        add(titleLabel, gbc);

        startButton = new JButton("Start Game");
        startButton.setPreferredSize(buttonSize);
        add(startButton, gbc);

        settingsButton = new JButton("Settings");
        settingsButton.setPreferredSize(buttonSize);
        add(settingsButton, gbc);

        recordsButton = new JButton("Records");
        recordsButton.setPreferredSize(buttonSize);
        add(recordsButton, gbc);

        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(buttonSize);
        add(exitButton, gbc);
        exitButton.addActionListener(e -> System.exit(0));
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getSettingsButton() {
        return settingsButton;
    }

    public JButton getRecordsButton() {
        return recordsButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
