package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Physics {
    private Table table;
    private GameState gameState;

    public Physics(Table table, GameState gameState){
        this.table = table;
        this.gameState = gameState;
    }

    public void updatePhysics(double dt){
        gameState.setMoving(false);
        for (Ball ball: table.getBalls()){
            ball.updatePosition(dt);
            ball.applyFriction(Ball.DEFAULT_FRICTION);


            // Checking if ball gets in a pocket
            if (table.willPocket(ball)){
                ball.setPocketed(true);
                ball.setVy(0);
                ball.setVx(0);
            }

            // Checking collisions
            if(!(ball.getVx()==0 && ball.getVy()==0)){

                // for balls
                for (Ball other : table.getBalls()) {
                    if (ball == other || other.isPocketed()) continue;

                    double dx = ball.getX() - other.getX();
                    double dy = ball.getY() - other.getY();
                    double distance = Math.hypot(dx, dy);

                    if (distance > 0 && distance <= 2 * Ball.DEFAULT_RADIUS) {
                        double nx = dx / distance;
                        double ny = dy / distance;

                        double overlap = 2 * Ball.DEFAULT_RADIUS - distance;
                        double separation = overlap / 2.0;

                        ball.setX(ball.getX() + nx * separation);
                        ball.setY(ball.getY() + ny * separation);
                        other.setX(other.getX() - nx * separation);
                        other.setY(other.getY() - ny * separation);

                        double rvx = ball.getVx() - other.getVx();
                        double rvy = ball.getVy() - other.getVy();
                        double velAlongNormal = rvx * nx + rvy * ny;

                        if (velAlongNormal > 0) continue;

                        double impulse = -velAlongNormal;

                        ball.setVx(ball.getVx() + impulse * nx);
                        ball.setVy(ball.getVy() + impulse * ny);
                        other.setVx(other.getVx() - impulse * nx);
                        other.setVy(other.getVy() - impulse * ny);
                    }
                }
            }

        }
        for (Ball ball: table.getBalls()){
            if (!(ball.getVy()==0.0 && ball.getVx()==0.0)){
                gameState.setMoving(true);
                break;
            }
        }
    }

    public void hit(Table table, double power, ArrayList<Double> direction){
        Ball cueBall = table.getBall(0);
        cueBall.setVx(power* direction.get(0));
        cueBall.setVy(power* direction.get(1));
    }
}
