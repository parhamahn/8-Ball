package model;

import java.util.ArrayList;

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
            if (table.willPocket(ball)){
                ball.setPocketed(true);
                ball.setVy(0);
                ball.setVx(0);
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
