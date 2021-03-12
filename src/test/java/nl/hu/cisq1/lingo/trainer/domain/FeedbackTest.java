package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("paard", List.of("P",".",".",".","."),List.of(ABSENT,CORRECT,ABSENT,ABSENT,ABSENT),"palet",List.of("P","A",".",".",".")),
                Arguments.of("hoofd", List.of("H",".",".",".","."),List.of(CORRECT,CORRECT,CORRECT,ABSENT,ABSENT),"horen",List.of("H","O","O",".","."))
        );
    }
    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed(){
         String attempt = "PAARD";
         List<Mark> marks = List.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT);
         Feedback feedback = new Feedback(attempt,marks);

         assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is not guessed if all letters are correct")
    void wordIsNotGuessed(){
        String attempt = "PAARD";
        List<Mark> marks = List.of(CORRECT, CORRECT, CORRECT, CORRECT, ABSENT);
        Feedback feedback = new Feedback(attempt,marks);

        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Guess is not valid if invalid in marks")
    void guessIsInvalid(){
        String attempt = "PAARD";
        List<Mark> marks = List.of(INVALID, CORRECT, CORRECT, CORRECT, ABSENT);
        Feedback feedback = new Feedback(attempt,marks);

        assertFalse(feedback.isGuessValid());
    }

    @Test
    @DisplayName("Guess is valid if invalid not in marks")
    void guessIsNotInvalid(){
        String attempt = "PAARD";
        List<Mark> marks = List.of(CORRECT, CORRECT, CORRECT, CORRECT, ABSENT);
        Feedback feedback = new Feedback(attempt,marks);

        assertTrue(feedback.isGuessValid());
    }

    @Test
    @DisplayName("Feedback length doesnt match word length")
    void invalidFeedback(){

        assertThrows(InvalidFeedbackException.class, () ->
            new Feedback("woord", List.of(Mark.CORRECT)
            ));
    }


    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Gives player a hint")
    void giveHint(String woord, List<String> previoushint,List<Mark> marks,String guesses, List<String> expectedHint){
        Feedback feedback = new Feedback(guesses,marks);

        List<String> result = feedback.giveHint(woord, previoushint);

        assertEquals(expectedHint,result);
    }

}