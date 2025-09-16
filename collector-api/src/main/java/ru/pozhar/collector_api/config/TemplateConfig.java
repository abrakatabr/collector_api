package ru.pozhar.collector_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class TemplateConfig {

    @Bean
    public String notificationTemplate(ResourceLoader resourceLoader) throws IOException {
        try {
            Resource resource = resourceLoader.getResource("classpath:notification_template.txt");
            return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IOException("Не удеалось загрузить шаблон");
        }
    }
}
