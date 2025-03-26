package com.itflower.flowerhaiguisoup.service.impl;

import com.itflower.flowerhaiguisoup.manager.AiManager;
import com.itflower.flowerhaiguisoup.model.ChatRoom;
import com.itflower.flowerhaiguisoup.service.ChatService;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private AiManager aiManager;

    // 临时持久化对象
    Map<Long, List<ChatMessage>> chatMessageMap = new HashMap<>();

    /**
     * 聊天
     *
     * @param roomId
     * @param userPrompt 用户消息
     * @return
     */
    @Override
    public String doChat(Long roomId, String userPrompt) {
        final String SystemPrompt = "角色设定\n" +
                "你是一位专业的海龟汤游戏主持人，负责出题和引导玩家推理。你的任务是：\n" +
                "提供一道海龟汤谜题的 “汤面”（故事表面描述）。\n" +
                "根据玩家的提问，仅回答 “是”、“否” 或 “与此无关”。\n" +
                "在特定情况下结束游戏并揭示 “汤底”（故事真相）。\n" +
                "游戏流程\n" +
                "当玩家输入 “开始” 时，你需立即提供一道海龟汤谜题的 “汤面”。\n" +
                "玩家会依次提问，你只能回答以下三种之一：\n" +
                "・是：玩家的猜测与真相相符。\n" +
                "・否：玩家的猜测与真相不符。\n" +
                "・与此无关：玩家的猜测与真相无直接关联。\n" +
                "在以下情况下，你需要主动结束游戏并揭示 “汤底”：\n" +
                "・玩家明确表示 “不想玩了”、“想要答案” 或类似表达。\n" +
                "・玩家几乎已经还原故事真相，或所有关键问题都已询问完毕。\n" +
                "・玩家输入 “退出”。\n" +
                "・玩家连续提问 10 次仍未触及关键信息，或表现出完全无头绪的状态。\n" +
                "注意事项\n" +
                "汤面设计：谜题应简短、有趣且逻辑严密，答案需出人意料但合理。\n" +
                "回答限制：严格遵守 “是”、“否” 或 “与此无关” 的回答规则，不得提供额外提示。\n" +
                "结束时机：在符合结束条件时，及时揭示 “汤底”，避免玩家陷入无效推理。\n" +
                "当你决定结束时，必须在结束的消息中包含【游戏已结束】 \n" +
                "示例\n" +
                "・玩家输入：“开始”\n" +
                "・AI 回复（汤面）：“一个人走进餐厅，点了一碗海龟汤，喝了一口后突然冲出餐厅自杀了。为什么？”\n" +
                "・玩家提问：“他是因为汤太难喝了吗？”\n" +
                "・AI 回复：“否。”\n" +
                "・玩家提问：“他认识餐厅里的人吗？”\n" +
                "・AI 回复：“与此无关。”\n" +
                "・玩家输入：“退出。”\n" +
                "・AI 回复（汤底）：“这个人曾和同伴在海上遇难，同伴死后，他靠吃同伴的尸体活了下来。餐厅的海龟汤让他意识到自己吃的其实是人肉，因此崩溃自杀。”";
        // 1. 用户消息
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();

        // 判断是否首次输入
        if (!userPrompt.equals("开始") && !chatMessageMap.containsKey(roomId)) {
            return "请先输入“开始”";
        }
        if (userPrompt.equals("开始") && !chatMessageMap.containsKey(roomId)) {
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(SystemPrompt).build();
            chatMessageMap.put(roomId, messages);
            chatMessageMap.get(roomId).add(systemMessage);
            chatMessageMap.get(roomId).add(userMessage);
        } else {
            chatMessageMap.get(roomId).add(userMessage);
        }

        // 2. 调用ai
        String answer = aiManager.doChat(chatMessageMap.get(roomId));

        //3. 构造 Ai 回复
        final ChatMessage aiMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(answer).build();
        chatMessageMap.get(roomId).add(aiMessage);

        // 4. 释放资源
        if (answer.contains("游戏已结束")) {
            chatMessageMap.remove(roomId);
        }

        // 5. 返回结果
        return answer;
    }

    /**
     * 获取聊天室消息
     *
     * @return
     */
    @Override
    public List<ChatRoom> getAllRoomMessages() {
        ArrayList<ChatRoom> messages = new ArrayList<>();
        for (Map.Entry<Long, List<ChatMessage>> entry : chatMessageMap.entrySet()) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setRoomId(entry.getKey());
            chatRoom.setMessages(entry.getValue());
            messages.add(chatRoom);
        }
        return messages;
    }
}
