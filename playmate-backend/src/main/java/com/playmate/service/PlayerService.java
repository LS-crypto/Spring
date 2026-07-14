package com.playmate.service;

import com.playmate.common.BusinessException;
import com.playmate.dto.ApplyPlayerRequest;
import com.playmate.entity.Player;
import com.playmate.entity.PlayerGame;
import com.playmate.entity.User;
import com.playmate.repository.PlayerGameRepository;
import com.playmate.repository.PlayerRepository;
import com.playmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerGameRepository playerGameRepository;
    private final UserRepository userRepository;

    public Page<Player> getPlayerList(String status, Long gameId, Pageable pageable) {
        if (status != null) {
            return playerRepository.findByStatus(status, pageable);
        }
        return playerRepository.findAll(pageable);
    }

    public Player getPlayerDetail(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("陪玩师不存在"));
    }

    public List<PlayerGame> getPlayerGames(Long playerId) {
        return playerGameRepository.findByPlayerId(playerId);
    }

    public Player getMyPlayerInfo(Long userId) {
        return playerRepository.findByUserId(userId).orElse(null);
    }

    @Transactional
    public Player apply(Long userId, ApplyPlayerRequest request) {
        // 检查是否已经是陪玩师
        if (playerRepository.findByUserId(userId).isPresent()) {
            throw new BusinessException("你已经申请过");
        }

        Player player = new Player();
        player.setUserId(userId);
        player.setRealName(request.getRealName());
        player.setDescription(request.getDescription());
        player.setBasePrice(request.getBasePrice());
        player.setStatus("pending");

        Player saved = playerRepository.save(player);

        // 保存擅长游戏
        if (request.getGames() != null) {
            for (ApplyPlayerRequest.PlayerGameDto dto : request.getGames()) {
                PlayerGame pg = new PlayerGame();
                pg.setPlayerId(saved.getId());
                pg.setGameId(dto.getGameId());
                pg.setRankLevel(dto.getRankLevel());
                pg.setPrice(dto.getPrice());
                playerGameRepository.save(pg);
            }
        }

        return saved;
    }

    @Transactional
    public Player updateProfile(Long userId, ApplyPlayerRequest request) {
        Player player = playerRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("你不是陪玩师"));

        if (request.getRealName() != null) player.setRealName(request.getRealName());
        if (request.getDescription() != null) player.setDescription(request.getDescription());
        if (request.getBasePrice() != null) player.setBasePrice(request.getBasePrice());

        return playerRepository.save(player);
    }

    @Transactional
    public void approve(Long playerId) {
        Player player = getPlayerDetail(playerId);
        player.setStatus("approved");
        playerRepository.save(player);

        // 更新用户角色
        User user = userRepository.findById(player.getUserId())
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setRole("player");
        userRepository.save(user);
    }

    @Transactional
    public void reject(Long playerId, String reason) {
        Player player = getPlayerDetail(playerId);
        player.setStatus("rejected");
        player.setRejectReason(reason);
        playerRepository.save(player);
    }
}
