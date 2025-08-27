package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDocumentsDTO;
import ru.pozhar.collector_api.model.Documents;

@Service
public interface DocumentsService {
    Documents initDocuments(RequestDocumentsDTO documentsDTO);
}
