package view;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    private JTextField player1NameField;
    private JTextField player2NameField;
    private JComboBox<String> cueTypeComboBox;
    private JCheckBox soundEffectsCheckBox;
    private JButton saveButton;
    private JButton backButton;

    public SettingsPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel player1Label = new JLabel("Player 1 Name:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(player1Label, gbc);

        player1NameField = new JTextField("Player 1", 20);
        gbc.gridx = 1;
        add(player1NameField, gbc);

        JLabel player2Label = new JLabel("Player 2 Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(player2Label, gbc);

        player2NameField = new JTextField("Player 2", 20);
        gbc.gridx = 1;
        add(player2NameField, gbc);

        JLabel cueLabel = new JLabel("Cue Type:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(cueLabel, gbc);

        cueTypeComboBox = new JComboBox<>(new String[]{"Classic Wooden Cue", "Modern Carbon Fiber Cue"});
        gbc.gridx = 1;
        add(cueTypeComboBox, gbc);

        JLabel soundLabel = new JLabel("Sound Effects:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(soundLabel, gbc);

        soundEffectsCheckBox = new JCheckBox("Enable", true);
        gbc.gridx = 1;
        add(soundEffectsCheckBox, gbc);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        saveButton = new JButton("Save");
        buttonsPanel.add(saveButton);

        backButton = new JButton("Back");
        buttonsPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        add(buttonsPanel, gbc);
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public String getPlayer1Name() {
        return player1NameField.getText().trim();
    }

    public String getPlayer2Name() {
        return player2NameField.getText().trim();
    }

    public String getSelectedCueType() {
        return (String) cueTypeComboBox.getSelectedItem();
    }

    public void setPlayer1Name(String name) {
        player1NameField.setText(name);
    }

    public void setPlayer2Name(String name) {
        player2NameField.setText(name);
    }

    public void setSelectedCueType(String cueType) {
        cueTypeComboBox.setSelectedItem(cueType);
    }

    public void setSoundEnabled(boolean enabled) {
        soundEffectsCheckBox.setSelected(enabled);
    }

    public boolean isSoundEffectsEnabled() {
        return soundEffectsCheckBox.isSelected();
    }
}