package com.playmate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;

    @Column(columnDefinition = "TEXT")
    private String description = "";

    @Column(name = "base_price", precision = 10, scale = 2)
    private BigDecimal basePrice = BigDecimal.ZERO;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating = new BigDecimal("5.0");

    @Column(name = "order_count")
    private Integer orderCount = 0;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @Column(length = 20)
    private String status = "pending"; // pending / approved / rejected

    @Column(name = "reject_reason", length = 500)
    private String rejectReason = "";

    @Column(columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean online = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
