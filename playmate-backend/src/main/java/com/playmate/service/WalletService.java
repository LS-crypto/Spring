package com.playmate.service;

import com.playmate.common.BusinessException;
import com.playmate.entity.User;
import com.playmate.entity.WalletTransaction;
import com.playmate.entity.WithdrawRequest;
import com.playmate.repository.UserRepository;
import com.playmate.repository.WalletTransactionRepository;
import com.playmate.repository.WithdrawRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final WithdrawRequestRepository withdrawRequestRepository;

    public BigDecimal getBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));
        return user.getBalance();
    }

    @Transactional
    public void recharge(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("充值金额必须大于0");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);

        // 记录流水
        WalletTransaction tx = new WalletTransaction();
        tx.setUserId(userId);
        tx.setType("recharge");
        tx.setAmount(amount);
        tx.setBalanceAfter(user.getBalance());
        tx.setRemark("充值");
        walletTransactionRepository.save(tx);
    }

    @Transactional
    public WithdrawRequest applyWithdraw(Long userId, BigDecimal amount, String method, String account) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("提现金额必须大于0");
        }

        if (user.getBalance().compareTo(amount) < 0) {
            throw new BusinessException("余额不足");
        }

        // 冻结金额（扣减余额）
        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        // 创建提现申请
        WithdrawRequest request = new WithdrawRequest();
        request.setUserId(userId);
        request.setAmount(amount);
        request.setMethod(method);
        request.setAccount(account);
        request.setStatus("pending");

        // 记录流水
        WalletTransaction tx = new WalletTransaction();
        tx.setUserId(userId);
        tx.setType("withdraw");
        tx.setAmount(amount.negate());
        tx.setBalanceAfter(user.getBalance());
        tx.setRemark("提现申请");
        walletTransactionRepository.save(tx);

        return withdrawRequestRepository.save(request);
    }

    public Page<WalletTransaction> getTransactions(Long userId, Pageable pageable) {
        return walletTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<WithdrawRequest> getWithdrawRecords(Long userId, Pageable pageable) {
        return withdrawRequestRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    // 管理员方法
    public Page<WithdrawRequest> getPendingWithdrawals(Pageable pageable) {
        return withdrawRequestRepository.findByStatusOrderByCreatedAtDesc("pending", pageable);
    }

    @Transactional
    public void approveWithdraw(Long withdrawId) {
        WithdrawRequest request = withdrawRequestRepository.findById(withdrawId)
                .orElseThrow(() -> new BusinessException("提现记录不存在"));

        if (!"pending".equals(request.getStatus())) {
            throw new BusinessException("状态不允许操作");
        }

        request.setStatus("approved");
        request.setProcessedAt(LocalDateTime.now());
        withdrawRequestRepository.save(request);
    }

    @Transactional
    public void rejectWithdraw(Long withdrawId, String reason) {
        WithdrawRequest request = withdrawRequestRepository.findById(withdrawId)
                .orElseThrow(() -> new BusinessException("提现记录不存在"));

        if (!"pending".equals(request.getStatus())) {
            throw new BusinessException("状态不允许操作");
        }

        request.setStatus("rejected");
        request.setRejectReason(reason);
        request.setProcessedAt(LocalDateTime.now());
        withdrawRequestRepository.save(request);

        // 退回金额
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("用户不存在"));
        user.setBalance(user.getBalance().add(request.getAmount()));
        userRepository.save(user);
    }
}
