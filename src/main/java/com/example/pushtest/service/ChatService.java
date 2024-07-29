package com.example.pushtest.service;

import com.example.pushtest.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private PushMessageService pushMessageService;

    public String sendChatMessage(String senderId, String recipientId, String message) {
        try {
            // 특정 클라이언트에게 채팅 메시지 보내기
            webSocketHandler.sendMessageToClient(recipientId, "From " + senderId + ": " + message);

            // 푸쉬 알림 보내기
            pushMessageService.sendMessageToClient(recipientId, "New message from " + senderId);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send chat message";
        }
        return "Chat message sent to " + recipientId + ": " + message;
    }

    public String sendRoomMessage(String roomId, String senderId, String message) {
        try {
            // 특정 채팅방에 채팅 메시지 보내기
            webSocketHandler.sendMessageToRoom(roomId, senderId + ": " + message);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send room message";
        }
        return "Message sent to room " + roomId + ": " + message;
    }
}
