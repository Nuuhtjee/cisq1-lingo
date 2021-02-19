package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

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



}