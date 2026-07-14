package com.playmate.repository;

import com.playmate.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Order> findByPlayerIdOrderByCreatedAtDesc(Long playerId, Pageable pageable);
    List<Order> findByStatus(String status);
    long countByStatus(String status);
}
