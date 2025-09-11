package ru.pozhar.collector_api.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.collector_api.exception.MinioException;

@Service
@RequiredArgsConstructor
public class MinioService {
    @Value("${SPRING_MINIO_BUCKET}")
    private final String BUCKET;

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
            boolean exists = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(BUCKET)
                            .object(fullPath)
                            .build()
            ) != null;
            if (exists) {
                throw new MinioException("Файл " + fullPath + " уже существует");
            }
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET)
                    .object(fullPath)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return fullPath;
        } catch (Exception ex) {
            throw new MinioException("Ошибка при загагрузке файла");
        }
    }
}
