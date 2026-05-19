package model;

public class GameState {
    private boolean isMoving;

    public GameState(boolean isMoving){
        this.isMoving = isMoving;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }
}
