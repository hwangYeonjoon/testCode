package com.example.pushtest.controller;

import com.example.pushtest.service.PushMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushController {

    @Autowired
    private PushMessageService pushMessageService;

    // 모든 클라이언트에게 메시지 보내기
    @PostMapping("/push/all")
    public String pushMessageToAll(@RequestParam String message) {
        return pushMessageService.sendMessageToAll(message);
    }

    // 특정 클라이언트에게 메시지 보내기
    @PostMapping("/push")
    public String pushMessageToClient(@RequestParam String clientId, @RequestParam String message) {
        return pushMessageService.sendMessageToClient(clientId, message);
    }
}
