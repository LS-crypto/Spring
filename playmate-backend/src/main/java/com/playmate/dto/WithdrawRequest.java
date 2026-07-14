package com.playmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class WithdrawRequest {
    @NotNull(message = "提现金额不能为空")
    private BigDecimal amount;

    @NotBlank(message = "提现方式不能为空")
    private String method; // alipay / wechat / bank

    @NotBlank(message = "账号不能为空")
    private String account;
}
