package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProgressTest {

    @Test
    @DisplayName("Update progress based on round")
    void updateProgress(){
        Progress progress = new Progress();

        progress.updateProgress(1, List.of("B",".",".",".","."), 3);

        assertEquals(15, progress.getScore());
    }


}