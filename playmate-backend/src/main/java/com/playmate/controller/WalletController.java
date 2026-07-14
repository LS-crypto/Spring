package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.dto.RechargeRequest;
import com.playmate.entity.WalletTransaction;
import com.playmate.entity.WithdrawRequest;
import com.playmate.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    public Result<BigDecimal> getBalance(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(walletService.getBalance(userId));
    }

    @PostMapping("/recharge")
    public Result<Void> recharge(Authentication authentication, @Valid @RequestBody RechargeRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        walletService.recharge(userId, request.getAmount());
        return Result.success();
    }

    @PostMapping("/withdraw")
    public Result<WithdrawRequest> withdraw(Authentication authentication,
                                            @Valid @RequestBody com.playmate.dto.WithdrawRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(walletService.applyWithdraw(userId, request.getAmount(), request.getMethod(), request.getAccount()));
    }

    @GetMapping("/transactions")
    public Result<Page<WalletTransaction>> getTransactions(Authentication authentication,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "20") int size) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(walletService.getTransactions(userId, PageRequest.of(page, size)));
    }

    @GetMapping("/withdraw-records")
    public Result<Page<WithdrawRequest>> getWithdrawRecords(Authentication authentication,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "20") int size) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(walletService.getWithdrawRecords(userId, PageRequest.of(page, size)));
    }
}
