package nl.hu.cisq1.lingo.trainer.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Progress {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private int score;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @ElementCollection
    private List<String> hint;

    @ElementCollection
    private List<Mark> mark;

    private int roundNumber;

    @Column
    private int gameid;

    public Progress(int gameid) {
        this.score = 0;
        this.hint = new ArrayList<>();
        this.roundNumber = 0;
        this.gameid = gameid;
        this.mark = new ArrayList<>();
    }

    public Progress(){

    }

    public void updateProgress(int roundNumber, List<String> hint, int pogingen, GameStatus gameStatus){
        this.roundNumber = roundNumber;
        this.hint = hint;
        int calcScore = 5 * (5 - pogingen) + 5;
        this.score += calcScore;
        this.status = gameStatus;
    }

    public void setHint(List<String> hint){
        this.hint = hint;
    }

    public void setMark(List<Mark> mark){
        this.mark = mark;
    }


    public int getScore() {
        return score;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public List<String> getHint() {
        return hint;
    }

    public GameStatus getStatus() {
        return status;
    }

}
