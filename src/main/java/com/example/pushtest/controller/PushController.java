package com.example.pushtest.controller;

import com.example.pushtest.service.PushMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushController {

    @Autowired
    private PushMessageService pushMessageService;

    @GetMapping("/push")
    public String pushMessage(@RequestParam String message) {
        return pushMessageService.sendMessage(message);
    }
}
