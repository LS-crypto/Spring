package com.playmate.repository;

import com.playmate.entity.PlayerGame;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {
    List<PlayerGame> findByPlayerId(Long playerId);
    List<PlayerGame> findByGameId(Long gameId);
    void deleteByPlayerId(Long playerId);
}
