package com.itflower.flowerhaiguisoup.service;

import com.itflower.flowerhaiguisoup.model.ChatRoom;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;

import java.util.List;
import java.util.Map;

/**
 * 聊天服务
 */
public interface ChatService {

    /**
     * 聊天
     *
     * @param userMessage 用户消息
     * @return
     */
    String doChat(Long roomId, String userMessage);

    /**
     * 获取聊天室消息
     *
     * @return
     */
    List<ChatRoom> getAllRoomMessages();
}
