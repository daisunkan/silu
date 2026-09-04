package com.team.sunoapi.dto;

import lombok.Data;

@Data
public class MusicCoverRequest {
    private String uploadUrl;
    private boolean customMode = true; // 默认启用自定义模式
    private boolean instrumental = true; // 默认设置为纯音乐
    private String model = "V4"; // 默认使用V4模型
    private String callBackUrl;
    private String prompt = ""; // 默认为空字符串
    private String style;
    private String title;
    private String negativeTags;
    private String vocalGender;
    private Double styleWeight;
    private Double weirdnessConstraint;
    private Double audioWeight;
    private String tags;

    @Override
    public String toString() {
        return "MusicCoverRequest{" +
                "uploadUrl='" + uploadUrl + '\'' +
                ", customMode=" + customMode +
                ", instrumental=" + instrumental +
                ", model='" + model + '\'' +
                ", callBackUrl='" + callBackUrl + '\'' +
                ", prompt='" + prompt + '\'' +
                ", style='" + style + '\'' +
                ", title='" + title + '\'' +
                ", negativeTags='" + negativeTags + '\'' +
                ", vocalGender='" + vocalGender + '\'' +
                ", styleWeight=" + styleWeight +
                ", weirdnessConstraint=" + weirdnessConstraint +
                ", audioWeight=" + audioWeight +
                ", tags='" + tags + '\'' +
                '}';
    }
}
