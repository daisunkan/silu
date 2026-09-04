package com.team.sunoapi.dto;

import lombok.Data;
import java.util.List;

@Data
public class MusicStatus {
    private String taskId;
    private String status;
    private List<AudioData> audioData;
    
    @Data
    public static class AudioData {
        private String id;
        private String audioUrl;
        private String title;
        private String[] tags;
        private Double duration;
        // 可以添加其他您需要的字段
    }
}