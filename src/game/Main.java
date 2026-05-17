package game;// Main.java
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

    private MenuController menuController;
    private SettingsController settingsController;

    public static void main(String[] args) {
        new Main().createAndShowGUI();
    }

    private void createAndShowGUI() {
        frame = new JFrame("8-Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        player1 = new Player("Player 1");
        player2 = new Player("Player 2");
        settings = new Settings();
        gameState = new GameState();

        MenuPanel menuPanel = new MenuPanel();
        SettingsPanel settingsPanel = new SettingsPanel();

        menuController = new MenuController(menuPanel, this);
        settingsController = new SettingsController(settingsPanel, this, player1, player2, settings);


        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.add(menuPanel, "menu");
        mainPanel.add(settingsPanel, "settings");

        frame.add(mainPanel);
        frame.setVisible(true);
        showMenu();
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "menu");
    }

    public void showSettings() {
        cardLayout.show(mainPanel, "settings");
    }
}