package model;

public class Ball {
    public enum BallType {CUE, SOLID, STRIPE, EIGHT}
    public static final double DEFAULT_RADIUS = 10.0;
    public static final double MIN_VELOCITY = 0.01;

    private double x;
    private double y;
    private double vx;
    private double vy;
    private final double radius;
    private BallType ballType;
    private int number;
    private boolean isPocketed;

    public Ball(double x, double y, double vx, double vy, double radius, BallType ballType, int number, boolean isPocketed){
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = radius;
        this.ballType = ballType;
        this.number = number;
        this.isPocketed = isPocketed;
    }

    public Ball(double x, double y, BallType ballType, int number){
        this(x,y, 0.0,0.0,DEFAULT_RADIUS, ballType, number, false);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getRadius() {
        return radius;
    }

    public BallType getBallType() {
        return ballType;
    }

    public void setBallType(BallType ballType) {
        this.ballType = ballType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPocketed() {
        return isPocketed;
    }

    public void setPocketed(boolean pocketed) {
        isPocketed = pocketed;
    }

    public void updatePosition(double dt){
        this.setX(vx*dt + this.x);
        this.setY(vy*dt + this.y);
    }

    public void applyFriction(double friction){
        this.vx*=(1-friction);
        this.vy*=(1-friction);
        if (Math.abs(this.vx) < MIN_VELOCITY){
            this.vx = 0;
        }
        if (Math.abs(this.vy) < MIN_VELOCITY){
            this.vy = 0;
        }
    }

    public boolean isMoving(){
        return !(this.vx==0 && this.vy==0);
    }

}
