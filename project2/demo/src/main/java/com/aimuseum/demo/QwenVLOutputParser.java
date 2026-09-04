package com.aimuseum.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QwenVLOutputParser {
    
    /**
     * 解析Qwen-VL模型的输出，提取三个部分的内容
     * @param output 模型输出的完整文本
     * @return 包含三个部分内容的OutputParts对象
     */
    public static OutputParts parseOutput(String output) {
        OutputParts parts = new OutputParts();
        
        // 使用正则表达式提取三个部分，只匹配关键词后的内容
        Pattern pattern = Pattern.compile(
            "思考：(.*?)提示词：(.*?)负面提示词：(.*)",
            Pattern.DOTALL
        );
        
        Matcher matcher = pattern.matcher(output);
        
        if (matcher.find() && matcher.groupCount() >= 3) {
            parts.thinkingPart = matcher.group(1).trim();
            parts.positivePromptPart = matcher.group(2).trim();
            parts.negativePromptPart = matcher.group(3).trim();
        } else {
            // 如果正则匹配失败，尝试其他方法
            extractPartsManually(output, parts);
        }
        
        return parts;
    }
    
    /**
     * 手动提取三个部分的内容（备用方法）
     * @param output 模型输出的完整文本
     * @param parts 用于存储提取结果的OutputParts对象
     */
    private static void extractPartsManually(String output, OutputParts parts) {
        // 查找关键词位置
        int thinkingIndex = output.indexOf("思考：");
        int promptIndex = output.indexOf("提示词：");
        int negativeIndex = output.indexOf("负面提示词：");
        
        // 提取思考部分
        if (thinkingIndex >= 0 && promptIndex > thinkingIndex) {
            parts.thinkingPart = output.substring(thinkingIndex + 3, promptIndex).trim();
        } else if (thinkingIndex >= 0) {
            parts.thinkingPart = output.substring(thinkingIndex + 3).trim();
        }
        
        // 提取正面提示词部分
        if (promptIndex >= 0 && negativeIndex > promptIndex) {
            parts.positivePromptPart = output.substring(promptIndex + 4, negativeIndex).trim();
        } else if (promptIndex >= 0) {
            parts.positivePromptPart = output.substring(promptIndex + 4).trim();
        }
        
        // 提取负面提示词部分
        if (negativeIndex >= 0) {
            parts.negativePromptPart = output.substring(negativeIndex + 5).trim();
        }
    }
    
    /**
     * 内部类，用于存储解析后的输出部分
     */
    public static class OutputParts {
        public String thinkingPart;        // 思考部分
        public String positivePromptPart;  // 正面提示词部分
        public String negativePromptPart;  // 负面提示词部分
        
        @Override
        public String toString() {
            return "思考部分:\n" + thinkingPart + 
                   "\n\n正面提示词部分:\n" + positivePromptPart +
                   "\n\n负面提示词部分:\n" + negativePromptPart;
        }
        
        /**
         * 获取格式化后的正面提示词（用于后端模型）
         * @return 格式化后的正面提示词
         */
        public String getFormattedPositivePrompts() {
            return formatPrompts(positivePromptPart);
        }
        
        /**
         * 获取格式化后的负面提示词（用于后端模型）
         * @return 格式化后的负面提示词
         */
        public String getFormattedNegativePrompts() {
            return formatPrompts(negativePromptPart);
        }
        
        /**
         * 格式化提示词（如果需要特定格式）
         * @param prompts 原始提示词
         * @return 格式化后的提示词
         */
        private String formatPrompts(String prompts) {
            // 这里可以根据后端模型的需要进行格式化
            // 例如：移除句号、添加特定前缀等
            return prompts.replaceAll("\\.$", ""); // 移除末尾的句号
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例输出（模拟Qwen-VL的新输出格式）
        String exampleOutput = "思考：用户希望修复一幅壁画，壁画上有明显的裂痕和剥落。为了保证图像原有的清晰度、自然性和完整性，需要进行细致的修复工作。" +
                              "提示词：修复壁画上的裂痕和剥落，保持原有风格和细节，恢复画面的完整性和美观性。" +
                              "负面提示词：避免过度修饰，确保修复后的图像与原图风格一致，不丢失原有的内容和细节。";
        
        // 解析输出
        OutputParts parts = parseOutput(exampleOutput);
        System.out.println(parts);
    }
}