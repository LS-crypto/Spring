package com.playmate.service;

import com.playmate.common.BusinessException;
import com.playmate.dto.CreateOrderRequest;
import com.playmate.entity.*;
import com.playmate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLogRepository orderLogRepository;
    private final PlayerRepository playerRepository;
    private final PlayerGameRepository playerGameRepository;
    private final UserRepository userRepository;

    @Transactional
    public Order createOrder(Long userId, CreateOrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        Player player = playerRepository.findById(request.getPlayerId())
                .orElseThrow(() -> new BusinessException("陪玩师不存在"));

        if (!"approved".equals(player.getStatus())) {
            throw new BusinessException("陪玩师未通过审核");
        }

        // 获取价格
        PlayerGame pg = playerGameRepository.findByPlayerId(request.getPlayerId()).stream()
                .filter(g -> g.getGameId().equals(request.getGameId()))
                .findFirst()
                .orElse(null);

        BigDecimal unitPrice = pg != null ? pg.getPrice() : player.getBasePrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

        // 检查余额
        if (user.getBalance().compareTo(totalPrice) < 0) {
            throw new BusinessException("余额不足");
        }

        // 扣款
        user.setBalance(user.getBalance().subtract(totalPrice));
        userRepository.save(user);

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setPlayerId(request.getPlayerId());
        order.setGameId(request.getGameId());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(unitPrice);
        order.setTotalPrice(totalPrice);
        order.setRemark(request.getRemark());
        order.setStatus("pending");

        Order saved = orderRepository.save(order);

        // 记录日志
        addOrderLog(saved.getId(), "created", "订单创建", userId);

        return saved;
    }

    public Page<Order> getMyOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Order> getPlayerOrders(Long playerId, Pageable pageable) {
        return orderRepository.findByPlayerIdOrderByCreatedAtDesc(playerId, pageable);
    }

    public Order getOrderDetail(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new BusinessException("订单不存在"));
    }

    public List<OrderLog> getOrderLogs(Long orderId) {
        return orderLogRepository.findByOrderIdOrderByCreatedAtDesc(orderId);
    }

    @Transactional
    public Order acceptOrder(Long orderId, Long playerId) {
        Order order = getOrderDetail(orderId);
        validatePlayerOrder(order, playerId);

        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许接单");
        }

        order.setStatus("active");
        order.setAcceptedAt(LocalDateTime.now());
        addOrderLog(orderId, "accepted", "陪玩师接单", playerId);

        return orderRepository.save(order);
    }

    @Transactional
    public Order rejectOrder(Long orderId, Long playerId, String reason) {
        Order order = getOrderDetail(orderId);
        validatePlayerOrder(order, playerId);

        if (!"pending".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许拒绝");
        }

        order.setStatus("cancelled");
        order.setCancelledAt(LocalDateTime.now());
        addOrderLog(orderId, "rejected", "拒绝原因: " + reason, playerId);

        // 退款
        refundToUser(order);

        return orderRepository.save(order);
    }

    @Transactional
    public Order completeOrder(Long orderId, Long playerId) {
        Order order = getOrderDetail(orderId);
        validatePlayerOrder(order, playerId);

        if (!"active".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许完成");
        }

        order.setStatus("completing");
        addOrderLog(orderId, "completed", "陪玩师完成服务", playerId);

        return orderRepository.save(order);
    }

    @Transactional
    public Order confirmOrder(Long orderId, Long userId) {
        Order order = getOrderDetail(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作");
        }

        if (!"completing".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许确认");
        }

        order.setStatus("completed");
        order.setCompletedAt(LocalDateTime.now());
        addOrderLog(orderId, "confirmed", "用户确认完成", userId);

        // 结算给陪玩师
        settleToPlayer(order);

        // 更新陪玩师订单数
        Player player = playerRepository.findById(order.getPlayerId()).orElse(null);
        if (player != null) {
            player.setOrderCount(player.getOrderCount() + 1);
            playerRepository.save(player);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId, Long userId, String reason) {
        Order order = getOrderDetail(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作");
        }

        if ("completed".equals(order.getStatus()) || "cancelled".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许取消");
        }

        order.setStatus("cancelled");
        order.setCancelledAt(LocalDateTime.now());
        addOrderLog(orderId, "cancelled", "取消原因: " + reason, userId);

        // 退款
        refundToUser(order);

        return orderRepository.save(order);
    }

    @Transactional
    public Order refundOrder(Long orderId, Long userId, String reason) {
        Order order = getOrderDetail(orderId);
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权操作");
        }

        if (!"active".equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许退款");
        }

        order.setStatus("refunded");
        order.setCancelledAt(LocalDateTime.now());
        addOrderLog(orderId, "refunded", "退款原因: " + reason, userId);

        refundToUser(order);

        return orderRepository.save(order);
    }

    private void refundToUser(Order order) {
        User user = userRepository.findById(order.getUserId()).orElse(null);
        if (user != null) {
            user.setBalance(user.getBalance().add(order.getTotalPrice()));
            userRepository.save(user);
        }
    }

    private void settleToPlayer(Order order) {
        Player player = playerRepository.findById(order.getPlayerId()).orElse(null);
        if (player != null) {
            User playerUser = userRepository.findById(player.getUserId()).orElse(null);
            if (playerUser != null) {
                playerUser.setBalance(playerUser.getBalance().add(order.getTotalPrice()));
                userRepository.save(playerUser);
            }
        }
    }

    private void validatePlayerOrder(Order order, Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new BusinessException("陪玩师不存在"));
        if (!order.getPlayerId().equals(player.getId())) {
            throw new BusinessException("无权操作此订单");
        }
    }

    private void addOrderLog(Long orderId, String action, String content, Long operatorId) {
        OrderLog log = new OrderLog();
        log.setOrderId(orderId);
        log.setAction(action);
        log.setContent(content);
        log.setOperatorId(operatorId);
        orderLogRepository.save(log);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "PM" + timestamp + random;
    }
}
