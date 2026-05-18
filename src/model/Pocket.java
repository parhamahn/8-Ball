package model;

public class Pocket {
    private static double DEFAULT_RADIUS = 25;

    private double centerX;
    private double centerY;
    private final double radius;
    private final int number;

    public Pocket(double centerX, double centerY, double radius, int number){
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.number = number;
    }

    public Pocket(double centerX, double centerY, int number){
        this(centerX, centerY, DEFAULT_RADIUS, number);
    }

    public static double getDefaultRadius() {
        return DEFAULT_RADIUS;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public double getRadius() {
        return radius;
    }

    public int getNumber() {
        return number;
    }

    public boolean isBallPocket(double ballX, double ballY, double ballRadius){
        double distance = Math.sqrt(Math.pow((ballX - this.centerX), 2) + Math.pow((ballY - this.centerY), 2));
        return distance < this.radius - ballRadius;
    }
}