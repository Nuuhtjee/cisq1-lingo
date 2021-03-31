package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static nl.hu.cisq1.lingo.trainer.domain.Gamestate.*;

@Entity
@Table(name="game")
public class LingoGame {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private int id;

    @OneToMany
    @JoinColumn
    @Cascade(CascadeType.ALL)
    private List<Round> rounds = new ArrayList<>();


    @Column
    @Enumerated(EnumType.STRING)
    private Gamestate gamestate;

    @OneToOne
    @Cascade(CascadeType.ALL)
    private Progress progress;


    public LingoGame(){
        gamestate = WAITING_FOR_ROUND;
        this.progress = new Progress(this.id);
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void startNewRound(String word) throws InvalidRoundException {
        if (gamestate == WAITING_FOR_ROUND){
            Round round = new Round(word);
            rounds.add(round);
            gamestate = PLAYING;
            progress.updateProgress(rounds.size(),round.giveHint(),round.getAttempts().size(), gamestate);
        }
        else{
            throw new InvalidRoundException();
        }
    }

    public void makeAttempt(String attempt) throws InvalidRoundException {
        if (gamestate == PLAYING) {
            List<Round> playableRounds = rounds.stream().filter(round -> !round.isRoundDone()).collect(Collectors.toList()); //Searches for a round object thats still ongoing

            playableRounds.stream().findFirst().ifPresent(round -> {
                List<Mark> marks = round.playRound(attempt);
                isPlayerEliminated();
                if (round.wordGuessed()){
                    progress.updateProgress(rounds.size(), round.giveHint(), round.getAttempts().size(),this.gamestate);
                }
                else{
                    progress.setHint(round.giveHint());
                    progress.setMark(marks);
                }
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

    public Progress showProgress(){
        return progress;
    }
}
