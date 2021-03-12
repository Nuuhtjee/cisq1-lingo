package nl.hu.cisq1.lingo.trainer.domain;

import java.util.ArrayList;
import java.util.List;

public class Progress {

    private int score;
    private List<String> hint;
    private int roundNumber;

    public Progress() {
        this.score = 0;
        this.hint = new ArrayList<>();
        this.roundNumber = 0;
    }

    public void updateProgress(int roundNumber, List<String> hint, int pogingen){
        this.roundNumber = roundNumber;
        this.hint = hint;
        int calcScore = 5 * (5 - pogingen) + 5;
        this.score += calcScore;
    }

    public int getScore() {
        return score;
    }
}
