package nl.hu.cisq1.lingo.trainer.data;

import nl.hu.cisq1.lingo.trainer.domain.GameStatus;
import nl.hu.cisq1.lingo.trainer.domain.LingoGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringGameRepository extends JpaRepository<LingoGame, Integer> {

    Optional<LingoGame> findByGameStatusAndId(GameStatus gameStatus, int id);
}
