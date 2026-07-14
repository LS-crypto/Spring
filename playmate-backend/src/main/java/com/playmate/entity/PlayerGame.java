package com.playmate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "player_games", uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "game_id"}))
public class PlayerGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "rank_level", length = 50)
    private String rankLevel = "";

    @Column(precision = 10, scale = 2)
    private BigDecimal price = BigDecimal.ZERO;
}
