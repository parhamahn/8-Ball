package model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Table {
    private static double DEFAULT_STARTX = 0;
    private static double DEFAULT_STARTY = 200;
    private static double DEFAULT_ENDX = 1200;
    private static double DEFAULT_ENDY = 800;

    private final double startX;
    private final double endX;
    private final double startY;
    private final double endY;
    private final double cushionThickness;
    private List<Pocket> pockets;
    private List<Ball> balls;

    private final double cornerFactor = 1.5;
    private final double centerGapFactor = 0.1;

    public Table() {
        this.startX = DEFAULT_STARTX;
        this.startY = DEFAULT_STARTY;
        this.endX = DEFAULT_ENDX;
        this.endY = DEFAULT_ENDY;
        this.cushionThickness = Pocket.getDefaultRadius() * 2;

        resetBalls();
        resetPockets();

    }

    public void resetBalls() {

        this.balls = new ArrayList<>();

        double cueX = getWidth() / 4.0;
        double cueY = getHeight() / 2.0 + DEFAULT_STARTY;

        double rackX = getWidth() * 3.0 / 4.0;
        double rackY = getHeight() / 2.0 + DEFAULT_STARTY;

        balls.add(new Ball(cueX, cueY, Ball.BallType.CUE, 0));

        double r = Ball.DEFAULT_RADIUS;
        double spacing = 2 * r + 1;

        int[] ballNumbers = {1, 2, 9, 10, 8, 3, 11, 4, 5, 12, 6, 13, 7, 14, 15};
        int index = 0;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col <= row; col++) {
                double x = rackX + row * spacing * Math.cos(Math.PI / 6);
                double y = rackY + (col - row / 2.0) * spacing;

                int number = ballNumbers[index];
                Ball.BallType type;

                if (number == 8) {
                    type = Ball.BallType.EIGHT;
                } else if (number <= 7) {
                    type = Ball.BallType.STRIPE;
                } else {
                    type = Ball.BallType.SOLID;
                }

                this.balls.add(new Ball(x, y, type, number));
                index++;
            }
        }
    }


    public void resetPockets(){
        this.pockets = new ArrayList<>();

        pockets.add(new Pocket(startX + Pocket.getDefaultRadius(), startY + Pocket.getDefaultRadius(), 1));
        pockets.add(new Pocket((endX - startX) / 2, startY + Pocket.getDefaultRadius(), 2));
        pockets.add(new Pocket(endX - Pocket.getDefaultRadius(), startY + Pocket.getDefaultRadius(), 3));
        pockets.add(new Pocket(startX + Pocket.getDefaultRadius(), endY - Pocket.getDefaultRadius(), 4));
        pockets.add(new Pocket((endX - startX) / 2, endY - Pocket.getDefaultRadius(), 5));
        pockets.add(new Pocket(endX - Pocket.getDefaultRadius(), endY - Pocket.getDefaultRadius(), 6));
    }

    public static double getDefaultStartx() {
        return DEFAULT_STARTX;
    }

    public static double getDefaultStarty() {
        return DEFAULT_STARTY;
    }

    public static double getDefaultEndx() {
        return DEFAULT_ENDX;
    }

    public static double getDefaultEndy() {
        return DEFAULT_ENDY;
    }

    public double getStartX() {
        return startX;
    }

    public double getEndX() {
        return endX;
    }

    public double getStartY() {
        return startY;
    }

    public double getEndY() {
        return endY;
    }

    public double getCushionThickness() {
        return cushionThickness;
    }

    public List<Pocket> getPockets() {
        return pockets;
    }

    public double getWidth() {
        return endX - startX;
    }

    public double getHeight() {
        return endY - startY;
    }

    public List<int[][]> getCushionPolygons() {
        double playLeft = startX;
        double playRight = endX;
        double playTop = startY;
        double playBottom = endY;

        double cThick = cushionThickness;
        double pRadius = Pocket.getDefaultRadius();
        double centerX = (playLeft + playRight) / 2.0;
        double centerHalf = (centerGapFactor * pRadius) / 2.0;

        double outerTop = playTop + cThick;
        double outerBottom = playBottom - cThick;
        double outerLeft = playLeft + cThick;
        double outerRight = playRight - cThick;

        List<int[][]> polygons = new ArrayList<>();

        // Top left
        polygons.add(new int[][] {
                {(int)(playLeft + cornerFactor * pRadius + cThick), (int)(centerX - centerHalf - cThick), (int)(centerX - centerHalf), (int)(playLeft + cornerFactor * pRadius)},
                {(int)outerTop, (int)outerTop, (int)playTop, (int)playTop}
        });

        // Top right
        polygons.add(new int[][] {
                {(int)(centerX + centerHalf + cThick), (int)(playRight - cornerFactor * pRadius - cThick), (int)(playRight - cornerFactor * pRadius), (int)(centerX + centerHalf)},
                {(int)outerTop, (int)outerTop, (int)playTop, (int)playTop}
        });

        // Bottom left
        polygons.add(new int[][] {
                {(int)(playLeft + cornerFactor * pRadius + cThick), (int)(centerX - centerHalf - cThick), (int)(centerX - centerHalf), (int)(playLeft + cornerFactor * pRadius)},
                {(int)outerBottom, (int)outerBottom, (int)playBottom, (int)playBottom}
        });

        // Bottom right
        polygons.add(new int[][] {
                {(int)(centerX + centerHalf + cThick), (int)(playRight - cornerFactor * pRadius - cThick), (int)(playRight - cornerFactor * pRadius), (int)(centerX + centerHalf)},
                {(int)outerBottom, (int)outerBottom, (int)playBottom, (int)playBottom}
        });

        // Left side
        polygons.add(new int[][] {
                {(int)outerLeft, (int)outerLeft, (int)playLeft, (int)playLeft},
                {(int)(playTop + cornerFactor * pRadius + cThick), (int)(playBottom - cornerFactor * pRadius - cThick), (int)(playBottom - cornerFactor * pRadius), (int)(playTop + cornerFactor * pRadius)}
        });

        // Right side
        polygons.add(new int[][] {
                {(int)outerRight, (int)outerRight, (int)playRight, (int)playRight},
                {(int)(playTop + cornerFactor * pRadius + cThick), (int)(playBottom - cornerFactor * pRadius - cThick), (int)(playBottom - cornerFactor * pRadius), (int)(playTop + cornerFactor * pRadius)}
        });

        return polygons;
    }

    public List<Ball> getBalls(){
        return balls;
    }

    public Ball getBall(int number){
        for(Ball ball: getBalls()){
            if(ball.getNumber()==number){
                return ball;
            }
        }
        return null;
    }

    public boolean willPocket(Ball ball){
        for (Pocket pocket: pockets){
            if (Point2D.distance(ball.getX(), ball.getY(), pocket.getCenterX(), pocket.getCenterY())<pocket.getRadius()-ball.getRadius()){
                return true;
            }
        }
        return false;
    }

    public void resetTable(){
        resetPockets();
        resetBalls();
    }

}