package com.team.sunoapi.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class AlibabaOssService {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint; // 例如: oss-cn-hangzhou.aliyuncs.com

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws Exception {
        // 1. 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 2. 生成唯一的文件名，避免覆盖
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = "suno/" + UUID.randomUUID().toString() + fileExtension; // 加个suno/目录便于管理

        // 3. 上传文件流
        ossClient.putObject(new PutObjectRequest(bucketName, fileName, new ByteArrayInputStream(file.getBytes())));

        // 4. 关闭OSSClient
        ossClient.shutdown();

        // 5. 拼接文件的公开访问URL
        // 格式: https://{Bucket名称}.{外网Endpoint}/{文件路径}
        return "https://" + bucketName + "." + endpoint + "/" + fileName;
    }
}