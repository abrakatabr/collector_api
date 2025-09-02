package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDocumentDTO;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.Document;

import java.util.List;

@Service
public interface DocumentService {
    List<Document> initDocuments(List<RequestDocumentDTO> documentDTOs, Debtor debtor);

    List<Document> findDocumentsByDebtor(Debtor debtor);
}
