package com.playmate.service;

import com.playmate.common.BusinessException;
import com.playmate.dto.CreateReviewRequest;
import com.playmate.entity.Order;
import com.playmate.entity.Player;
import com.playmate.entity.Review;
import com.playmate.repository.OrderRepository;
import com.playmate.repository.PlayerRepository;
import com.playmate.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public Review createReview(Long userId, CreateReviewRequest request) {
        // 检查订单
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException("订单不存在"));

        if (!order.getUserId().equals(userId)) {
            throw new BusinessException("无权评价");
        }

        if (!"completed".equals(order.getStatus())) {
            throw new BusinessException("订单未完成，不能评价");
        }

        // 检查是否已评价
        if (reviewRepository.findByOrderId(request.getOrderId()).isPresent()) {
            throw new BusinessException("已评价过");
        }

        Review review = new Review();
        review.setOrderId(request.getOrderId());
        review.setUserId(userId);
        review.setPlayerId(order.getPlayerId());
        review.setRating(request.getRating());
        review.setContent(request.getContent());

        Review saved = reviewRepository.save(review);

        // 更新陪玩师评分
        updatePlayerRating(order.getPlayerId());

        return saved;
    }

    public Review getReviewByOrder(Long orderId) {
        return reviewRepository.findByOrderId(orderId).orElse(null);
    }

    public Page<Review> getPlayerReviews(Long playerId, Pageable pageable) {
        return reviewRepository.findByPlayerIdOrderByCreatedAtDesc(playerId, pageable);
    }

    private void updatePlayerRating(Long playerId) {
        Page<Review> reviews = reviewRepository.findByPlayerIdOrderByCreatedAtDesc(playerId, Pageable.unpaged());
        double avgRating = reviews.getContent().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(5.0);

        Player player = playerRepository.findById(playerId).orElse(null);
        if (player != null) {
            player.setRating(BigDecimal.valueOf(avgRating).setScale(1, RoundingMode.HALF_UP));
            playerRepository.save(player);
        }
    }
}
