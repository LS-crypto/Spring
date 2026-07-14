package com.playmate.service;

import com.playmate.dto.SendMessageRequest;
import com.playmate.entity.ChatMessage;
import com.playmate.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessage sendMessage(Long senderId, SendMessageRequest request) {
        ChatMessage message = new ChatMessage();
        message.setConversationId(generateConversationId(senderId, request.getReceiverId()));
        message.setSenderId(senderId);
        message.setReceiverId(request.getReceiverId());
        message.setContent(request.getContent());

        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessages(Long userId1, Long userId2) {
        String conversationId = generateConversationId(userId1, userId2);
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Transactional
    public void markAsRead(Long userId1, Long userId2, Long receiverId) {
        String conversationId = generateConversationId(userId1, userId2);
        chatMessageRepository.markAsRead(conversationId, receiverId);
    }

    public long getUnreadCount(Long userId) {
        return chatMessageRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    // 生成会话ID（小的ID在前，保证唯一性）
    private String generateConversationId(Long userId1, Long userId2) {
        Long min = Math.min(userId1, userId2);
        Long max = Math.max(userId1, userId2);
        return min + "_" + max;
    }
}
