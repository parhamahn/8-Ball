package controller;

import game.Main;
import view.GamePanel;

public class GameController {
    private GamePanel view;
    private Main mainApp;

    public GameController(GamePanel view, Main mainApp){
        this.view = view;
        this.mainApp = mainApp;
        attachListeners();
    }

    public void attachListeners(){
        view.getExitButton().addActionListener(e -> mainApp.showMenu());
    }
}
