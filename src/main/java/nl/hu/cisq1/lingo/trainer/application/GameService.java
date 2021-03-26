package nl.hu.cisq1.lingo.trainer.application;


import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.LingoGame;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoGameFoundException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class GameService {
    private final SpringGameRepository springGameRepository;
    private final WordService wordService;

    public GameService(SpringGameRepository springGameRepository, WordService wordService) {
        this.springGameRepository = springGameRepository;
        this.wordService = wordService;
    }

    public Progress startNewGame(){
        LingoGame lingoGame = new LingoGame();

        int wordLength = lingoGame.provideNextWordLength();

        String word = wordService.provideRandomWord(wordLength);

        lingoGame.startNewRound(word);

        springGameRepository.save(lingoGame);
        return lingoGame.showProgress();
    }

    public Progress startNewRound(int gameID){

        LingoGame game = springGameRepository.findById(gameID).orElseThrow(NoGameFoundException::new);

        String word = wordService.provideRandomWord(game.provideNextWordLength());

        game.startNewRound(word);
        springGameRepository.save(game);
        return game.showProgress();

    }

    public Progress makeAttempt(int gameID, String attempt){
        LingoGame game = springGameRepository.findById(gameID).orElseThrow(NoGameFoundException::new);
        game.makeAttempt(attempt);
        springGameRepository.save(game);
        return game.showProgress();
    }


}
