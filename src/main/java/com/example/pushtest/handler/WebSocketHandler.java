package com.example.pushtest.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    // 채팅방별로 세션을 관리
    private final Map<String, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();
    private final Map<String, String> sessionIdToRoomId = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        if (payload.startsWith("JOIN:")) {
            String roomId = payload.substring(5);
            joinRoom(session, roomId);
        } else {
            // 메시지 형식이 "clientId:message" 형식이라고 가정
            String[] parts = payload.split(":", 2);
            if (parts.length == 2) {
                String clientId = parts[0];
                String clientMessage = parts[1];
                performSpecificEvent(session, clientMessage, clientId);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = sessionIdToRoomId.remove(session.getId());
        if (roomId != null) {
            Set<WebSocketSession> sessions = chatRooms.get(roomId);
            sessions.remove(session);
            if (sessions.isEmpty()) {
                chatRooms.remove(roomId);
            }
        }
        System.out.println("Connection closed: " + session.getId());
    }

    private void joinRoom(WebSocketSession session, String roomId) {
        chatRooms.computeIfAbsent(roomId, k -> new CopyOnWriteArraySet<>()).add(session);
        sessionIdToRoomId.put(session.getId(), roomId);
        System.out.println("Client " + session.getId() + " joined room: " + roomId);
    }

    public void sendMessageToRoom(String roomId, String message) throws Exception {
        Set<WebSocketSession> sessions = chatRooms.get(roomId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        } else {
            System.out.println("Room does not exist: " + roomId);
        }
    }

    private void performSpecificEvent(WebSocketSession session, String message, String clientId) throws Exception {
        String roomId = sessionIdToRoomId.get(session.getId());
        if (roomId != null) {
            System.out.println("Performing specific event for client " + clientId + " in room " + roomId + ": " + message);
            // 특정 채팅방에 메시지 보내기
            sendMessageToRoom(roomId, clientId + ": " + message);
        }
    }

    public void sendMessageToClient(String clientId, String message) throws Exception {
        for (WebSocketSession session : chatRooms.values().stream().flatMap(Set::stream).toList()) {
            if (session.getId().equals(clientId) && session.isOpen()) {
                session.sendMessage(new TextMessage(message));
                break;
            }
        }
    }

    public Map<String, Set<WebSocketSession>> getChatRooms() {
        return chatRooms;
    }
}
