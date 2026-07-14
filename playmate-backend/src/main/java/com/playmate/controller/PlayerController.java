package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.dto.ApplyPlayerRequest;
import com.playmate.entity.Player;
import com.playmate.entity.PlayerGame;
import com.playmate.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping("/list")
    public Result<Page<Player>> getPlayerList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long gameId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("orderCount").descending());
        return Result.success(playerService.getPlayerList(status, gameId, pageRequest));
    }

    @GetMapping("/{id}")
    public Result<Player> getPlayerDetail(@PathVariable Long id) {
        return Result.success(playerService.getPlayerDetail(id));
    }

    @GetMapping("/{id}/games")
    public Result<List<PlayerGame>> getPlayerGames(@PathVariable Long id) {
        return Result.success(playerService.getPlayerGames(id));
    }

    @GetMapping("/mine")
    public Result<Player> getMyPlayerInfo(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(playerService.getMyPlayerInfo(userId));
    }

    @PostMapping("/apply")
    public Result<Player> apply(Authentication authentication, @Valid @RequestBody ApplyPlayerRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(playerService.apply(userId, request));
    }

    @PutMapping("/profile")
    public Result<Player> updateProfile(Authentication authentication, @RequestBody ApplyPlayerRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(playerService.updateProfile(userId, request));
    }
}
