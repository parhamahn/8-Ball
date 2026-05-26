package controller;

import model.*;
import view.GamePanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class GamePlayController extends MouseAdapter {
    private Table table;
    private Physics physics;
    private GameState gameState;
    private GamePanel gamePanel;

    private Point2D dragStart;
    private Point2D dragCurrent;

    public GamePlayController(Table table, Physics physics, GameState gameState, GamePanel gamePanel) {
        this.table = table;
        this.physics = physics;
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !gameState.isMoving()) {

            if (!gameState.isPocketChosen()) {
                Pocket pocket = findPocketAt(e.getX(), e.getY());
                if (pocket != null) {
                    gameState.setChosenPocket(pocket);
                    if (gamePanel != null){
                        gamePanel.repaint();
                    }
                    return;
                }
            }
            if(gameState.isPocketChosen()) {
                double distanceToBall = Point2D.distance(
                        e.getX(), e.getY(),
                        this.table.getBall(0).getX(), this.table.getBall(0).getY()
                );
                if (distanceToBall < this.table.getBall(0).getRadius() * 2) {
                    dragStart = new Point2D.Double(e.getX(), e.getY());
                    dragCurrent = null;
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragStart != null && !gameState.isMoving()) {
            dragCurrent = new Point2D.Double(e.getX(), e.getY());
            if (gamePanel != null) {
                gamePanel.repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && dragStart != null && !gameState.isMoving() && gameState.isPocketChosen()) {
            if (dragCurrent != null) {
                double dx = dragStart.getX() - dragCurrent.getX();
                double dy = dragStart.getY() - dragCurrent.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 0) {
                    double dirX = dx / distance;
                    double dirY = dy / distance;

                    double power = Math.min(distance / 10.0, 20.0);

                    java.util.ArrayList<Double> direction = new java.util.ArrayList<>();
                    direction.add(dirX);
                    direction.add(dirY);

                    physics.hit(table, power, direction);
                }
            }

            dragStart = null;
            dragCurrent = null;
            if (gamePanel != null) {
                gamePanel.repaint();
            }
        }
    }

    private Pocket findPocketAt(int x, int y) {
        for (Pocket p : table.getPockets()) {
            double dist = Point2D.distance(x, y, p.getCenterX(), p.getCenterY());
            if (dist < p.getRadius() * 1.5) {
                return p;
            }
        }
        return null;
    }

    public boolean isDragging() {
        return dragStart != null && dragCurrent != null;
    }

    public Point2D getDragStart() {
        return dragStart;
    }

    public Point2D getDragCurrent() {
        return dragCurrent;
    }
}