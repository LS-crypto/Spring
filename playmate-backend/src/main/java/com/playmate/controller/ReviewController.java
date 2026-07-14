package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.dto.CreateReviewRequest;
import com.playmate.entity.Review;
import com.playmate.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/create")
    public Result<Review> createReview(Authentication authentication, @Valid @RequestBody CreateReviewRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(reviewService.createReview(userId, request));
    }

    @GetMapping("/order/{orderId}")
    public Result<Review> getReviewByOrder(@PathVariable Long orderId) {
        return Result.success(reviewService.getReviewByOrder(orderId));
    }

    @GetMapping("/player/{playerId}")
    public Result<Page<Review>> getPlayerReviews(@PathVariable Long playerId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return Result.success(reviewService.getPlayerReviews(playerId, PageRequest.of(page, size)));
    }
}
