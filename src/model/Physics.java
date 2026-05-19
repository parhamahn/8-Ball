package model;

import java.util.ArrayList;

public class Physics {
    private Table table;

    public Physics(Table table){
        this.table = table;
    }

    public void updatePhysics(double dt){
        for (Ball ball: table.getBalls()){
            ball.updatePosition(dt);
            ball.applyFriction(Ball.DEFAULT_FRICTION);
            if (table.willPocket(ball)){
                ball.setPocketed(true);
            }
        }
    }

    public void hit(Table table, double power, ArrayList<Double> direction){
        Ball cueBall = table.getBall(0);
        cueBall.setVx(power* direction.get(0));
        cueBall.setVy(power* direction.get(1));
    }
}
