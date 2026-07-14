package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.entity.Game;
import com.playmate.entity.Order;
import com.playmate.entity.User;
import com.playmate.entity.WithdrawRequest;
import com.playmate.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final PlayerService playerService;
    private final OrderService orderService;
    private final GameService gameService;
    private final WalletService walletService;

    // ===== 用户管理 =====
    @GetMapping("/users")
    public Result<Page<User>> getUsers(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size) {
        // 简化实现，返回所有用户
        return Result.success(null);
    }

    @PutMapping("/users/{id}")
    public Result<User> updateUser(@PathVariable Long id, @RequestBody User updateData) {
        return Result.success(userService.updateProfile(id, updateData));
    }

    @PutMapping("/users/{id}/ban")
    public Result<Void> banUser(@PathVariable Long id, @RequestBody Map<String, Boolean> params) {
        // 简化实现
        return Result.success();
    }

    // ===== 陪玩师审核 =====
    @GetMapping("/player-applications")
    public Result<?> getPlayerApplications(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "20") int size) {
        return Result.success(playerService.getPlayerList("pending", null, PageRequest.of(page, size)));
    }

    @PutMapping("/player-applications/{id}/approve")
    public Result<Void> approvePlayer(@PathVariable Long id) {
        playerService.approve(id);
        return Result.success();
    }

    @PutMapping("/player-applications/{id}/reject")
    public Result<Void> rejectPlayer(@PathVariable Long id, @RequestBody Map<String, String> params) {
        playerService.reject(id, params.get("reason"));
        return Result.success();
    }

    // ===== 订单管理 =====
    @GetMapping("/orders")
    public Result<Page<Order>> getOrders(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        return Result.success(null);
    }

    @GetMapping("/orders/{id}")
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    // ===== 数据统计 =====
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboard() {
        Map<String, Object> data = new HashMap<>();
        // 简化实现，可以添加更多统计数据
        data.put("totalOrders", 0);
        data.put("pendingOrders", 0);
        data.put("totalUsers", 0);
        data.put("totalPlayers", 0);
        return Result.success(data);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(@RequestParam(required = false) String period) {
        return Result.success(new HashMap<>());
    }

    // ===== 游戏分类管理 =====
    @PostMapping("/games")
    public Result<Game> createGame(@RequestBody Game game) {
        return Result.success(gameService.createGame(game));
    }

    @PutMapping("/games/{id}")
    public Result<Game> updateGame(@PathVariable Long id, @RequestBody Game game) {
        return Result.success(gameService.updateGame(id, game));
    }

    @DeleteMapping("/games/{id}")
    public Result<Void> deleteGame(@PathVariable Long id) {
        gameService.deleteGame(id);
        return Result.success();
    }

    // ===== 提现审核 =====
    @GetMapping("/withdraw-requests")
    public Result<Page<WithdrawRequest>> getWithdrawRequests(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        return Result.success(walletService.getPendingWithdrawals(PageRequest.of(page, size)));
    }

    @PutMapping("/withdraw-requests/{id}/approve")
    public Result<Void> approveWithdraw(@PathVariable Long id) {
        walletService.approveWithdraw(id);
        return Result.success();
    }

    @PutMapping("/withdraw-requests/{id}/reject")
    public Result<Void> rejectWithdraw(@PathVariable Long id, @RequestBody Map<String, String> params) {
        walletService.rejectWithdraw(id, params.get("reason"));
        return Result.success();
    }
}
