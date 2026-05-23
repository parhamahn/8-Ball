package model;

import java.util.List;

public class GameState {
    private Table table;
    private boolean isMoving;
    private Player player1;
    private Player player2;
    private Player turn;
    private boolean sidesAssigned;
    private boolean isFirstShot;
    private boolean isInBreak;

    public GameState(Player player1, Player player2, Table table){
        this.isMoving = false;
        this.player1 = player1;
        this.player2 = player2;
        this.table = table;
        this.player1.setSide(Ball.BallType.SOLID);
        this.player2.setSide(Ball.BallType.STRIPE);
        this.turn = (Math.random()<=0.5? player1 : player2);
        this.sidesAssigned = false;
        this.isFirstShot = true;
        this.isInBreak = true;
    }

    public GameState(boolean isMoving){
        this.isMoving = isMoving;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void switchTurns(){this.turn = this.turn==player1? player2:player1;}

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getTurn() {
        return turn;
    }

    public void setTurn(Player turn) {
        this.turn = turn;
    }

    public boolean isSidesAssigned() {
        return sidesAssigned;
    }

    public void setSidesAssigned(boolean sidesAssigned) {
        this.sidesAssigned = sidesAssigned;
    }

    public boolean isFirstShot() {
        return isFirstShot;
    }

    public void setFirstShot(boolean firstShot) {
        isFirstShot = firstShot;
    }

    public boolean isInBreak() {
        return isInBreak;
    }

    public void setInBreak(boolean inBreak) {
        isInBreak = inBreak;
    }
}
