package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.dto.CreateOrderRequest;
import com.playmate.entity.Order;
import com.playmate.entity.OrderLog;
import com.playmate.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public Result<Order> createOrder(Authentication authentication, @Valid @RequestBody CreateOrderRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.createOrder(userId, request));
    }

    @GetMapping("/my")
    public Result<Page<Order>> getMyOrders(Authentication authentication,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.getMyOrders(userId, PageRequest.of(page, size)));
    }

    @GetMapping("/received")
    public Result<Page<Order>> getPlayerOrders(Authentication authentication,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {
        Long userId = (Long) authentication.getPrincipal();
        // 需要先获取陪玩师ID，这里简化处理
        return Result.success(orderService.getPlayerOrders(userId, PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        return Result.success(orderService.getOrderDetail(id));
    }

    @GetMapping("/{id}/logs")
    public Result<List<OrderLog>> getOrderLogs(@PathVariable Long id) {
        return Result.success(orderService.getOrderLogs(id));
    }

    @PutMapping("/{id}/accept")
    public Result<Order> acceptOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.acceptOrder(id, userId));
    }

    @PutMapping("/{id}/reject")
    public Result<Order> rejectOrder(@PathVariable Long id, Authentication authentication,
                                     @RequestBody Map<String, String> params) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.rejectOrder(id, userId, params.get("reason")));
    }

    @PutMapping("/{id}/complete")
    public Result<Order> completeOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.completeOrder(id, userId));
    }

    @PutMapping("/{id}/confirm")
    public Result<Order> confirmOrder(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.confirmOrder(id, userId));
    }

    @PutMapping("/{id}/cancel")
    public Result<Order> cancelOrder(@PathVariable Long id, Authentication authentication,
                                     @RequestBody Map<String, String> params) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.cancelOrder(id, userId, params.get("reason")));
    }

    @PostMapping("/{id}/refund")
    public Result<Order> refundOrder(@PathVariable Long id, Authentication authentication,
                                     @RequestBody Map<String, String> params) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(orderService.refundOrder(id, userId, params.get("reason")));
    }
}
