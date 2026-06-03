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
    private boolean validCalledBallPocketed;
    private boolean anyCushionCollision;
    private boolean collisionWithEight;
    private boolean ballInHand;
    private boolean isEnded;
    private Player winner;
    private Player loser;
    private Ball.Spin selectedSpin;
    private Pocket eightPocket;
    private int turnTimer;
    private boolean timerRunning;

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
        this.validCalledBallPocketed = false;
        this.anyCushionCollision = false;
        this.collisionWithEight = false;
        this.ballInHand = false;
        this.isEnded = false;
        this.winner = null;
        this.loser = null;
        this.selectedSpin = Ball.Spin.STOP;
        this.eightPocket = null;
        this.turnTimer = 30;
        this.timerRunning = false;
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

    public void switchTurns(){
        if(this.shouldChangeTurn){
            this.turn = this.turn==player1? player2:player1;
        }
    }

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

    public void resetSelectedSpin(){
        this.selectedSpin = Ball.Spin.STOP;
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

    public boolean isValidCalledBallPocketed() {
        return validCalledBallPocketed;
    }

    public void setValidCalledBallPocketed(boolean validCalledBallPocketed) {
        this.validCalledBallPocketed = validCalledBallPocketed;
    }

    public boolean isCollisionWithEight() {
        return collisionWithEight;
    }

    public void setCollisionWithEight(boolean collisionWithEight) {
        this.collisionWithEight = collisionWithEight;
    }

    public boolean isBallInHand() { return ballInHand; }
    public void setBallInHand(boolean ballInHand) { this.ballInHand = ballInHand; }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public Player getLoser() {
        return loser;
    }

    public void setLoser(Player loser) {
        this.loser = loser;
    }

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    public void resetGameState() {
        this.isMoving = false;
        this.player1.setSide(Ball.BallType.CUE);
        this.player2.setSide(Ball.BallType.CUE);
        this.turn = (Math.random() <= 0.5 ? player1 : player2);
        this.sidesAssigned = false;
        this.shouldChangeTurn = true;
        this.isPocketChosen = false;
        this.foulHappened = false;
        this.firstCueCollisionedBall = null;
        this.everCollisionedWithSide = false;
        this.anyBallPocketed = false;
        this.validCalledBallPocketed = false;
        this.anyCushionCollision = false;
        this.collisionWithEight = false;
        this.ballInHand = false;
        this.isEnded = false;
        this.winner = null;
        this.doneBreak = false;
        this.chosenPocket = null;
        this.selectedSpin = Ball.Spin.STOP;
        this.eightPocket = null;
    }

    public Pocket getEightPocket() {
        return eightPocket;
    }

    public void setEightPocket(Pocket eightPocket) {
        this.eightPocket = eightPocket;
    }

    public Ball.Spin getSelectedSpin() {
        return selectedSpin;
    }

    public void setSelectedSpin(Ball.Spin selectedSpin) {
        this.selectedSpin = selectedSpin;
    }

    public int getTurnTimer() {
        return turnTimer;
    }

    public void passOneSecond() {
        if (turnTimer > 0) turnTimer--;
    }

    public void resetTimer() {
        this.turnTimer = 30;
    }

    public boolean isTimerRunning() {
        return timerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        this.timerRunning = timerRunning;
    }
}
