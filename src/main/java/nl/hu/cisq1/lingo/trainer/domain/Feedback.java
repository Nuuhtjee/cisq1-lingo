package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

public class Feedback {

    private final String attempt;
    private final List<Mark> marks;
    private List<String> hint = new ArrayList<>();

    public Feedback(String attempt, List<Mark> marks) throws InvalidFeedbackException {
        if (attempt.length() != marks.size()){
            throw new InvalidFeedbackException();
        }
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed(){
        return  marks.stream().allMatch(mark -> mark == CORRECT);
    }


    public boolean isGuessValid() {
        return marks.stream().noneMatch(mark -> mark == INVALID);
    }

    public List<String> giveHint(String wordToGuess, List<String> previousHint) throws InvalidFeedbackException{
        String[] listLetters = wordToGuess.toUpperCase().split("");
        List<String> result = new ArrayList<>();
        if (previousHint.size() != wordToGuess.length()){
            throw new InvalidFeedbackException();
        }
        for (int i = 0; i < listLetters.length; i++){
            if (marks.get(i) == CORRECT){
                result.add(listLetters[i]);
            }
            else if (marks.get(i) != CORRECT){
                result.add(previousHint.get(i));
            }
        }
        this.hint = result;

        return result;
    }

    public List<String> getHint() {
        return hint;
    }

    public String getAttempt() {
        return attempt;
    }
}
