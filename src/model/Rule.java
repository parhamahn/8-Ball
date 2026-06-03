package model;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private GameState gameState;
    private Table table;
    private boolean inBreakCheck;
    private List<Ball> cushionCollisionedBalls;
    private boolean anyBallsPocketed;
    private boolean checkingRule;
    private boolean shotFired;

    public Rule(GameState gameState, Table table){
        this.gameState = gameState;
        this.table = table;
        this.inBreakCheck = true;
        this.cushionCollisionedBalls = new ArrayList<>();
        this.anyBallsPocketed = false;
        this.checkingRule = false;
        this.shotFired = false;
    }

    public boolean checkPocketedAndPlayer(Ball ball, Player player){
        return ball.getBallType()==player.getSide();
    }

    public void resetBreak() {
        cushionCollisionedBalls.clear();
        anyBallsPocketed = false;
        inBreakCheck = true;
        checkingRule = false;
        shotFired = false;
    }

    public void addCushionCollisionedBall(Ball ball) {
        if (!this.cushionCollisionedBalls.contains(ball) & ball.getBallType() != Ball.BallType.CUE){
            this.cushionCollisionedBalls.add(ball);
        }
    }

    public void setAnyBallsPocketed(boolean anyBallsPocketed) {
        this.anyBallsPocketed = anyBallsPocketed;
    }

    public boolean isInBreakCheck() {
        return inBreakCheck;
    }

    public boolean checkIfBreak(){
        if(!gameState.isDoneBreak()){
            if (table.getBall(8).isPocketed()){
                return false;
            }
            return cushionCollisionedBalls.toArray().length >= 4 || anyBallsPocketed;
        }
        return true;
    }

    public void checkBreak(){
        if(this.isCheckingRule()){
            if (this.isInBreakCheck()){
                if(this.checkIfBreak()) {
                    this.inBreakCheck = false;
                    gameState.setDoneBreak(true);
                }else{
                    table.resetTable();
                    this.resetBreak();
                    this.shotFired = false;
                    this.checkingRule = false;
                    return;
                }
            }
            this.shotFired = false;
            this.checkingRule = false;
            gameState.resetPocketSelection();
            gameState.resetSelectedSpin();
        }
    }

    public boolean checkIfCueBallPocketedFoul(Ball ball){
        return ball.getBallType()== Ball.BallType.CUE && ball.isPocketed();
    }

    public boolean checkIfCueCollisionedWithWrongBallFoul(Player player){
        if(gameState.getFirstCueCollisionedBall()==null)return true;
        if (gameState.isSidesAssigned()) {
            Ball first = gameState.getFirstCueCollisionedBall();
            if (first.getBallType() == Ball.BallType.EIGHT && table.allTypesPocketed(gameState.getTurn().getSide())) {
                return !table.allTypesPocketed(player.getSide());
            }
            return first.getBallType() != player.getSide();
        }
        return false;
    }

    public boolean checkIfEverCollisionedWithSideFoul(){
        return !gameState.isEverCollisionedWithSide();
    }

    public boolean checkIfAnyPocketsOrCushionCollisionsFoul(){
        return !(gameState.isAnyBallPocketed() || gameState.isAnyCushionCollision());
    }

    public boolean checkIfEightCollisionsFoul(Table table, Player player){
        if(gameState.isCollisionWithEight()){
            if(table.allTypesPocketed(player.getSide())) return false;
            for(Ball tball: table.getBalls()){
                if(!tball.isPocketed() && tball.getBallType()==player.getSide()){
                    return true;
                }
            }
        }
        return false;
    }

    public void checkFouls(Ball ball, Player player, Table table){
        System.out.println("checkFouls: cuePocketed=" + checkIfCueBallPocketedFoul(ball) + ", wrongBall=" + checkIfCueCollisionedWithWrongBallFoul(player) + ", noSide=" + checkIfEverCollisionedWithSideFoul() + ", noPocketOrCushion=" + checkIfAnyPocketsOrCushionCollisionsFoul() + ", eightFoul=" + checkIfEightCollisionsFoul(table, player));
        if (checkIfCueBallPocketedFoul(ball) ||  checkIfCueCollisionedWithWrongBallFoul(player) || checkIfEverCollisionedWithSideFoul() || checkIfAnyPocketsOrCushionCollisionsFoul() || checkIfEightCollisionsFoul(table, player)){
            gameState.setFoulHappened(true);
            gameState.switchTurns();
            Ball cueBall = table.getBall(0);
            cueBall.setPocketed(false);
            cueBall.setVx(0);
            cueBall.setVy(0);
            cueBall.setX(table.getStartX() + table.getWidth() / 8.0);
            cueBall.setY(table.getStartY() + table.getHeight() / 2.0);
            gameState.setBallInHand(true);
        }
    }

    public void checkRules(){
        if(this.isCheckingRule()){
            checkBreak();
            checkFouls(table.getBall(0), gameState.getTurn(), table);

            if (gameState.isFoulHappened()) {
                gameState.setShouldChangeTurn(true);
            } else if (gameState.isValidCalledBallPocketed()) {
                gameState.setShouldChangeTurn(false);
            } else {
                gameState.switchTurns();
            }

            gameState.resetTimer();
            
            gameState.setTimerRunning(true);
            gameState.setEverCollisionedWithSide(false);
            gameState.setAnyBallPocketed(false);
            gameState.setValidCalledBallPocketed(false);
            gameState.setAnyCushionCollision(false);
            gameState.setCollisionWithEight(false);
            gameState.setShouldChangeTurn(true);

            this.shotFired = false;
            this.checkingRule = false;
            gameState.resetPocketSelection();
            gameState.resetSelectedSpin();
            gameState.setFirstCueCollisionedBall(null);
        }
    }

    public boolean isCheckingRule() {
        return checkingRule;
    }

    public void setCheckingRule(boolean checkingRule) {
        this.checkingRule = checkingRule;
    }

    public boolean isShotFired() {
        return shotFired;
    }

    public void setShotFired(boolean shotFired) {
        this.shotFired = shotFired;
    }

    public boolean checkWin(Player player){
        boolean won = table.allTypesPocketed(player.getSide()) && table.getBall(8).isPocketed();
        if (won) {
            gameState.setWinner(player);
            gameState.setLoser(gameState.getPlayer1()!=player?gameState.getPlayer1():gameState.getPlayer2());
            gameState.setEnded(true);
        }
        return won;
    }

    public boolean checkLose(Player player, Pocket eightPocketedIn){
        boolean eightPocketedWrong = !table.allTypesPocketed(player.getSide()) && table.getBall(8).isPocketed();
        boolean eightPocketedInWrongPocket = table.getBall(8).isPocketed() && gameState.getChosenPocket()!=eightPocketedIn;
        boolean foulOnEightShot = table.allTypesPocketed(player.getSide()) && gameState.isFoulHappened();
        boolean lose = eightPocketedWrong || eightPocketedInWrongPocket || foulOnEightShot;
        if (lose){
            gameState.setLoser(player);
            gameState.setWinner(gameState.getPlayer1()!=player?gameState.getPlayer1():gameState.getPlayer2());
            gameState.setEnded(true);
        }
        return lose;
    }
}
