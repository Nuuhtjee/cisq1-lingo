package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidRoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LingoGameTest {

    static Stream<Arguments> provideAttemptExamples() {
        return Stream.of(
                Arguments.of("paard", "pinda"),
                Arguments.of("appel", "aapje")
        );
    }

    static Stream<Arguments> provideNextWordLength() {
        return Stream.of(
                Arguments.of("paard", 6),
                Arguments.of("appels", 7),
                Arguments.of("familie",5)
        );
    }

    static Stream<Arguments> provideEliminationExamples(){
        return Stream.of(
                Arguments.of("BAARD",List.of("BERGEN","BONJE","BARST","DRAAD","BAARD"),false),
                Arguments.of("APPEL",List.of("AARDE","APART","APRIL","AFHEL","ALERT"),true)
        );
    }

    @Test
    @DisplayName("Allows player to start a round")
    void startNewRound(){
        LingoGame lingoGame = new LingoGame();

        String word = "APPEL";

        lingoGame.startNewRound(word);

        assertEquals(1, lingoGame.getRounds().size());
    }

    @Test
    @DisplayName("Throws exception if there is still an ongoing round")
    void startInvalidNewRound(){
        LingoGame lingoGame = new LingoGame();

        String word = "APPEL";
        String word2 = "BANAAN";

        lingoGame.startNewRound(word);

        assertThrows(InvalidRoundException.class, () -> lingoGame.startNewRound(word2));
    }

    @ParameterizedTest
    @MethodSource("provideAttemptExamples")
    @DisplayName("Make a guess to an available round")
    void makeAttempt(String wordToGuess, String attempt){
        LingoGame lingoGame = new LingoGame();

        lingoGame.startNewRound(wordToGuess);

        lingoGame.makeAttempt(attempt);

        Round round = lingoGame.getRounds().get(0);

        Feedback feedback = round.getAttempts().get(0);

        assertEquals(feedback.getAttempt(), attempt);

    }

    @Test
    @DisplayName("Make an invalid guess to an unavailable round")
    void makeInvalidAttempt(){
        LingoGame lingoGame = new LingoGame();

        assertThrows(InvalidRoundException.class, () -> lingoGame.makeAttempt("PAARD"));
    }

    @ParameterizedTest
    @MethodSource("provideNextWordLength")
    @DisplayName("Provides next word length")
    void provideNextWordLength(String word, int expectedResult){
        LingoGame lingoGame = new LingoGame();

        lingoGame.startNewRound(word);
        assertEquals(expectedResult,lingoGame.provideNextWordLength());


    }

    @ParameterizedTest
    @MethodSource("provideEliminationExamples")
    @DisplayName("Checks if player is eliminated")
    void isPlayerEliminated(String wordToGuess,List<String> attempts, boolean expectedResult){
        LingoGame lingoGame = new LingoGame();

        lingoGame.startNewRound(wordToGuess);

        lingoGame.makeAttempt(attempts.get(0));
        lingoGame.makeAttempt(attempts.get(1));
        lingoGame.makeAttempt(attempts.get(2));
        lingoGame.makeAttempt(attempts.get(3));
        lingoGame.makeAttempt(attempts.get(4));




        assertEquals(expectedResult, lingoGame.isPlayerEliminated());
    }

    @Test
    @DisplayName("Checks if player is eliminated without there being a round")
    void isPlayerEliminatedWithoutRound(){
        LingoGame lingoGame = new LingoGame();
        System.out.println(lingoGame.isPlayerEliminated());
        assertFalse(lingoGame.isPlayerEliminated());
    }



}