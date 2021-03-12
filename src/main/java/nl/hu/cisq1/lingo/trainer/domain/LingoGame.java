package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static nl.hu.cisq1.lingo.trainer.domain.GameStatus.*;

public class LingoGame {

    private List<Round> rounds = new ArrayList<>();
    private GameStatus gameStatus;
    private Progress progress;


    public LingoGame(){
        gameStatus = WAITING_FOR_ROUND;
        this.progress = new Progress();
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void startNewRound(String word) throws InvalidRoundException {
        if (gameStatus == WAITING_FOR_ROUND){
            rounds.add(new Round(word));
            gameStatus = PLAYING;
        }
        else{
            throw new InvalidRoundException();
        }
    }

    public void makeAttempt(String attempt) throws InvalidRoundException {
        if (gameStatus == PLAYING) {
            List<Round> playableRounds = rounds.stream().filter(round -> !round.checkRound()).collect(Collectors.toList()); //Searches for a round object thats still ongoing

            playableRounds.stream().findFirst().ifPresent(round -> {
                round.makeAttempt(attempt);
                isPlayerEliminated();
                progress.updateProgress(rounds.size(), round.giveHint(), round.getAttempts().size());
            });
        }
        else{
                throw new InvalidRoundException();
            }

        }

    public int provideNextWordLength(){
        int size;
        if (rounds.isEmpty()){
            size = 5;
        }
        else{
            if (rounds.get(rounds.size() - 1).getWordToGuess().length() == 7){
                size = 5;
            }
            else{
                size = rounds.get(rounds.size() - 1).getWordToGuess().length() + 1;
            }
        }
        return size;
    }


    public boolean isPlayerEliminated() {
       boolean result = rounds.stream().anyMatch(Round::isPlayerEliminated);
       if(result){
           gameStatus = ELIMINATED;
           return true;
       }
       else{
           if (rounds.isEmpty() || rounds.get(rounds.size() - 1).checkRound()){ //Gets last round in arraylist and checks if round is done
               gameStatus = WAITING_FOR_ROUND;
           }
           else{
               gameStatus = PLAYING;
           }
           return false;
       }
    }

    public Progress showProgress(){
        return progress;
    }
}
