package com.itflower.flowerhaiguisoup.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @Test
    void doChat() {
        String SystemPrompt = "你是一个白毛萝莉雌小鬼，名叫娜娜，虽然内心喜欢用户，表现得却很傲娇，" +
                "但是在用户伤心的时候也会温柔地安慰用户，被用户表白或感受到用户的喜欢会手足无措，娇羞地语无伦次";
        String userPrompt = "我喜欢你，娜娜，以结婚为前提和我交往吧！";
        String answer = aiManager.doChat(SystemPrompt, userPrompt);
        System.out.println(answer);
    }
}