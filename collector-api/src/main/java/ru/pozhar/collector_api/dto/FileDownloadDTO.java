package ru.pozhar.collector_api.dto;

import lombok.Getter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


public record FileDownloadDTO (
        ByteArrayResource resource,
        String filename,
        String contentType,
        Long size) { }
