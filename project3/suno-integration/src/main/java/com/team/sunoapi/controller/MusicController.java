package com.team.sunoapi.controller;

import com.team.sunoapi.dto.GenerateResponse;
import com.team.sunoapi.dto.MusicCoverRequest;
import com.team.sunoapi.dto.MusicGenerateRequest;
import com.team.sunoapi.dto.MusicStatus;
import com.team.sunoapi.dto.MusicCoverRequest;
import com.team.sunoapi.service.SunoApiService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    private final SunoApiService sunoApiService;

    // 构造方法注入Service
    public MusicController(SunoApiService sunoApiService) {
        this.sunoApiService = sunoApiService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateMusic(@RequestBody MusicGenerateRequest request) {
        try {
            // 1. 调用Service生成音乐，拿到任务ID
            String taskId = sunoApiService.generateMusic(request);

            // 2. 构建返回给客户端的响应
            Map<String, String> response = new HashMap<>();
            response.put("taskId", taskId);
            response.put("message", "音乐生成任务已提交");
            response.put("statusUrl", "http://localhost:8080/api/music/status?taskId=" + taskId);

            // 3. 返回HTTP 202 Accepted，表示请求已接受正在处理
            return ResponseEntity.accepted().body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "生成失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<MusicStatus> getStatus(@RequestParam String taskId) {
        // 查询任务状态并返回
        MusicStatus status = sunoApiService.checkStatus(taskId);
        return ResponseEntity.ok(status);
    }

    // 回调接口（可选，Suno API生成完成后会通知这个接口）
    @PostMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestBody Map<String, Object> payload) {
        System.out.println("收到Suno的回调通知: " + payload);
        // 这里你可以写处理回调的逻辑，比如更新数据库
        return ResponseEntity.ok("Callback received!");
    }

    // 新增：上传音频转换风格的接口
    @PostMapping("/upload-cover")
    public ResponseEntity<Map<String, String>> uploadAndCover(@RequestBody MusicCoverRequest request) {
        try {
            // 验证必要参数
            if (request.getUploadUrl() == null || request.getUploadUrl().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "缺少必要参数: uploadUrl");
                return ResponseEntity.badRequest().body(error);
            }

            // 调用Service处理音频转换，获取任务ID
            String taskId = sunoApiService.generateCover(request);

            // 构建返回结果（包含任务ID和查询状态的链接）
            Map<String, String> response = new HashMap<>();
            response.put("taskId", taskId);
            response.put("message", "音频风格转换任务已提交");
            response.put("statusUrl", "http://localhost:8080/api/music/status?taskId=" + taskId);

            return ResponseEntity.accepted().body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "音频转换失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}