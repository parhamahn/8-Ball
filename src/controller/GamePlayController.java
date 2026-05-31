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
    private boolean ballInHandDragging = false;
    private Point2D dragStart;
    private Point2D dragCurrent;
    private Point2D dragOffset;

    public GamePlayController(Table table, Physics physics, GameState gameState, GamePanel gamePanel) {
        this.table = table;
        this.physics = physics;
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !gameState.isMoving()) {

            if (gameState.isBallInHand()) {
                Ball cueBall = table.getBall(0);
                if (cueBall != null && !cueBall.isPocketed() &&
                        Point2D.distance(e.getX(), e.getY(), cueBall.getX(), cueBall.getY()) < cueBall.getRadius() * 2) {
                    ballInHandDragging = true;
                    dragOffset = new Point2D.Double(cueBall.getX() - e.getX(), cueBall.getY() - e.getY());
                }
                return;
            }

            Pocket pocket = findPocketAt(e.getX(), e.getY());
            if (pocket != null) {
                gameState.setChosenPocket(pocket);
                if (gamePanel != null) gamePanel.repaint();
                return;
            }


            if (gameState.isPocketChosen()) {
                double distanceToBall = Point2D.distance(
                        e.getX(), e.getY(),
                        table.getBall(0).getX(), table.getBall(0).getY()
                );
                if (distanceToBall < table.getBall(0).getRadius() * 2) {
                    dragStart = new Point2D.Double(e.getX(), e.getY());
                    dragCurrent = null;
                }
            }

            if (gameState.isPocketChosen()) {
                Ball.Spin spin = findSpinAt(e.getX(), e.getY());
                if (spin != null) {
                    gameState.setSelectedSpin(spin);
                    if (gamePanel != null) gamePanel.repaint();
                    return;
                }
            }

        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (ballInHandDragging) {
            Ball cueBall = table.getBall(0);
            if (cueBall != null && dragOffset != null) {
                double newX = e.getX() + dragOffset.getX();
                double newY = e.getY() + dragOffset.getY();

                double r = cueBall.getRadius();
                double headString = table.getStartX() + table.getWidth() / 4.0;
                double minX = table.getStartX() + r + 2*Pocket.getDefaultRadius();
                double maxX = headString - r;
                double minY = table.getStartY() + r + 2*Pocket.getDefaultRadius();
                double maxY = table.getEndY() - r - 2*Pocket.getDefaultRadius();

                newX = Math.min(maxX, Math.max(minX, newX));
                newY = Math.min(maxY, Math.max(minY, newY));

                cueBall.setX(newX);
                cueBall.setY(newY);
                if (gamePanel != null) gamePanel.repaint();
            }
            return;
        }

        if (dragStart != null && !gameState.isMoving()) {
            dragCurrent = new Point2D.Double(e.getX(), e.getY());
            if (gamePanel != null) gamePanel.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;

        if (ballInHandDragging) {
            ballInHandDragging = false;
            dragOffset = null;
            gameState.setBallInHand(false);
            if (gamePanel != null) gamePanel.repaint();
            return;
        }

        if (dragStart != null && !gameState.isMoving() && gameState.isPocketChosen()) {
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
            if (gamePanel != null) gamePanel.repaint();
        }
    }

    private Pocket findPocketAt(int x, int y) {
        for (Pocket p : table.getPockets()) {
            double dist = Point2D.distance(x, y, p.getCenterX(), p.getCenterY());
            if (dist < p.getRadius() * 1.5) return p;
        }
        return null;
    }

    private Ball.Spin findSpinAt(int x, int y) {
        int selectorRadius = 50;
        int selectorX = (int) table.getWidth() / 2;
        int selectorY = 120;
        int innerOffset = (int)(selectorRadius * 0.6);
        int hitRadius = 12;

        int[][] dotCenters = {
                {selectorX,               selectorY},
                {selectorX,               selectorY - innerOffset},
                {selectorX,               selectorY + innerOffset},
                {selectorX - innerOffset, selectorY},
                {selectorX + innerOffset, selectorY}
        };

        Ball.Spin[] types = {Ball.Spin.STOP, Ball.Spin.TOP, Ball.Spin.BACK, Ball.Spin.LEFT, Ball.Spin.RIGHT};

        for (int i = 0; i < dotCenters.length; i++) {
            if (Point2D.distance(x, y, dotCenters[i][0], dotCenters[i][1]) <= hitRadius) {
                return types[i];
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