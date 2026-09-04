package com.example.demo;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {
    
    // 密钥统一从环境变量注入（上传前已脱敏，请配置 DASHSCOPE_API_KEY）
    private static final String API_KEY = System.getenv().getOrDefault("DASHSCOPE_API_KEY", "");
    private static final String UPLOAD_API_URL = "https://dashscope.aliyuncs.com/api/v1/uploads";
    private static final String SYNTHESIS_API_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image2image/image-synthesis";
    private static final String TASK_API_BASE = "https://dashscope.aliyuncs.com/api/v1/tasks/";
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "healthy");
        response.put("message", "AI文创风格迁移系统后端服务正常运行");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        response.put("version", "1.0.0");
        
        System.out.println("AI文创风格迁移系统健康检查被调用");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "AI文创风格迁移系统测试端点工作正常");
        response.put("time", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/generate-image")
    public ResponseEntity<?> generateImage(
            @RequestParam("prompt") String prompt,
            @RequestParam("baseImageFile") MultipartFile baseImageFile,
            @RequestParam("maskImageFile") MultipartFile maskImageFile,
            @RequestParam(value = "size", defaultValue = "1024*1024") String size,
            @RequestParam(value = "style", defaultValue = "<auto>") String style,
            @RequestParam(value = "n", defaultValue = "1") int n) {
        
        String baseImagePath = null;
        String maskImagePath = null;
        String processedMaskPath = null;
        
        try {
            System.out.println("=== AI文创风格迁移系统开始处理请求 ===");
            System.out.println("Prompt: " + prompt);
            System.out.println("Base Image: " + baseImageFile.getOriginalFilename() + " (" + baseImageFile.getSize() + " bytes)");
            System.out.println("Mask Image: " + maskImageFile.getOriginalFilename() + " (" + maskImageFile.getSize() + " bytes)");
            System.out.println("Size: " + size);
            System.out.println("Style: " + style);
            System.out.println("N: " + n);
            
            // 验证提示词长度
            if (prompt.length() > 75) {
                prompt = prompt.substring(0, 75);
                System.out.println("提示词已截断至75字符: " + prompt);
            }
            
            // 验证文件大小
            long maxFileSize = 10 * 1024 * 1024; // API限制10MB
            if (baseImageFile.getSize() > maxFileSize) {
                System.out.println("基础图片文件过大");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("基础图片文件过大，请压缩到10MB以内");
            }
            if (maskImageFile.getSize() > maxFileSize) {
                System.out.println("掩码图片文件过大");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("掩码图片文件过大，请压缩到10MB以内");
            }
            
            // 验证文件格式
            if (!isValidImageFormat(baseImageFile) || !isValidImageFormat(maskImageFile)) {
                System.out.println("文件格式不支持");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("只支持 JPG、PNG、BMP 格式的图片");
            }
            
            // 保存上传的文件到临时文件
            System.out.println("保存上传文件...");
            baseImagePath = saveMultipartFile(baseImageFile);
            maskImagePath = saveMultipartFile(maskImageFile);
            System.out.println("Base image saved to: " + baseImagePath);
            System.out.println("Mask image saved to: " + maskImagePath);
            
            // 验证图像分辨率
            BufferedImage baseImage = ImageIO.read(new File(baseImagePath));
            BufferedImage maskImage = ImageIO.read(new File(maskImagePath));
            
            validateImageResolution(baseImage);
            validateImageResolution(maskImage);
            
            // 处理掩码图像：转换为API期望的格式（黑色背景，白色涂抹区域）
            System.out.println("处理掩码图像...");
            processedMaskPath = processMaskForAPI(maskImagePath);
            System.out.println("Processed mask saved to: " + processedMaskPath);
            
            // 上传到OSS
            System.out.println("上传文件到OSS...");
            String baseImageUrl = uploadFileAndGetUrl(API_KEY, "wanx-x-painting", baseImagePath);
            String maskImageUrl = uploadFileAndGetUrl(API_KEY, "wanx-x-painting", processedMaskPath);
            System.out.println("Base image URL: " + baseImageUrl);
            System.out.println("Mask image URL: " + maskImageUrl);
            
            // 直接进行图像合成
            System.out.println("提交风格迁移任务...");
            List<String> generatedImageUrls = synthesizeImageWithHttp(baseImageUrl, maskImageUrl, prompt, size, style, n);
            System.out.println("风格迁移后的图像URL数量: " + generatedImageUrls.size());
            
            if (!generatedImageUrls.isEmpty()) {
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("imageUrls", generatedImageUrls);
                responseMap.put("promptUsed", prompt);
                responseMap.put("expiryNotice", "生成的图像URL有效期为24小时，请及时保存");
                responseMap.put("expiryHours", 24);
                System.out.println("=== AI风格迁移成功 ===");
                return ResponseEntity.ok(responseMap);
            } else {
                System.out.println("风格迁移失败，未返回图像URL");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("风格迁移失败，请检查提示词和图片内容");
            }
        } catch (Exception e) {
            System.err.println("=== AI风格迁移失败 ===");
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "AI风格迁移失败");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        } finally {
            // 清理临时文件
            cleanupTempFiles(baseImagePath, maskImagePath, processedMaskPath);
        }
    }
    
    /**
     * 验证图片格式
     */
    private boolean isValidImageFormat(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || 
               fileName.endsWith(".png") || fileName.endsWith(".bmp");
    }
    
    /**
     * 验证图像分辨率
     */
    private void validateImageResolution(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        
        if (width < 256 || height < 256) {
            throw new IllegalArgumentException("图像分辨率太小，需大于256×256像素");
        }
        if (width > 4096 || height > 4096) {
            throw new IllegalArgumentException("图像分辨率太大，需小于4096×4096像素");
        }
    }
    
    /**
     * 清理临时文件
     */
    private void cleanupTempFiles(String... filePaths) {
        for (String filePath : filePaths) {
            if (filePath != null) {
                try {
                    File file = new File(filePath);
                    if (file.exists()) {
                        if (file.delete()) {
                            System.out.println("已删除临时文件: " + filePath);
                        } else {
                            System.out.println("无法删除临时文件: " + filePath);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("删除临时文件时出错: " + filePath + ", error: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * 使用HTTP直接调用图像合成API
     */
    private List<String> synthesizeImageWithHttp(String baseImageUrl, String maskImageUrl, 
                                               String prompt, String size, String style, int n) {
        try {
            // 确保size格式正确（使用*而不是x）
            String adjustedSize = size.replace('x', '*');
            
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "wanx-x-painting");
            
            JSONObject input = new JSONObject();
            input.put("prompt", prompt);
            input.put("base_image_url", baseImageUrl);
            input.put("mask_image_url", maskImageUrl);
            requestBody.put("input", input);
            
            JSONObject parameters = new JSONObject();
            parameters.put("size", adjustedSize);
            parameters.put("n", Math.min(Math.max(n, 1), 4)); // 限制1-4张
            
            // 添加风格参数（如果不是默认值）
            if (!"<auto>".equals(style)) {
                parameters.put("style", style);
            }
            
            // 关键修复：指定掩码颜色 - 白色为涂抹区域
            JSONArray maskColor = new JSONArray();
            maskColor.put(new JSONArray().put(255).put(255).put(255)); // 白色涂抹区域
            parameters.put("mask_color", maskColor);
            
            requestBody.put("parameters", parameters);
            
            System.out.println("Request Body: " + requestBody.toString(2));
            
            // 创建HTTP客户端
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(60))
                    .build();
            
            // 创建HTTP请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SYNTHESIS_API_URL))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .header("X-DashScope-Async", "enable")
                    .header("X-DashScope-OssResourceResolve", "enable")
                    .POST(BodyPublishers.ofString(requestBody.toString()))
                    .build();
            
            // 发送请求
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            System.out.println("Response Status: " + response.statusCode());
            System.out.println("Response Body: " + response.body());
            
            if (response.statusCode() == 200) {
                return handleSynthesisResponse(response.body());
            } else {
                throw new RuntimeException("Request failed with status code: " + response.statusCode() + ", response: " + response.body());
            }
            
        } catch (Exception e) {
            System.err.println("HTTP调用风格迁移API失败: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * 处理合成响应
     */
    private List<String> handleSynthesisResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            
            // 检查是否有错误
            if (jsonResponse.has("code") && !jsonResponse.isNull("code")) {
                String errorCode = jsonResponse.getString("code");
                String errorMessage = jsonResponse.optString("message", "Unknown error");
                System.err.println("API返回错误: " + errorCode + " - " + errorMessage);
                return Collections.emptyList();
            }
            
            // 提取task_id
            String taskId = jsonResponse.getJSONObject("output").getString("task_id");
            if (taskId != null) {
                System.out.println("异步任务ID: " + taskId);
                return pollTaskResult(taskId);
            } else {
                throw new RuntimeException("无法获取任务ID");
            }
        } catch (Exception e) {
            System.err.println("处理响应失败: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    /**
     * 轮询异步任务结果
     */
    private List<String> pollTaskResult(String taskId) {
        try {
            String taskUrl = TASK_API_BASE + taskId;
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(60))
                    .build();
            
            // 最多轮询30次，每次间隔3秒
            for (int i = 0; i < 30; i++) {
                System.out.println("轮询风格迁移任务结果，第 " + (i + 1) + " 次尝试...");
                
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(taskUrl))
                        .header("Authorization", "Bearer " + API_KEY)
                        .header("Content-Type", "application/json")
                        .GET()
                        .build();
                
                HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    String responseBody = response.body();
                    System.out.println("轮询响应: " + responseBody);
                    
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String taskStatus = jsonResponse.getJSONObject("output").getString("task_status");
                    
                    if ("SUCCEEDED".equals(taskStatus)) {
                        return extractImageUrlsFromJson(responseBody);
                    } else if ("FAILED".equals(taskStatus) || "CANCELED".equals(taskStatus)) {
                        System.err.println("风格迁移任务执行失败: " + responseBody);
                        return Collections.emptyList();
                    }
                    // 任务还在进行中，等待3秒后继续
                    if (i < 29) {
                        System.out.println("任务状态: " + taskStatus + "，等待3秒后继续轮询...");
                        Thread.sleep(3000);
                    }
                } else {
                    System.err.println("轮询请求失败，状态码: " + response.statusCode());
                    return Collections.emptyList();
                }
            }
            
            System.err.println("轮询超时，风格迁移任务未在预期时间内完成");
            return Collections.emptyList();
            
        } catch (Exception e) {
            System.err.println("轮询风格迁移任务结果失败: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    /**
     * 从JSON响应中提取多个图像URL
     */
    private List<String> extractImageUrlsFromJson(String resultJson) {
        List<String> urls = new ArrayList<>();
        try {
            JSONObject jsonResponse = new JSONObject(resultJson);
            JSONObject output = jsonResponse.getJSONObject("output");
            
            if (output.has("results")) {
                JSONArray results = output.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.getJSONObject(i);
                    if (result.has("url")) {
                        String url = result.getString("url");
                        urls.add(url);
                        System.out.println("提取到风格迁移后的图像URL " + (i + 1) + ": " + url);
                    }
                }
            }
            
            if (urls.isEmpty()) {
                System.err.println("未找到风格迁移后的图像URL，完整响应: " + resultJson);
            }
            
        } catch (Exception e) {
            System.err.println("解析响应JSON失败: " + e.getMessage());
        }
        return urls;
    }
    
    /**
     * 处理掩码图像以适应API要求 - 转换为黑色背景白色涂抹区域
     */
    private String processMaskForAPI(String maskImagePath) throws IOException {
        BufferedImage maskImage = ImageIO.read(new File(maskImagePath));
        BufferedImage processedMask = new BufferedImage(
            maskImage.getWidth(), 
            maskImage.getHeight(), 
            BufferedImage.TYPE_INT_ARGB
        );
        
        // 将用户涂抹的黑色区域转换为白色，背景转为黑色
        for (int y = 0; y < maskImage.getHeight(); y++) {
            for (int x = 0; x < maskImage.getWidth(); x++) {
                int rgb = maskImage.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xff;
                int red = (rgb >> 16) & 0xff;
                int green = (rgb >> 8) & 0xff;
                int blue = rgb & 0xff;
                
                // 如果像素是用户涂抹的（黑色或深色），转换为白色
                if (alpha > 0 && (red < 128 || green < 128 || blue < 128)) {
                    processedMask.setRGB(x, y, 0xFFFFFFFF); // 白色涂抹区域
                } else {
                    processedMask.setRGB(x, y, 0xFF000000); // 黑色背景
                }
            }
        }
        
        String processedPath = System.getProperty("java.io.tmpdir") + File.separator + "api_mask_" + System.currentTimeMillis() + ".png";
        ImageIO.write(processedMask, "PNG", new File(processedPath));
        return processedPath;
    }
    
    /**
     * 保存MultipartFile到临时文件
     */
    private String saveMultipartFile(MultipartFile file) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        // 使用英文文件名避免URL编码问题
        String fileName = System.currentTimeMillis() + "_" + 
                         file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");
        String filePath = tempDir + File.separator + fileName;
        file.transferTo(new File(filePath));
        return filePath;
    }
    
    /**
     * 获取上传策略
     */
    private JSONObject getUploadPolicy(String apiKey, String modelName) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(UPLOAD_API_URL);
            httpGet.addHeader("Authorization", "Bearer " + apiKey);
            httpGet.addHeader("Content-Type", "application/json");

            String query = String.format("action=getPolicy&model=%s", modelName);
            httpGet.setURI(URI.create(UPLOAD_API_URL + "?" + query));

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException("Failed to get upload policy: " +
                            EntityUtils.toString(response.getEntity()));
                }
                String responseBody = EntityUtils.toString(response.getEntity());
                return new JSONObject(responseBody).getJSONObject("data");
            }
        }
    }

    /**
     * 上传文件到OSS
     */
    private String uploadFileToOSS(JSONObject policyData, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        String key = policyData.getString("upload_dir") + "/" + fileName;

        HttpPost httpPost = new HttpPost(policyData.getString("upload_host"));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        builder.addTextBody("OSSAccessKeyId", policyData.getString("oss_access_key_id"));
        builder.addTextBody("Signature", policyData.getString("signature"));
        builder.addTextBody("policy", policyData.getString("policy"));
        builder.addTextBody("x-oss-object-acl", "private");
        builder.addTextBody("x-oss-forbid-overwrite", policyData.getString("x_oss_forbid_overwrite"));
        builder.addTextBody("key", key);
        builder.addTextBody("success_action_status", "200");
        byte[] fileContent = Files.readAllBytes(path);
        builder.addBinaryBody("file", fileContent, ContentType.DEFAULT_BINARY, fileName);

        httpPost.setEntity(builder.build());

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                String errorResponse = EntityUtils.toString(response.getEntity());
                System.err.println("OSS Upload Error: " + errorResponse);
                throw new IOException("Failed to upload file: " + errorResponse);
            }
            // 返回oss://格式的URL
            return "oss://" + key;
        }
    }

    /**
     * 上传文件并获取URL
     */
    public String uploadFileAndGetUrl(String apiKey, String modelName, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("文件不存在: " + filePath);
        }

        JSONObject policyData = getUploadPolicy(apiKey, modelName);
        String url = uploadFileToOSS(policyData, filePath);
        
        LocalDateTime expireTime = LocalDateTime.now().plusHours(48);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println("文件上传成功，有效期为48小时，过期时间: " + expireTime.format(formatter));
        
        return url;
    }
}