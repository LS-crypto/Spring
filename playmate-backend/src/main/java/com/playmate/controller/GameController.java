package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.entity.Game;
import com.playmate.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/categories")
    public Result<List<Game>> getAllGames() {
        return Result.success(gameService.getAllGames());
    }

    @GetMapping("/{id}")
    public Result<Game> getGame(@PathVariable Long id) {
        return Result.success(gameService.getGame(id));
    }
}
