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
    private RecordsController recordsController;
    private RecordsPanel recordsPanel;
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
        frame.setResizable(false);

        player1   = new Player("Player 1");
        player2   = new Player("Player 2");
        settings  = new Settings();
        table     = new Table();
        gameState = new GameState(player1, player2, table);
        rule = new Rule(gameState, table);
        physics   = new Physics(table, gameState, rule);

        MenuPanel     menuPanel     = new MenuPanel();
        SettingsPanel settingsPanel = new SettingsPanel();
        recordsPanel = new RecordsPanel();
        GamePlayController gamePlayController = new GamePlayController(table, physics, gameState, gamePanel);
        gamePanel = new GamePanel(table, physics, gamePlayController, gameState, rule, this);

        recordsController = new RecordsController();
        menuController     = new MenuController(menuPanel, this);
        settingsController = new SettingsController(settingsPanel, this, player1, player2, settings);
        gameController     = new GameController(gamePanel, this, table, gameState);

        gamePanel.addMouseListener(gamePlayController);
        gamePanel.addMouseMotionListener(gamePlayController);

        recordsPanel.getBackButton().addActionListener(e -> showMenu());

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.add(menuPanel,     "menu");
        mainPanel.add(settingsPanel, "settings");
        mainPanel.add(recordsPanel,  "records");
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
        gameState.resetGameState();
        rule.resetBreak();
        gameState.resetTimer();
        cardLayout.show(mainPanel, "game");
        gamePanel.startLoop();
    }

    public void showRecords() {
        recordsPanel.displayRecords(recordsController.loadFromFile());
        cardLayout.show(mainPanel, "records");
    }

    public void showWin(Player winner){
        JOptionPane.showMessageDialog(gamePanel, winner.getName()+" Won the game");
        Player loser = (winner == player1) ? player2 : player1;
        java.util.List<model.Record> records = recordsController.loadFromFile();
        records.add(new model.Record(winner, loser));
        recordsController.saveIntoFile(records);
    }
}