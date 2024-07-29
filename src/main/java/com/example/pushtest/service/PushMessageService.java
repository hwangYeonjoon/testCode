package com.example.pushtest.service;

import com.example.pushtest.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Service
public class PushMessageService {

    @Autowired
    private WebSocketHandler webSocketHandler;

    // 모든 클라이언트에게 메시지 보내기
    public String sendMessageToAll(String message) {
        try {
            Map<String, Set<WebSocketSession>> chatRooms = webSocketHandler.getChatRooms();
            for (Set<WebSocketSession> sessions : chatRooms.values()) {
                for (WebSocketSession session : sessions) {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(message));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to send message";
        }
        return "Message sent to all clients: " + message;
    }

    // 특정 클라이언트에게 메시지 보내기
    public String sendMessageToClient(String clientId, String message) {
        try {
            webSocketHandler.sendMessageToClient(clientId, message);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send message to client " + clientId;
        }
        return "Message sent to client " + clientId + ": " + message;
    }

    // 특정 채팅방에 메시지 보내기
    public String sendMessageToRoom(String roomId, String message) {
        try {
            webSocketHandler.sendMessageToRoom(roomId, message);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to send message to room " + roomId;
        }
        return "Message sent to room " + roomId + ": " + message;
    }
}
