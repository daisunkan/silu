package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api")
public class ChatController {
    // 维护对话历史
    private Map<String, List<Message>> conversationHistory = new ConcurrentHashMap<>();

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            // 验证请求参数
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return new ChatResponse("错误：消息内容不能为空");
            }
            if (request.getSessionId() == null || request.getSessionId().trim().isEmpty()) {
                return new ChatResponse("错误：会话ID不能为空");
            }
            
            // 获取或创建对话历史
            List<Message> messages = conversationHistory.computeIfAbsent(
                request.getSessionId(), 
                k -> new ArrayList<>()
            );
            
            // 如果是新会话，添加系统消息
            if (messages.isEmpty()) {
                messages.add(createMessage(Role.SYSTEM, "你是一个精通中华文化的专家，回答问题时精简而有条理。"));
            }
            
            // 添加用户消息
            messages.add(createMessage(Role.USER, request.getMessage()));

            // 验证API密钥
            String apiKey = System.getenv("DASHSCOPE_API_KEY");
            if (apiKey == null || apiKey.trim().isEmpty()) {
                return new ChatResponse("错误：未配置API密钥");
            }

            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-flash")
                    .messages(messages)
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();

            Generation gen = new Generation();
            GenerationResult result = gen.call(param);
            
            // 验证模型响应
            if (result == null || result.getOutput() == null || 
                result.getOutput().getChoices() == null || 
                result.getOutput().getChoices().isEmpty()) {
                return new ChatResponse("错误：模型返回无效响应");
            }
            
            // 添加AI回复到对话历史
            Message aiMessage = result.getOutput().getChoices().get(0).getMessage();
            messages.add(aiMessage);
            
            return new ChatResponse(aiMessage.getContent());
            
        } catch (Exception e) {
            // 捕获所有异常并记录日志
            System.err.println("API调用出错: " + e.getMessage());
            e.printStackTrace();
            return new ChatResponse("系统错误: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    @PostMapping("/clear")
    public void clearHistory(@RequestBody ChatRequest request) {
        conversationHistory.remove(request.getSessionId());
    }

    private Message createMessage(Role role, String content) {
        return Message.builder().role(role.getValue()).content(content).build();
    }

    static class ChatRequest {
        private String message;
        private String sessionId;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }

    static class ChatResponse {
        private String response;

        public ChatResponse(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}
