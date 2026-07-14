package com.playmate.controller;

import com.playmate.common.Result;
import com.playmate.dto.SendMessageRequest;
import com.playmate.entity.ChatMessage;
import com.playmate.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/send")
    public Result<ChatMessage> sendMessage(Authentication authentication, @Valid @RequestBody SendMessageRequest request) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(chatService.sendMessage(userId, request));
    }

    @GetMapping("/messages")
    public Result<List<ChatMessage>> getMessages(Authentication authentication, @RequestParam Long targetUserId) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(chatService.getMessages(userId, targetUserId));
    }

    @PutMapping("/read")
    public Result<Void> markAsRead(Authentication authentication, @RequestParam Long targetUserId) {
        Long userId = (Long) authentication.getPrincipal();
        chatService.markAsRead(userId, targetUserId, userId);
        return Result.success();
    }

    @GetMapping("/unread")
    public Result<Long> getUnreadCount(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(chatService.getUnreadCount(userId));
    }
}
