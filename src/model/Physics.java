package model;

import java.util.ArrayList;
import java.util.List;

public class Physics {
    private Table table;
    private GameState gameState;
    private Rule rule;

    public Physics(Table table, GameState gameState, Rule rule){
        this.table = table;
        this.gameState = gameState;
        this.rule = rule;
    }

    public void updatePhysics(double dt){
        gameState.setMoving(false);
        for (Ball ball: table.getBalls()){
            if (ball.isPocketed()){continue;}

            this.checkBallCushionCollision(ball);
            this.updatePosition(ball, dt);
            this.gettingInPocket(ball);
            this.checkBallCushionCollision(ball);
            this.checkBallsCollision(ball);

        }
        checkMoving();

    }

    public void hit(Table table, double power, ArrayList<Double> direction){
        Ball cueBall = table.getBall(0);
        cueBall.setVx(power* direction.get(0));
        cueBall.setVy(power* direction.get(1));
        rule.setShotFired(true);
        rule.setCheckingRule(false);
    }

    public void updatePosition(Ball ball, double dt){
        ball.updatePosition(dt);
        ball.applyFriction(Ball.DEFAULT_FRICTION);
    }

    public void gettingInPocket(Ball ball){
        if (table.willPocket(ball)){
            ball.setPocketed(true);
            ball.setVy(0);
            ball.setVx(0);
            rule.setAnyBallsPocketed(true);
            if(rule.checkPocketedAndPlayer(ball, gameState.getTurn())){gameState.setShouldChangeTurn(false);};
        }else{
            gameState.setShouldChangeTurn(true);
        }
    }

    public void checkBallsCollision(Ball ball){
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

    public void checkBallCushionCollision(Ball ball) {
        List<int[][]> cushionPolygons = table.getCushionPolygons();

        for (int[][] polygon : cushionPolygons) {
            int[] xs = polygon[0];
            int[] ys = polygon[1];
            int numVertices = xs.length;

            for (int i = 0; i < numVertices; i++) {
                int j = (i + 1) % numVertices;

                double ax = xs[i], ay = ys[i];
                double bx = xs[j], by = ys[j];

                double edgeDx = bx - ax;
                double edgeDy = by - ay;
                double lenSq  = edgeDx * edgeDx + edgeDy * edgeDy;
                if (lenSq == 0) continue;

                double t = ((ball.getX() - ax) * edgeDx + (ball.getY() - ay) * edgeDy) / lenSq;
                t = Math.max(0.0, Math.min(1.0, t));

                double closestX = ax + t * edgeDx;
                double closestY = ay + t * edgeDy;

                double diffX = ball.getX() - closestX;
                double diffY = ball.getY() - closestY;
                double dist  = Math.hypot(diffX, diffY);

                if (dist == 0 || dist >= ball.getRadius()) continue;

                double nx = diffX / dist;
                double ny = diffY / dist;

                double velDotNormal = ball.getVx() * nx + ball.getVy() * ny;
                if (velDotNormal >= 0) continue;

                double penetration = ball.getRadius() - dist;
                ball.setX(ball.getX() + nx * penetration);
                ball.setY(ball.getY() + ny * penetration);

                ball.setVx(ball.getVx() - (1 + Table.CUSHION_RESTITUTION) * velDotNormal * nx);
                ball.setVy(ball.getVy() - (1 + Table.CUSHION_RESTITUTION) * velDotNormal * ny);

                rule.addCushionCollisionedBall(ball);
            }
        }
    }

    public void checkMoving(){
        for (Ball ball: table.getBalls()){
            if (!(ball.getVy()==0.0 && ball.getVx()==0.0)){
                gameState.setMoving(true);
                break;
            }
        }
        if (!gameState.isMoving() && rule.isShotFired()) {
            rule.setCheckingRule(true);
        }
    }
}