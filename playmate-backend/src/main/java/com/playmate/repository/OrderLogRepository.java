package com.playmate.repository;

import com.playmate.entity.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
    List<OrderLog> findByOrderIdOrderByCreatedAtDesc(Long orderId);
}
