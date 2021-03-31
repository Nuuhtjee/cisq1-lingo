package nl.hu.cisq1.lingo.trainer.presentation;

import com.jayway.jsonpath.JsonPath;
import nl.hu.cisq1.lingo.CiTestConfiguration;
import nl.hu.cisq1.lingo.trainer.data.SpringGameRepository;
import nl.hu.cisq1.lingo.trainer.domain.LingoGame;
import nl.hu.cisq1.lingo.trainer.domain.enums.Gamestate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(CiTestConfiguration.class)
@AutoConfigureMockMvc
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringGameRepository springGameRepository;

    private int gameidWithRound;

    private int gameidWithoutRound;

    @BeforeEach
    public void init() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/startgame");

        MvcResult result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        gameidWithRound = JsonPath.parse(response).read("$.gameid");

        gameidWithoutRound = springGameRepository.save(new LingoGame()).getId();

    }
    @Test
    @DisplayName("Starting a new game")
    void startNewGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/startgame");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").isNumber())
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.status").value(Gamestate.PLAYING.toString()))
                .andExpect(jsonPath("$.hint").isArray())
                .andExpect(jsonPath("$.mark").isArray())
                .andExpect(jsonPath("$.roundNumber").isNumber())
                .andExpect(jsonPath("$.roundNumber").value(1));
    }

    @Test
    @DisplayName("Get game information")
    void getGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/game/{id}",gameidWithRound);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").isNumber())
                .andExpect(jsonPath("$.hint").isArray())
                .andExpect(jsonPath("$.mark").isArray())
                .andExpect(jsonPath("$.roundNumber").isNumber());
    }

    @Test
    @DisplayName("Starting a new round to a non existing game")
    void startNewRoundNoGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/startround/-10");

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Starting a new round to an existing game")
    void startNewRoundWithGame() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/startround/{id}",gameidWithoutRound);

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Make attempt on an existing game with a round")
    void makeAttempt() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess/{id}",gameidWithRound)
                .content("paard");

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Make attempt on a non existing game")
    void makeAttemptNoGame() throws Exception{
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess/-10")
                .content("PAARD");

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Make invalid attempt")
    void makeInvalidAttempt() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess/{id}",gameidWithRound)
                .content("PAARDENHOK");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mark").isArray());
    }

    @Test
    @DisplayName("Make attempt on an existing game but with no available rounds")
    void makeAttemptNoRound() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .post("/game/guess/{id}",gameidWithoutRound)
                .content("PAARD");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

}
