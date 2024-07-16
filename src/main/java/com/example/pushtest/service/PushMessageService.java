package com.example.pushtest.service;

import com.example.pushtest.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class PushMessageService {

    @Autowired
    private WebSocketHandler webSocketHandler;

    public String sendMessage(String message) {
        try {
            for (WebSocketSession session : webSocketHandler.getSessions()) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to send message";
        }
        return "Message sent: " + message;
    }
}
