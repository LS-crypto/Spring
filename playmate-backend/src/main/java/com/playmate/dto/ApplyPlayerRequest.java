package com.playmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ApplyPlayerRequest {
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    private String description = "";

    @NotNull(message = "基础价格不能为空")
    private BigDecimal basePrice;

    // 擅长游戏及段位
    private List<PlayerGameDto> games;

    @Data
    public static class PlayerGameDto {
        private Long gameId;
        private String rankLevel;
        private BigDecimal price;
    }
}
