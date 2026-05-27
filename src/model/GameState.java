package model;

public class GameState {
    private Table table;
    private boolean isMoving;
    private Player player1;
    private Player player2;
    private Player turn;
    private boolean sidesAssigned;
    private boolean isFirstShot;
    private boolean doneBreak;
    private boolean shouldChangeTurn;
    private boolean isPocketChosen;
    private Pocket chosenPocket;
    private boolean foulHappened;
    private Ball firstCueCollisionedBall;
    private boolean everCollisionedWithSide;
    private boolean anyBallPocketed;
    private boolean anyCushionCollision;
    private boolean collisionWithEight;
    private boolean ballInHand;

    public GameState(Player player1, Player player2, Table table){
        this.isMoving = false;
        this.player1 = player1;
        this.player2 = player2;
        this.table = table;
        this.player1.setSide(Ball.BallType.CUE);
        this.player2.setSide(Ball.BallType.CUE);
        this.turn = (Math.random()<=0.5? player1 : player2);
        this.sidesAssigned = false;
        this.shouldChangeTurn = true;
        this.isPocketChosen = false;
        this.foulHappened = false;
        this.firstCueCollisionedBall = null;
        this.everCollisionedWithSide = false;
        this.anyBallPocketed = false;
        this.anyCushionCollision = false;
        this.collisionWithEight = false;
        this.ballInHand = false;
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

    public void switchTurns(){if(this.shouldChangeTurn){this.turn = this.turn==player1? player2:player1;}}

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

    public boolean isDoneBreak() {
        return doneBreak;
    }

    public void setDoneBreak(boolean doneBreak) {
        this.doneBreak = doneBreak;
    }

    public boolean isShouldChangeTurn() {
        return shouldChangeTurn;
    }

    public void setShouldChangeTurn(boolean shouldChangeTurn) {
        this.shouldChangeTurn = shouldChangeTurn;
    }

    public void assignSides(Ball.BallType turnPlayerSide){
        this.turn.setSide(turnPlayerSide);
        if(this.turn==player1){
            player2.setSide(turnPlayerSide== Ball.BallType.SOLID? Ball.BallType.STRIPE: Ball.BallType.SOLID);
        }else{
            player1.setSide(turnPlayerSide== Ball.BallType.SOLID? Ball.BallType.STRIPE: Ball.BallType.SOLID);
        }
    }

    public boolean isPocketChosen() {
        return isPocketChosen;
    }

    public Pocket getChosenPocket() {
        return chosenPocket;
    }

    public void setChosenPocket(Pocket chosenPocket) {
        this.chosenPocket = chosenPocket;
        this.isPocketChosen = true;
    }

    public void resetPocketSelection() {
        this.chosenPocket = null;
        this.isPocketChosen = false;
    }

    public boolean isFoulHappened() {
        return foulHappened;
    }

    public void setFoulHappened(boolean foulHappened) {
        this.foulHappened = foulHappened;
    }

    public Ball getFirstCueCollisionedBall() {
        return firstCueCollisionedBall;
    }

    public void setFirstCueCollisionedBall(Ball firstCueCollisionedBall) {
        this.firstCueCollisionedBall = firstCueCollisionedBall;
    }

    public boolean isEverCollisionedWithSide() {
        return everCollisionedWithSide;
    }

    public void setEverCollisionedWithSide(boolean everCollisionedWithSide) {
        this.everCollisionedWithSide = everCollisionedWithSide;
    }

    public boolean isAnyCushionCollision() {
        return anyCushionCollision;
    }

    public void setAnyCushionCollision(boolean anyCushionCollision) {
        this.anyCushionCollision = anyCushionCollision;
    }

    public boolean isAnyBallPocketed() {
        return anyBallPocketed;
    }

    public void setAnyBallPocketed(boolean anyBallPocketed) {
        this.anyBallPocketed = anyBallPocketed;
    }

    public boolean isCollisionWithEight() {
        return collisionWithEight;
    }

    public void setCollisionWithEight(boolean collisionWithEight) {
        this.collisionWithEight = collisionWithEight;
    }

    public boolean isBallInHand() { return ballInHand; }
    public void setBallInHand(boolean ballInHand) { this.ballInHand = ballInHand; }

}
