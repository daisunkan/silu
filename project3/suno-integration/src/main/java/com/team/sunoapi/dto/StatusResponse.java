package com.team.sunoapi.dto;
import java.util.List;
import lombok.Data;

@Data
public class StatusResponse {
    private Integer code;
    private String msg;
    private SunoApiResponseData data; // 修改字段名以更准确反映内容
    
    @Data
    public static class SunoApiResponseData {
        private String taskId;
        private String status;
        private SunoResponse response;
        private String type;
        private Long createTime;
        
        @Data
        public static class SunoResponse {
            private String taskId;
            private List<SunoAudio> sunoData; // 注意这里改成了 sunoData
            
            @Data
            public static class SunoAudio {
                private String id;
                private String audioUrl;
                private String sourceAudioUrl;
                private String streamAudioUrl;
                private String sourceStreamAudioUrl;
                private String imageUrl;
                private String sourceImageUrl;
                private String prompt;
                private String modelName;
                private String title;
                private String tags;
                private Long createTime;
                private Double duration;
            }
        }
    }
}