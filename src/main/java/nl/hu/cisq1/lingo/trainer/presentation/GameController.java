package nl.hu.cisq1.lingo.trainer.presentation;

import nl.hu.cisq1.lingo.trainer.application.GameService;
import nl.hu.cisq1.lingo.trainer.domain.Progress;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/startgame")
    public Progress startNewGame(){
        return gameService.startNewGame();
    }

    @PostMapping("/startround/{id}")
    public Progress startNewRound(@Validated @PathVariable("id") int id){
        return gameService.startNewRound(id);
    }

    @PostMapping("/guess/{id}")
    public Progress makeAttempt(@Validated @PathVariable("id") int id, @Validated @RequestBody String attempt){
        return gameService.makeAttempt(id,attempt);
    }


}
