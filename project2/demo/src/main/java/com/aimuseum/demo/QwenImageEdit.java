package com.aimuseum.demo;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.dashscope.utils.JsonUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/image")
public class QwenImageEdit {

    // 若没有配置环境变量，请用百炼API Key将下行替换为：apiKey="sk-xxx"
    private static String apiKey = System.getenv("DASHSCOPE_API_KEY");

    @PostMapping("/edit")
    public String editImage(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("prompt") String prompt,
            @RequestParam(value = "negative_prompt", required = false) String negativePrompt) 
            throws ApiException, NoApiKeyException, UploadFileException, IOException {

        String image = encodeMultipartFile(imageFile);

        MultiModalConversation conv = new MultiModalConversation();

        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(
                        Collections.singletonMap("image", image),
                        Collections.singletonMap("text", prompt)
                )).build();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("watermark", false);
        if (negativePrompt != null && !negativePrompt.isEmpty()) {
            parameters.put("negative_prompt", negativePrompt);
        }

        MultiModalConversationParam param = MultiModalConversationParam.builder()
                .apiKey(apiKey)
                .model("qwen-image-edit")
                .messages(Collections.singletonList(userMessage))
                .parameters(parameters)
                .build();

        MultiModalConversationResult result = conv.call(param);
        return JsonUtils.toJson(result);
    }

    /**
     * 将MultipartFile编码为Base64字符串
     * @param file MultipartFile对象
     * @return Base64字符串，格式为 data:{MIME_type};base64,{base64_data}
     */
    private static String encodeMultipartFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }
        
        String mimeType = file.getContentType();
        if (mimeType == null || !mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("不支持或无法识别的图像格式");
        }
        
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new IllegalArgumentException("无法读取文件内容");
        }
        
        String encodedString = Base64.getEncoder().encodeToString(fileBytes);
        return "data:" + mimeType + ";base64," + encodedString;
    }
}