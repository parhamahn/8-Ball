package controller;
import game.Main;
import view.MenuPanel;

public class MenuController {
    private MenuPanel view;
    private Main mainApp;

    public MenuController(MenuPanel view, Main mainApp) {
        this.view = view;
        this.mainApp = mainApp;
        attachListeners();
    }

    private void attachListeners() {
        view.getSettingsButton().addActionListener(e -> mainApp.showSettings());
        view.getStartButton().addActionListener(e -> mainApp.showGame());
        view.getRecordsButton().addActionListener(e -> mainApp.showRecords());
    }
}