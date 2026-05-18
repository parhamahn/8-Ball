package view;

import model.Pocket;
import model.Table;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private JButton exitButton;
    private Table table;

    public GamePanel(Table table){
        this.table = table;
        setBackground(new Color(30, 30, 40));
        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(100,40));
        add(exitButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(new Color(0, 100, 0));  // dark green
        g2d.fillRect((int) table.getStartX(), (int) table.getStartY(), getWidth(), getHeight());

        g2d.setColor(Color.BLACK);
        for (Pocket pocket : table.getPockets()) {
            int x = (int)(pocket.getCenterX() - pocket.getRadius());
            int y = (int)(pocket.getCenterY() - pocket.getRadius());
            int diameter = (int)(pocket.getRadius() * 2);
            g2d.fillOval(x, y, diameter, diameter);
        }

    }
    public JButton getExitButton() {
        return exitButton;
    }
}