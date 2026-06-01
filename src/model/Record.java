package model;

import java.io.Serializable;

public class Record implements Serializable {
    private String winner;
    private String loser;

    public Record(Player winner, Player loser){
        this.winner = winner.getName();
        this.loser = loser.getName();
    }

    public String getWinner() {
        return winner;
    }

    public String getLoser() {
        return loser;
    }
}
