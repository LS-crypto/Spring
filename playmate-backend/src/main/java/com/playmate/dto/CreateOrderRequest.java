package com.playmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateOrderRequest {
    @NotNull(message = "陪玩师ID不能为空")
    private Long playerId;

    @NotNull(message = "游戏ID不能为空")
    private Long gameId;

    private Integer quantity = 1;

    private String remark = "";
}
