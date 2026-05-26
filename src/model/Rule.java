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

    public boolean checkBreak(){
        if(!gameState.isDoneBreak()){
            if (cushionCollisionedBalls.toArray().length >= 4 | anyBallsPocketed){
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean checkPocketedAndPlayer(Ball ball, Player player){
        if(ball.getBallType()==player.getSide()){
            return true;
        }
        return false;
    }

    public void resetBreak() {
        cushionCollisionedBalls.clear();
        anyBallsPocketed = false;
        inBreakCheck = true;
    }

    public List<Ball> getCushionCollisionedBalls() {
        return this.cushionCollisionedBalls;
    }

    public void addCushionCollisionedBall(Ball ball) {
        if (!this.cushionCollisionedBalls.contains(ball) & ball.getBallType() != Ball.BallType.CUE){
            this.cushionCollisionedBalls.add(ball);
        }
    }

    public boolean isAnyBallsPocketed() {
        return anyBallsPocketed;
    }

    public void setAnyBallsPocketed(boolean anyBallsPocketed) {
        this.anyBallsPocketed = anyBallsPocketed;
    }

    public boolean isInBreakCheck() {
        return inBreakCheck;
    }

    public void checkRules(){
        if(this.isCheckingRule()){
            if (this.isInBreakCheck()){
                if(this.checkBreak()) {
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
            gameState.switchTurns();
            this.shotFired = false;
            this.checkingRule = false;
            gameState.resetPocketSelection();
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
        return table.allTypesPocketed(player.getSide());
    }


}