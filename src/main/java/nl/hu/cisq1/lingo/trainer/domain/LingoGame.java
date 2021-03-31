package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.enums.Gamestate;
import nl.hu.cisq1.lingo.trainer.domain.enums.Mark;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Gamestate.*;

@Entity
@Table(name="game")
public class LingoGame {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany
    @JoinColumn
    @Cascade(CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();


    @Column
    @Enumerated(EnumType.STRING)
    private Gamestate gamestate;

    private int score;


    public LingoGame(){
        gamestate = WAITING_FOR_ROUND;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void startNewRound(String word) throws InvalidRoundException {
        if (gamestate == WAITING_FOR_ROUND){
            Round round = new Round(word);
            rounds.add(round);
            gamestate = PLAYING;
        }
        else{
            throw new InvalidRoundException();
        }
    }

    public void makeAttempt(String attempt) throws InvalidRoundException {
        if (gamestate == PLAYING) {
            Round round = rounds.get(rounds.size() - 1);

            round.playRound(attempt);
            isPlayerEliminated();
            if (round.wordGuessed()) {
                score = score + 5 * (5 - round.getAttempts().size()) + 5;
            }
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
           gamestate = ELIMINATED;
           return true;
       }
       else{
           if (rounds.isEmpty() || rounds.get(rounds.size() - 1).isRoundDone()){ //Gets last round in arraylist and checks if round is done
               gamestate = WAITING_FOR_ROUND;
           }
           else{
               gamestate = PLAYING;
           }
           return false;
       }
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public List<String> getLastHint(){
        List<String> result = new ArrayList<>();
        if (!rounds.isEmpty()){
            result = rounds.get(rounds.size() - 1).giveHint();
        }
        return result;
    }

    public List<Mark> getLastMark(){
        List<Mark> result = new ArrayList<>();
        if (!rounds.isEmpty() && !rounds.get(rounds.size() - 1).getAttempts().isEmpty()){
            Round lastRound = rounds.get(rounds.size() - 1);
            Feedback lastFeedback = lastRound.getAttempts().get(lastRound.getAttempts().size() -1);
            result = lastFeedback.getMarks();
        }
        return result;
    }
}
