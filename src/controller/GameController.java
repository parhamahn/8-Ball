package controller;

import game.Main;
import model.Ball;
import model.GameState;
import model.Table;
import view.GamePanel;
import javax.swing.*;

public class GameController {
    private GamePanel view;
    private Main mainApp;
    private Table table;
    private GameState gameState;

    public GameController(GamePanel view, Main mainApp, Table table, GameState gameState){
        this.view = view;
        this.mainApp = mainApp;
        this.table = table;
        this.gameState = gameState;
        attachListeners();
    }

    public void attachListeners(){
        view.getExitButton().addActionListener(e -> mainApp.showMenu());
        view.getDebugButton().addActionListener(e -> {
            for(Ball ball: table.getBalls()){
                if(ball.getNumber() != 0 && ball.getNumber() != 8 && ball.getNumber() != 7 && ball.getNumber() != 9){
                    gameState.setDoneBreak(true);
                    ball.setPocketed(true);
                }
            }
        });
    }
}
