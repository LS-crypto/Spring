package com.playmate.repository;

import com.playmate.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUserId(Long userId);
    Page<Player> findByStatus(String status, Pageable pageable);
    List<Player> findByOnlineTrueAndStatus(String status);
}
package com.playmate.repository;

import com.playmate.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUserId(Long userId);
    List<Player> findByStatus(String status);
    List<Player> findByOnlineTrueAndStatus(String status);
}
