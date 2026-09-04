package com.team.sunoapi.dto;

import lombok.Data;

@Data
public class MusicGenerateRequest {
    private String prompt;         // 提示词/歌词
    private Boolean customMode = true;  // 改为true，启用自定义模式
    private Boolean instrumental = false;
    private String model = "V3_5";
    private String style;          // 音乐风格
    private String title;
    private String callBackUrl;
    private String language = "zh"; // 明确指定中文
    private String tags; // 风格标签
    
    @Override
    public String toString() {
        return "MusicGenerateRequest{" +
            "prompt='" + prompt + '\'' +
            ", customMode=" + customMode +
            ", instrumental=" + instrumental +
            ", model='" + model + '\'' +
            ", style='" + style + '\'' +
            ", title='" + title + '\'' +
            ", callBackUrl='" + callBackUrl + '\'' +
            ", language='" + language + '\'' +
            ", tags='" + tags + '\'' +
            '}';
    }
}