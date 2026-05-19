package controller;

import model.Ball;
import model.GameState;
import model.Physics;
import model.Table;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class GamePlayController extends MouseAdapter {
    private Table table;
    private Physics physics;
    private GameState gameState;

    private Point2D dragStart;

    public GamePlayController(Table table, Physics physics, GameState gameState) {
        this.table = table;
        this.physics = physics;
        this.gameState = gameState;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && !gameState.isMoving()) {
            double distanceToBall = Point2D.distance(
                    e.getX(), e.getY(),
                    this.table.getBall(0).getX(), this.table.getBall(0).getY()
            );
            if (distanceToBall < this.table.getBall(0).getRadius()*2) {
                dragStart = new Point2D.Double(e.getX(), e.getY());
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && dragStart != null) {
            double dx = dragStart.getX() - e.getX();
            double dy = dragStart.getY() - e.getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                // Normalize direction
                double dirX = dx / distance;
                double dirY = dy / distance;

                double power = Math.min(distance / 10.0, 20.0);

                java.util.ArrayList<Double> direction = new java.util.ArrayList<>();
                direction.add(dirX);
                direction.add(dirY);

                physics.hit(table, power, direction);
            }

            dragStart = null;
        }
    }
}
