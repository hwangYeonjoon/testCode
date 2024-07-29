package com.example.pushtest.controller;

import com.example.pushtest.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/chat/send")
    public String sendChatMessage(@RequestParam String senderId, @RequestParam String recipientId, @RequestParam String message) {
        return chatService.sendChatMessage(senderId, recipientId, message);
    }

    @PostMapping("/chat/sendToRoom")
    public String sendRoomMessage(@RequestParam String roomId, @RequestParam String senderId, @RequestParam String message) {
        return chatService.sendRoomMessage(roomId, senderId, message);
    }
}
