package com.javaAssigment.chatApp.controller;

import com.javaAssigment.chatApp.model.ChatMessage;
import com.javaAssigment.chatApp.service.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Controller
public class ChatController {

    @Autowired
    HelloWorldService heloHelloWorldService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) throws InterruptedException, ExecutionException {
        CompletableFuture<String> resEn = heloHelloWorldService.GetByLangAPI(chatMessage.getContent(), "en");
        CompletableFuture<String> resFr = heloHelloWorldService.GetByLangAPI(chatMessage.getContent(), "fr");
        CompletableFuture.allOf(resEn,resFr);

        chatMessage.setContent(resEn.get() + " - " + resFr.get());
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}

