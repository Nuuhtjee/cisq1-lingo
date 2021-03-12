package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidAttemptException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    static Stream<Arguments> provideAttemptExamples() {
        return Stream.of(
                Arguments.of("WOORD","WOONT",List.of(CORRECT,CORRECT,CORRECT,ABSENT,ABSENT)),
                Arguments.of("WOORD","WAGON",List.of(CORRECT,ABSENT,ABSENT,PRESENT,ABSENT)),
                Arguments.of("WOORD","WAKKER",List.of(INVALID,INVALID,INVALID,INVALID,INVALID,INVALID)),
                Arguments.of("WATER","AAPJE",List.of(ABSENT,CORRECT,ABSENT,ABSENT,PRESENT)),
                Arguments.of("PAARD","ATTAA",List.of(PRESENT,ABSENT,ABSENT,PRESENT,ABSENT)),
                Arguments.of("PAARD","TATAA",List.of(ABSENT,CORRECT,ABSENT,PRESENT,ABSENT)),
                Arguments.of("PAARD","AAAAA",List.of(ABSENT,CORRECT,CORRECT,ABSENT,ABSENT))
        );
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("AMPEL",List.of("A",".","P","E","L")),
                Arguments.of("AARDE",List.of("A",".",".",".",".")),
                Arguments.of("APART",List.of("A","P",".",".","."))
        );
    }

    static Stream<Arguments> provideMultipleHintExamples() {
        return Stream.of(
                Arguments.of("BAARD",List.of("BERGEN","BONJE","BARST","DRAAD","BAARD"),List.of("B","A","A","R","D")),
                Arguments.of("APPEL",List.of("AARDE","APART","APRIL","AFHEL","ALERT"),List.of("A","P",".","E","L"))
        );
    }


    @ParameterizedTest
    @MethodSource("provideAttemptExamples")
    @DisplayName("Allows player to make an attempt")
    void makeAttempt(String wordToGuess, String attempt, List<Mark> result){
        Round round = new Round(wordToGuess);

        List<Mark> attemptMark = round.makeAttempt(attempt);

        assertEquals(result,attemptMark);

    }

    @Test
    @DisplayName("Guess is invalid if there are too many attempts")
    void tooManyAttemps(){
        Round round = new Round("Appel");

        round.playRound("Aaien");
        round.playRound("Ademt");
        round.playRound("Aders");
        round.playRound("Acuut");
        round.playRound("Aapje");

        assertThrows(InvalidAttemptException.class, () -> round.playRound("aldus"));
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Give hint based on attempt for the first time")
    void giveFirstHint(String attempt, List<String> expectedHint){
        Round round = new Round("Appel");

        round.playRound(attempt);

        assertEquals(expectedHint,round.giveHint());
    }

    @Test
    @DisplayName("First attempt is faulty and hint should be the same")
    void giveInvalidFirstHint(){
        Round round = new Round("BAARD");

        round.playRound("BERGEN");

        assertEquals(List.of("B",".",".",".","."),round.giveHint());
    }

    @ParameterizedTest
    @MethodSource("provideMultipleHintExamples")
    @DisplayName("Gives player the latest hint available")
    void giveHint(String wordToGuess, List<String> attempt, List<String> expectedHint){
        Round round = new Round(wordToGuess);

        round.playRound(attempt.get(0));
        round.playRound(attempt.get(1));
        round.playRound(attempt.get(2));
        round.playRound(attempt.get(3));
        round.playRound(attempt.get(4));

        assertEquals(expectedHint,round.giveHint());
    }



}