package controller;

import view.SettingsPanel;
import model.Player;
import model.Settings;
import javax.swing.*;
import game.Main;

public class SettingsController {
    private SettingsPanel view;
    private Main mainApp;
    private Player player1;
    private Player player2;
    private Settings settings;

    public SettingsController(SettingsPanel view, Main mainApp,
                              Player player1, Player player2, Settings settings) {
        this.view = view;
        this.mainApp = mainApp;
        this.player1 = player1;
        this.player2 = player2;
        this.settings = settings;
        attachListeners();
        loadCurrentSettingsIntoView();
    }

    private void attachListeners() {
        view.getSaveButton().addActionListener(e -> saveSettings());
        view.getBackButton().addActionListener(e -> mainApp.showMenu());
    }

    private void loadCurrentSettingsIntoView() {
        view.setPlayer1Name(player1.getName());
        view.setPlayer2Name(player2.getName());
        view.setSelectedCueType(settings.getCueType());
        view.setSoundEnabled(settings.isSoundEnabled());
    }

    private void saveSettings() {
        player1.setName(view.getPlayer1Name());
        player2.setName(view.getPlayer2Name());
        settings.setCueType(view.getSelectedCueType());
        settings.setSoundEnabled(view.isSoundEffectsEnabled());

        JOptionPane.showMessageDialog(view, "Settings saved");
        mainApp.showMenu();
    }
}