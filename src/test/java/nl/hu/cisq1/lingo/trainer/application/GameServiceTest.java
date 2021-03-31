package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.Gamestate;
import nl.hu.cisq1.lingo.trainer.domain.LingoGame;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoGameFoundException;
import nl.hu.cisq1.lingo.words.application.WordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Gamestate.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameServiceTest {

    static Stream<Arguments> provideAttemptExamples() {
        return Stream.of(
                Arguments.of("PAARD",PLAYING,"PALET",List.of("P","A",".",".",".")),
                Arguments.of("HOOFD",PLAYING,"HOREN",List.of("H","O",".",".",".")),
                Arguments.of("APPEL",PLAYING,"WATER",List.of("A",".",".","E",".")),
                Arguments.of("BAARD",WAITING_FOR_ROUND,"BAARD",List.of("B","A","A","R","D"))
        );
    }

    @Test
    @DisplayName("Start new game")
    void startNewGame(){

        SpringGameRepository springGameRepository = mock(SpringGameRepository.class);
        WordService wordService = mock(WordService.class);

        GameService gameService = new GameService(springGameRepository,wordService);

        when(wordService.provideRandomWord(anyInt())).thenReturn("APPEL");
        Progress progress = gameService.startNewGame();

        assertEquals(1,progress.getRoundNumber());
    }


    @Test
    @DisplayName("Start new round")
    void startNewRound(){
        SpringGameRepository springGameRepository = mock(SpringGameRepository.class);

        WordService wordService = mock(WordService.class);

        GameService gameService = new GameService(springGameRepository,wordService);

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.of(new LingoGame()));

        when(wordService.provideRandomWord(anyInt())).thenReturn("PAARD");

        Progress progress = gameService.startNewRound(1);

        assertEquals(List.of("P",".",".",".","."),progress.getHint());
        assertEquals(PLAYING,progress.getStatus());
    }

    @Test
    @DisplayName("Start new round with no available game")
    void startNewRoundNoGame(){

        SpringGameRepository springGameRepository = mock(SpringGameRepository.class);

        WordService wordService = mock(WordService.class);

        GameService gameService = new GameService(springGameRepository,wordService);

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.empty());

        when(wordService.provideRandomWord(anyInt())).thenReturn("PAARD");
        assertThrows(NoGameFoundException.class, () -> {
            gameService.startNewRound(4);
        });
    }

    @Test
    @DisplayName("Start multiple rounds")
    void startMultipleRoundsWithGame(){
        SpringGameRepository springGameRepository = mock(SpringGameRepository.class);

        WordService wordService = mock(WordService.class);

        GameService gameService = new GameService(springGameRepository,wordService);

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.of(new LingoGame()));

        when(wordService.provideRandomWord(anyInt())).thenReturn("PAARD");

        gameService.startNewRound(1);

        assertThrows(InvalidRoundException.class, () -> {
            gameService.startNewRound(1);
        });
    }

    @Test
    @DisplayName("Make attempt on non existing game")
    void makeAttemptNoGame(){
        SpringGameRepository springGameRepository = mock(SpringGameRepository.class);

        WordService wordService = mock(WordService.class);

        GameService gameService = new GameService(springGameRepository,wordService);

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.empty());

        when(wordService.provideRandomWord(anyInt())).thenReturn("PAARD");

        assertThrows(NoGameFoundException.class, () -> {
            gameService.makeAttempt(1,"BAARD");
        });
    }


    @ParameterizedTest
    @MethodSource("provideAttemptExamples")
    @DisplayName("Make attempt on existing game")
    void makeAttempt(String woord, Gamestate gamestate, String attempt, List<String> hint){
        SpringGameRepository springGameRepository = mock(SpringGameRepository.class);

        WordService wordService = mock(WordService.class);

        GameService gameService = new GameService(springGameRepository,wordService);

        when(wordService.provideRandomWord(anyInt())).thenReturn("BAARD");

        LingoGame lingoGame = new LingoGame();
        lingoGame.startNewRound(woord);

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.of(lingoGame));

        Progress progress = gameService.makeAttempt(1,attempt);

        assertEquals(progress.getHint(),hint);
        assertEquals(progress.getStatus(), gamestate);
    }

    @Test
    @DisplayName("Make attempt on non existing round")
    void makeAttemptNoRound(){
        SpringGameRepository springGameRepository = mock(SpringGameRepository.class);

        WordService wordService = mock(WordService.class);

        GameService gameService = new GameService(springGameRepository,wordService);

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.of(new LingoGame()));

        assertThrows(InvalidRoundException.class, () -> {
            gameService.makeAttempt(1,"PAARD");
        });
    }

}