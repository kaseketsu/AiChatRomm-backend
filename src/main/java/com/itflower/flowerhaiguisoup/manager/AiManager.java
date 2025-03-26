package com.itflower.flowerhaiguisoup.manager;

import cn.hutool.core.collection.CollUtil;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChoice;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Ai 调用 SDK 封装
 */
@Service
public class AiManager {

    @Resource
    private ArkService arkService;
    ;

    /**
     * 单次对话调用
     *
     * @param systemPrompt
     * @param userPrompt
     * @return
     */
    public String doChat(String systemPrompt, String userPrompt) {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();
        messages.add(systemMessage);
        messages.add(userMessage);
        return doChat(messages);
    }

    /**
     * 带有历史记录的对话调用
     *
     * @param chatMessageList
     * @return
     */
    public String doChat(List<ChatMessage> chatMessageList) {

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 指定您创建的方舟推理接入点 ID，此处已帮您修改为您的推理接入点 ID
                .model("ep-20250324145047-vs8j9")
                .messages(chatMessageList)
                .build();

        List<ChatCompletionChoice> choices = arkService.createChatCompletion(chatCompletionRequest).getChoices();
        if (CollUtil.isEmpty(choices)) {
            throw new RuntimeException("AI返回结果为空");
        }
        String content = (String) choices.get(0).getMessage().getContent();
        System.out.println("AI返回结果：" + content);
        return content;
    }
}
