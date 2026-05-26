package game;

import view.*;
import controller.*;
import model.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Player player1;
    private Player player2;
    private Settings settings;
    private GameState gameState;
    private Table table;
    private Physics physics;

    private MenuController menuController;
    private SettingsController settingsController;
    private GameController gameController;
    private GamePanel gamePanel;
    private Rule rule;

    public static void main(String[] args) {
        new Main().createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("8-Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        player1   = new Player("Player 1");
        player2   = new Player("Player 2");
        settings  = new Settings();
        table     = new Table();
        gameState = new GameState(player1, player2, table);
        rule = new Rule(gameState, table);
        physics   = new Physics(table, gameState, rule);

        MenuPanel     menuPanel     = new MenuPanel();
        SettingsPanel settingsPanel = new SettingsPanel();
        GamePlayController gamePlayController = new GamePlayController(table, physics, gameState, gamePanel);
        gamePanel = new GamePanel(table, physics, gamePlayController, gameState, rule);

        menuController     = new MenuController(menuPanel, this);
        settingsController = new SettingsController(settingsPanel, this, player1, player2, settings);
        gameController     = new GameController(gamePanel, this);

        gamePanel.addMouseListener(gamePlayController);
        gamePanel.addMouseMotionListener(gamePlayController);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.add(menuPanel,     "menu");
        mainPanel.add(settingsPanel, "settings");
        mainPanel.add(gamePanel,     "game");

        frame.add(mainPanel);
        frame.setVisible(true);
        showMenu();
    }

    public void showMenu() {
        gamePanel.stopLoop();
        cardLayout.show(mainPanel, "menu");
    }

    public void showSettings() {
        cardLayout.show(mainPanel, "settings");
    }

    public void showGame() {
        table.resetTable();
        gameState.resetPocketSelection();
        rule.resetBreak();
        gameState.setMoving(false);
        cardLayout.show(mainPanel, "game");
        gamePanel.startLoop();
    }
}