package com.playmate.repository;

import com.playmate.entity.WithdrawRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequest, Long> {
    Page<WithdrawRequest> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<WithdrawRequest> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    List<WithdrawRequest> findByStatus(String status);
}
