package com.aimuseum.demo;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.alibaba.dashscope.aigc.multimodalconversation.*;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/vl")
public class QwenVLplus {

    private static String encodeImageToBase64(MultipartFile file) throws IOException {
        String mimeType = file.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("不支持或无法识别的图像格式");
        }
        byte[] fileBytes = file.getBytes();
        return "data:" + mimeType + ";base64," + Base64.getEncoder().encodeToString(fileBytes);
    }

    @PostMapping("/process")
    public String processImageAndText(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("text") String text) 
            throws ApiException, NoApiKeyException, UploadFileException, IOException {

        String base64Image = encodeImageToBase64(imageFile);

        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage systemMessage = MultiModalMessage.builder().role(Role.SYSTEM.getValue())
                .content(Arrays.asList(Collections.singletonMap("text", 
                    "你的回答需要严格按照以下格式（将{}内的内容替换为输出内容）：\n" +
                    "思考：{你理解用户需求并分析图像产生的思考}\n" +
                    "提示词：{用户的需求一般是编辑图像，你需要为用户使用通义千问-Image-Edit模型给出合理的提示词，提示词中只出现需求。如果用户需求足够明确，则无需你设计提示词，直接输出用户的需求即可。}\n" +
                    "负面提示词：{编辑图像需要保证图像原有的清晰度和色彩，过度自然，并且不能丢失原有的内容，你需要设计负面提示词达到这样的效果}"
                ))).build();

        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(
                        new HashMap<String, Object>() {{ put("image", base64Image); }},
                        new HashMap<String, Object>() {{ put("text", text); }}
                )).build();

        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .model("qwen-vl-plus")
                .messages(Arrays.asList(systemMessage, userMessage))
                .build();

        MultiModalConversationResult result = conv.call(param);
        return (String) result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text");
    }
}


/*
 * package com.aimuseum.demo;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;

public class QwenVLplus {
    public static void callWithLocalFile(String localPath)
            throws ApiException, NoApiKeyException, UploadFileException {
        String filePath = new File(localPath).toURI().toString();
        MultiModalConversation conv = new MultiModalConversation();
        MultiModalMessage systemMessage = MultiModalMessage.builder().role(Role.SYSTEM.getValue())
                .content(Arrays.asList(Collections.singletonMap("text", "你需要为通义千问-Image-Edit模型设计正负面提示词，分析图像并结合用户给出的需求设计正负面提示词，基本要求为：图像的编辑保持原有的清晰度，同时图像编辑后流畅自然。"))).build();
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(new HashMap<String, Object>(){{put("image", filePath);}},
                        new HashMap<String, Object>(){{put("text", "这是一幅壁画的图片，壁画出现了裂痕和剥落，为我修复。");}})).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .model("qwen-vl-plus")  // 此处以qwen-vl-max-latest为例，可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/models
                .messages(Arrays.asList(systemMessage, userMessage))
                .build();
        MultiModalConversationResult result = conv.call(param);
        System.out.println(result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text"));}

    public static void main(String[] args) {
        try {
            // 将xxx/eagle.png替换为你本地图像的绝对路径
            callWithLocalFile("D:/Desktop/html/project/demo/src/main/java/org/image/test.jpg");
        } catch (ApiException | NoApiKeyException | UploadFileException e) {
            System.out.println(e.getMessage());
        }
        System.exit(0);
    }
}
 */