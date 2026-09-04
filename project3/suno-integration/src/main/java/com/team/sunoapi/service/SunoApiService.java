package com.team.sunoapi.service;

import com.team.sunoapi.dto.GenerateResponse;
import com.team.sunoapi.dto.MusicCoverRequest;
import com.team.sunoapi.dto.MusicGenerateRequest;
import com.team.sunoapi.dto.MusicStatus;
import com.team.sunoapi.dto.StatusResponse;
import com.team.sunoapi.dto.MusicCoverRequest;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SunoApiService {

    @Value("${suno.api.base-url}")
    private String baseUrl;

    @Value("${suno.api.key}")
    private String apiKey;

    @PostConstruct
    public void checkApiKey() {
        System.out.println("===================================");
        System.out.println("🔍 正在初始化 Suno API 客户端");
        System.out.println("🔑 当前配置的 API Key: " + apiKey);
        System.out.println("🔑 Key 长度: " + (apiKey != null ? apiKey.length() : "null"));

        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("❌ 错误：API Key 为空！配置未生效！");
        } else if (!apiKey.startsWith("24b958fe")) {
            System.out.println("⚠️  注意：Key 前缀不是 24b958fe，可能加载了错误的配置！");
        } else {
            System.out.println("✅ 确认：API Key 前缀正确，配置可能已生效。");
        }
        System.out.println("===================================");
    }

    @Value("${suno.api.timeout}")
    private int timeout;

    // 添加代理配置
    @Value("${proxy.host:127.0.0.1}")
    private String proxyHost;

    @Value("${proxy.port:7890}")
    private int proxyPort;

    @Value("${proxy.enabled:false}")
    private boolean proxyEnabled;

    @Value("${suno.api.callback-url}") // 新增：注入回调URL配置
    private String callbackUrl;

    private RestTemplate restTemplate;
    private final RestTemplateBuilder restTemplateBuilder;

    public SunoApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @PostConstruct
    public void init() {
        System.out.println("=== 配置验证 ===");
        System.out.println("代理启用: " + proxyEnabled);
        System.out.println("代理主机: " + proxyHost);
        System.out.println("代理端口: " + proxyPort);
        System.out.println("API基础URL: " + baseUrl);
        System.out.println("API超时: " + timeout);
        System.out.println("API密钥: " + (apiKey != null ? "已设置" : "未设置"));
        System.out.println("=== 配置验证结束 ===");

        // 在依赖注入完成后配置 RestTemplate
        configureRestTemplate();
    }

    private void configureRestTemplate() {
        System.out.println("配置 RestTemplate - 代理启用: " + proxyEnabled + ", 主机: " + proxyHost + ", 端口: " + proxyPort);

        // 创建基础的RestTemplateBuilder
        RestTemplateBuilder builder = restTemplateBuilder
                .setConnectTimeout(Duration.ofMillis(timeout))
                .setReadTimeout(Duration.ofMillis(timeout));

        // 如果启用代理，配置代理
        if (proxyEnabled) {
            System.out.println("正在配置代理...");
            builder = builder.requestFactory(() -> {
                SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                requestFactory.setProxy(proxy);
                System.out.println("代理已设置: " + proxy);
                return requestFactory;
            });
        } else {
            System.out.println("代理未启用");
        }

        this.restTemplate = builder.build();
        System.out.println("RestTemplate 配置完成");
    }

    public String generateMusic(MusicGenerateRequest request) {
        try {
            // 确保使用中文参数
            request.setLanguage("zh");
            request.setCustomMode(true); // 强制使用自定义模式

            // 根据用户选择的风格设置相应的标签和提示词增强
            String style = request.getStyle();
            if (style != null) {
                switch (style) {
                    case "traditional Chinese Guqin":
                        request.setTags("traditional Chinese music, Guqin, ancient instrument, classical Chinese");
                        if (request.getPrompt() != null) {
                            request.setPrompt("古琴风格，" + request.getPrompt());
                        }
                        break;
                    case "traditional Chinese Zheng":
                        request.setTags("traditional Chinese music, Guzheng, Chinese zither, ancient instrument");
                        if (request.getPrompt() != null) {
                            request.setPrompt("古筝风格，" + request.getPrompt());
                        }
                        break;
                    case "traditional Chinese Xiao":
                        request.setTags("traditional Chinese music, Xiao, Chinese flute, ancient instrument");
                        if (request.getPrompt() != null) {
                            request.setPrompt("萧风格，" + request.getPrompt());
                        }
                        break;
                    case "traditional Chinese Dizi":
                        request.setTags("traditional Chinese music, Dizi, Chinese bamboo flute, ancient instrument");
                        if (request.getPrompt() != null) {
                            request.setPrompt("笛子风格，" + request.getPrompt());
                        }
                        break;
                    default:
                        // 默认处理
                        request.setTags("traditional Chinese music");
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept-Language", "zh-CN"); // 添加语言头

            HttpEntity<MusicGenerateRequest> entity = new HttpEntity<>(request, headers);

            System.out.println("=== 生成音乐请求详情 ===");
            System.out.println("URL: " + baseUrl + "/api/v1/generate");
            System.out.println("请求头: " + headers);
            System.out.println("请求体: " + request.toString());

            // 首先获取原始响应字符串
            ResponseEntity<String> rawResponse = restTemplate.exchange(
                    baseUrl + "/api/v1/generate",
                    HttpMethod.POST,
                    entity,
                    String.class);

            System.out.println("=== 生成音乐响应详情 ===");
            System.out.println("HTTP状态码: " + rawResponse.getStatusCode());
            System.out.println("原始响应体: " + rawResponse.getBody());

            // 尝试解析为GenerateResponse
            try {
                ResponseEntity<GenerateResponse> response = restTemplate.exchange(
                        baseUrl + "/api/v1/generate",
                        HttpMethod.POST,
                        entity,
                        GenerateResponse.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    GenerateResponse body = response.getBody();
                    if (body != null && body.getData() != null) {
                        return body.getData().getTaskId();
                    } else {
                        throw new RuntimeException("音乐生成请求成功，但响应体为空或格式不正确");
                    }
                } else {
                    throw new RuntimeException("音乐生成请求失败: " + response.getStatusCode());
                }
            } catch (Exception e) {
                System.out.println("解析响应时出错: " + e.getMessage());
                throw new RuntimeException("无法解析API响应: " + e.getMessage() + "。原始响应: " + rawResponse.getBody(), e);
            }
        } catch (Exception e) {
            System.out.println("调用Suno API时发生异常: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("调用Suno API失败: " + e.getMessage(), e);
        }
    }

    public MusicStatus checkStatus(String taskId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<StatusResponse> response = restTemplate.exchange(
                    baseUrl + "/api/v1/generate/record-info?taskId=" + taskId,
                    HttpMethod.GET,
                    entity,
                    StatusResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                StatusResponse body = response.getBody();
                if (body != null && body.getData() != null) {
                    StatusResponse.SunoApiResponseData apiData = body.getData();
                    MusicStatus result = new MusicStatus();
                    result.setTaskId(apiData.getTaskId());
                    result.setStatus(apiData.getStatus());

                    // 转换音频数据
                    if (apiData.getResponse() != null && apiData.getResponse().getSunoData() != null) {
                        List<MusicStatus.AudioData> audioList = apiData.getResponse().getSunoData().stream()
                                .map(sunoAudio -> {
                                    MusicStatus.AudioData audio = new MusicStatus.AudioData();
                                    audio.setId(sunoAudio.getId());
                                    audio.setAudioUrl(sunoAudio.getAudioUrl());
                                    audio.setTitle(sunoAudio.getTitle());
                                    audio.setTags(sunoAudio.getTags() != null ? sunoAudio.getTags().split(", ")
                                            : new String[0]);
                                    audio.setDuration(sunoAudio.getDuration());
                                    return audio;
                                })
                                .collect(Collectors.toList());
                        result.setAudioData(audioList);
                    }

                    return result;
                } else {
                    throw new RuntimeException("状态查询成功，但响应体为空");
                }
            } else {
                throw new RuntimeException("状态查询失败: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("查询任务状态失败: " + e.getMessage(), e);
        }

    }

    // 新增：上传音频转换风格
    public String generateCover(MusicCoverRequest request) {
        try {
            System.out.println("=== 音频转换请求详情 ===");
            System.out.println("请求对象: " + request.toString());
            System.out.println("URL: " + baseUrl + "/api/v1/generate/upload-cover");

            // 强制设置为纯音乐模式
            request.setInstrumental(true);

            // 如果是纯音乐模式，清空提示词
            if (request.isInstrumental()) {
                request.setPrompt("");

                // 为纯音乐模式优化参数
                if (request.getStyle() != null) {
                    // 根据风格设置更合适的标签
                    switch (request.getStyle()) {
                        case "traditional Chinese Guqin":
                            request.setTags(
                                    "traditional Chinese music, Guqin, instrumental, ancient instrument, classical Chinese, no vocals");
                            break;
                        case "traditional Chinese Zheng":
                            request.setTags(
                                    "traditional Chinese music, Guzheng, instrumental, Chinese zither, ancient instrument, no vocals");
                            break;
                        case "traditional Chinese Xiao":
                            request.setTags(
                                    "traditional Chinese music, Xiao, instrumental, Chinese flute, ancient instrument, no vocals");
                            break;
                        case "traditional Chinese Dizi":
                            request.setTags(
                                    "traditional Chinese music, Dizi, instrumental, Chinese bamboo flute, ancient instrument, no vocals");
                            break;
                        default:
                            request.setTags("instrumental, no vocals, " + request.getStyle());
                    }
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<MusicCoverRequest> entity = new HttpEntity<>(request, headers);

            // 调用Suno API的上传翻唱接口
            ResponseEntity<GenerateResponse> response = restTemplate.exchange(
                    baseUrl + "/api/v1/generate/upload-cover",
                    HttpMethod.POST,
                    entity,
                    GenerateResponse.class);

            System.out.println("=== 音频转换响应 ===");
            System.out.println("HTTP状态: " + response.getStatusCode());
            System.out.println("响应体: " + (response.getBody() != null ? response.getBody().toString() : "null"));

            if (response.getStatusCode().is2xxSuccessful()) {
                GenerateResponse body = response.getBody();
                if (body != null && body.getData() != null) {
                    return body.getData().getTaskId();
                } else {
                    throw new RuntimeException("音频转换请求成功，但响应格式不正确");
                }
            } else {
                String errorBody = response.getBody() != null ? response.getBody().toString() : "无响应体";
                throw new RuntimeException("音频转换请求失败: " + response.getStatusCode() + ", 响应: " + errorBody);
            }
        } catch (Exception e) {
            System.out.println("调用音频转换API失败: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("调用音频转换API失败: " + e.getMessage(), e);
        }
    }
}