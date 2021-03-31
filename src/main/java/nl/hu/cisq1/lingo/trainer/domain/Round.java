package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.*;

@Entity
public class Round {

    @GeneratedValue
    @Id
    private int id;

    @Column
    private String wordToGuess;
    private boolean roundDone;

    @OneToMany
    @JoinColumn
    @Cascade(CascadeType.ALL)
    private List<Feedback> attempts = new ArrayList<>();

    public Round(String wordToGuess){
        this.wordToGuess = wordToGuess;
        this.roundDone = false;
    }

    public Round(){

    }

    public List<Mark> makeAttempt(String attempt) {

        String[] wordList = wordToGuess.toUpperCase().split("");
        String[] attemptList = attempt.toUpperCase().split("");

        List<Mark> result = new ArrayList<>();

        if (wordList.length != attemptList.length){
            Arrays.stream(attemptList).forEach(s -> result.add(INVALID));
        }
        else{
            for (int i = 0; i < attemptList.length; i++) {
                if (wordList[i].equals(attemptList[i])){
                    wordList[i] = ".";
                    attemptList[i] = ".";
                }
            }
            for (int i = 0; i < attemptList.length; i++) {
                if (wordList[i].equals(".")){
                    result.add(CORRECT);
                }
                else{
                    if (!wordList[i].equals(attemptList[i])) {
                        List<String> subarr = Arrays.asList(attemptList).subList(0,i + 1);
                        if (Arrays.asList(wordList).contains(attemptList[i])) {
                            String letter = attemptList[i];
                            int amountOfLetterInAttempt = (int) subarr.stream().filter(item -> item.equals(letter)).count();
                            int amountOfLetterInWord = (int) Arrays.stream(wordList).filter(item -> item.equals(letter)).count();

                            if (amountOfLetterInAttempt > amountOfLetterInWord){
                                result.add(ABSENT);
                            }
                            else {
                                result.add(PRESENT);
                            }

                        }
                        else{
                            result.add(ABSENT);
                        }
                    }
                }
            }
        }
        Feedback feedback = new Feedback(attempt,result);
        attempts.add(feedback);
        return result;
    }

    public List<String> giveHint(){

        String[] word = wordToGuess.split("");
        List<String> resList = new ArrayList<>();
        List<String> hint;

        for (int i = 0; i < word.length; i++) {
            if (i == 0){
                resList.add(word[0]);
            }
            else{
                resList.add(".");
            }
        }

        hint = resList;

        for (Feedback feedback : attempts) {
            if (attempts.indexOf(feedback) == 0){
                hint = feedback.giveHint(wordToGuess,resList);
            }
            else{
                List<String> previousHint = attempts.get(attempts.indexOf(feedback)-1).getHint();
                hint = feedback.giveHint(wordToGuess,previousHint);
            }
        }
        return hint;

    }



    public List<Mark> playRound(String attempt) throws InvalidAttemptException{
        if (!roundDone && attempts.size() != 5){
            List<Mark> marks = this.makeAttempt(attempt);
            roundDone = checkRound();
            return marks;

        }
        else{
            roundDone = checkRound();
            throw new InvalidAttemptException();
        }
    }

    public boolean checkRound(){
        return attempts.stream().anyMatch(Feedback::isWordGuessed) || attempts.size() > 4;
    }

    public boolean wordGuessed(){
        return attempts.stream().anyMatch(Feedback::isWordGuessed);
    }

    public boolean isPlayerEliminated(){
        return attempts.size() == 5 && attempts.stream().noneMatch(Feedback::isWordGuessed);
    }


    public List<Feedback> getAttempts() {
        return attempts;
    }

    public String getWordToGuess() {
        return wordToGuess;
    }

    public boolean isRoundDone() {
        return roundDone;
    }
}
