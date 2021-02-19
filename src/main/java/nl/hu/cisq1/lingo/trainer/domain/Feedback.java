package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exception.InvalidFeedbackException;

import java.util.List;
import java.util.Objects;

import static nl.hu.cisq1.lingo.trainer.domain.Mark.*;

public class Feedback {

    private final String attempt;
    private final List<Mark> marks;

    public Feedback(String attempt, List<Mark> marks) throws InvalidFeedbackException {
        if (attempt.length() != marks.size()){
            throw new InvalidFeedbackException();
        }
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed(){
        return  marks.stream().allMatch(mark -> mark == CORRECT);
    }


    public boolean isGuessValid() {
        return marks.stream().noneMatch(mark -> mark == INVALID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return attempt.equals(feedback.attempt) &&
                marks.equals(feedback.marks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attempt, marks);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "attempt='" + attempt + '\'' +
                ", marks=" + marks +
                '}';
    }
}
