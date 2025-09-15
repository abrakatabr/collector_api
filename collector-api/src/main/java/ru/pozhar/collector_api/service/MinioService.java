package ru.pozhar.collector_api.service;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.webresources.FileResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.collector_api.exception.MinioException;

@Service
@RequiredArgsConstructor
public class MinioService {
    @Value("${SPRING_MINIO_BUCKET}")
    private String BUCKET;

    private final MinioClient minioClient;

    @PostConstruct
    public void init() {
        createBucketIfNotExists(BUCKET);
    }

    public void createBucketIfNotExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception ex) {
            throw new MinioException("Не удалось создать бакет в minIO");
        }
    }

    public String uploadFile(MultipartFile file, String path, String fileName) {
        try {
            String fullPath = path + "/" + fileName;
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET)
                    .object(fullPath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return fullPath;
        } catch (Exception ex) {
            throw new MinioException("Ошибка при загрузке файла");
        }
    }



    public StatObjectResponse downloadMetadata(String path, String fileName) {
        String fullPath = path + "/" + fileName;
        try {
            StatObjectResponse metaData = minioClient.statObject(
              StatObjectArgs.builder()
                      .bucket(BUCKET)
                      .object(fullPath)
                      .build()
            );
            return metaData;
        } catch (Exception ex) {
            throw new MinioException("Не удалось получить метаданные файла");
        }
    }

    public ByteArrayResource downloadFile(String path, String fileName) {
        String fullPath = path + "/" + fileName;
        try {
            byte[] fileData = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(fullPath)
                            .build()
            ).readAllBytes();
            ByteArrayResource resource = new ByteArrayResource(fileData);
            return resource;
        } catch (Exception ex) {
            throw new MinioException("Не удалось загрузить файл");
        }
    }
}
