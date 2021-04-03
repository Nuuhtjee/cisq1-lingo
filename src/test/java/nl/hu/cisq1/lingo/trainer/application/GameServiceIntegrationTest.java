package nl.hu.cisq1.lingo.trainer.application;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.LingoGame;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import nl.hu.cisq1.lingo.trainer.domain.enums.Gamestate;
import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import nl.hu.cisq1.lingo.trainer.domain.exception.NoGameFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.enums.Gamestate.PLAYING;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Gamestate.WAITING_FOR_ROUND;
import static nl.hu.cisq1.lingo.trainer.domain.enums.Mark.INVALID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@Import(CiTestConfiguration.class)
class GameServiceIntegrationTest {

    @Autowired
    private GameService gameService;

    @MockBean
    private SpringGameRepository springGameRepository;

    static Stream<Arguments> provideAttemptExamples() {
        return Stream.of(
                Arguments.of("PAARD", PLAYING, "PALET", List.of("P", "A", ".", ".", ".")),
                Arguments.of("HOOFD", PLAYING, "HOREN", List.of("H", "O", ".", ".", ".")),
                Arguments.of("APPEL", PLAYING, "WATER", List.of("A", ".", ".", "E", ".")),
                Arguments.of("BAARD", WAITING_FOR_ROUND, "BAARD", List.of("B", "A", "A", "R", "D"))
        );
    }

    @Test
    @DisplayName("Makes a guess on a game with no started rounds")
    void makeAttemptWithNoRound() {
        when(springGameRepository.findById(anyInt())).thenReturn(Optional.of(new LingoGame()));

        assertThrows(InvalidRoundException.class, () -> gameService.makeAttempt(1, "PAARD"));
    }

    @Test
    @DisplayName("Makes a guess on an unavailable game")
    void makeAttemptWithNoGame() {
        when(springGameRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NoGameFoundException.class, () -> gameService.makeAttempt(1, "PAARD"));
    }

    @ParameterizedTest
    @MethodSource("provideAttemptExamples")
    @DisplayName("Makes a guess on a game")
    void makeAttempt(String word, Gamestate gamestate, String attempt, List<String> hint) {
        LingoGame lingoGame = new LingoGame();

        lingoGame.startNewRound(word);

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.of(lingoGame));

        Progress progress1 = gameService.makeAttempt(1, attempt);

        assertEquals(hint, progress1.getHint());
        assertEquals(gamestate, progress1.getStatus());
    }

    @Test
    @DisplayName("Makes an invalid guess on a game")
    void makeInvalidAttempt() {
        LingoGame lingoGame = new LingoGame();

        lingoGame.startNewRound("Paard");

        when(springGameRepository.findById(anyInt())).thenReturn(Optional.of(lingoGame));

        Progress progress1 = gameService.makeAttempt(1, "TRAINER");

        assertEquals(PLAYING, progress1.getStatus());
        assertEquals(List.of(INVALID, INVALID, INVALID, INVALID, INVALID, INVALID, INVALID), progress1.getMark());
    }
}
