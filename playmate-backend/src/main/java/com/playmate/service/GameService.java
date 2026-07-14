package com.playmate.service;

import com.playmate.entity.Game;
import com.playmate.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public List<Game> getAllGames() {
        return gameRepository.findByStatusTrueOrderBySortAsc();
    }

    public Game getGame(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Transactional
    public Game createGame(Game game) {
        return gameRepository.save(game);
    }

    @Transactional
    public Game updateGame(Long id, Game updateData) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game != null) {
            if (updateData.getName() != null) game.setName(updateData.getName());
            if (updateData.getIcon() != null) game.setIcon(updateData.getIcon());
            if (updateData.getSort() != null) game.setSort(updateData.getSort());
            if (updateData.getStatus() != null) game.setStatus(updateData.getStatus());
            return gameRepository.save(game);
        }
        return null;
    }

    @Transactional
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}
