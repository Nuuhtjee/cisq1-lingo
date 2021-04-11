package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Gamestate;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;

import java.util.ArrayList;
import java.util.List;

public class Progress {

    private int gameid;


    private int score;

    private Gamestate status;

    private List<String> hint;

    private List<Mark> mark;

    private int roundNumber;

    private List<String> previousAttempts = new ArrayList<>();

    public Progress(int gameid, int score, Gamestate status, List<String> hint, List<Mark> mark, int roundNumber, List<String> previousAttempts) {
        this.gameid = gameid;
        this.score = score;
        this.status = status;
        this.hint = hint;
        this.mark = mark;
        this.roundNumber = roundNumber;
        this.previousAttempts = previousAttempts;
    }

    public int getGameid() {
        return gameid;
    }

    public int getScore() {
        return score;
    }

    public Gamestate getStatus() {
        return status;
    }

    public List<String> getHint() {
        return hint;
    }

    public List<Mark> getMark() {
        return mark;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public List<String> getPreviousAttempts() {
        return previousAttempts;
    }
}
