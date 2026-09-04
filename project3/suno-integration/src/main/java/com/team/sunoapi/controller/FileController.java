package com.team.sunoapi.controller;

import com.team.sunoapi.service.AlibabaOssService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final AlibabaOssService alibabaOssService;

    // 使用构造器注入服务
    public FileController(AlibabaOssService alibabaOssService) {
        this.alibabaOssService = alibabaOssService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadAudio(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件类型
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.equals("audio/mpeg") && !contentType.equals("audio/wav"))) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "不支持的文件类型，请上传MP3或WAV格式的音频文件");
                return ResponseEntity.badRequest().body(error);
            }

            // 验证文件大小（50MB）
            if (file.getSize() > 50 * 1024 * 1024) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "文件大小不能超过50MB");
                return ResponseEntity.badRequest().body(error);
            }

            // 上传到阿里云OSS
            String uploadUrl = alibabaOssService.uploadFile(file);

            // 返回上传后的URL
            Map<String, String> response = new HashMap<>();
            response.put("uploadUrl", uploadUrl);
            response.put("message", "文件上传成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}