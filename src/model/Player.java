package model;

public class Player {
    private String name;
    private Ball.BallType side;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ball.BallType getSide() {
        return side;
    }

    public void setSide(Ball.BallType side) {
        this.side = side;
    }

}
