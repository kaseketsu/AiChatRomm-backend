package com.itflower.flowerhaiguisoup.controller;

import com.itflower.flowerhaiguisoup.model.ChatRoom;
import com.itflower.flowerhaiguisoup.service.ChatService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/")
public class ChatController {

    @Resource
    private ChatService chatService;

    @PostMapping("/{roomId}/chat")
    public String doChat(@PathVariable Long roomId, @RequestParam String userMessage) {
        return chatService.doChat(roomId, userMessage);
    }

    @GetMapping("/room")
    public List<ChatRoom> getAllRoomMessages() {
        return chatService.getAllRoomMessages();
    }
}
