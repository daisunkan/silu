package com.team.sunoapi.dto;

import lombok.Data;

@Data
public class GenerateResponse {
    private Integer code;
    private String msg;
    private GenerateData data;

    @Data
    public static class GenerateData {
        private String taskId;
    }
}