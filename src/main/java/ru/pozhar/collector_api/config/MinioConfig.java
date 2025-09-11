package ru.pozhar.collector_api.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(
            @Value("${SPRING_MINIO_URL}") String url,
            @Value("${SPRING_MINIO_ACCESS_KEY}") String accessKey,
            @Value("${SPRING_MINIO_SECRET_KEY}") String secretKey) {

        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();
    }
}
