package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.domain.Gamestate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
public class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @DisplayName("Starting a new game")
//    void startNewGame() throws Exception {
//        RequestBuilder request = MockMvcRequestBuilders
//                .post("/game/startgame");
//
//        mockMvc.perform(request)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.score").isNumber())
//                .andExpect(jsonPath("$.score").value(0))
//                .andExpect(jsonPath("$.status").value(Gamestate.PLAYING))
//                .andExpect(jsonPath("$.hint").isArray())
//                .andExpect(jsonPath("$.roundNumber").isNumber())
//                .andExpect(jsonPath("$.roundNumber").value(1));
//    }

}
