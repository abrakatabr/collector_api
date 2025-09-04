package ru.pozhar.collector_api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDocumentDTO;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.mapper.DocumentMapper;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.repository.DocumentRepository;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;

    @Transactional
    public List<Document> initDocuments(List<RequestDocumentDTO> documentDTOs, Debtor debtor) {
        List<Document> documents = new LinkedList<>();
        for (RequestDocumentDTO documentDTO : documentDTOs) {
            Document document = documentRepository
                    .findByDocumentTypeAndDocumentNumber(documentDTO.documentType(), documentDTO.documentNumber());
            if (document == null) {
                document = documentMapper.toDocumentEntity(documentDTO, debtor);
                document = documentRepository.save(document);
            }
            documents.add(document);
        }
        return documents;
    }

    public List<Document> findDocumentsByDebtor(Debtor debtor) {
        List<Document> documents = documentRepository.findByDebtor(debtor);
        if (documents.size() == 0) {
            throw new EntityNotFoundException("Документов с таким ID нет в базе данных");
        }
        return documents;
    }
}
