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

import java.util.*;

@Service
@RequiredArgsConstructor
public class SimpleDocumentService implements DocumentService {
    private final DocumentMapper documentMapper;
    private final DocumentRepository documentRepository;

    @Transactional
    @Override
    public List<Document> initDocuments(List<RequestDocumentDTO> documentDTOs, Debtor debtor) {
        List<Document> documents = new LinkedList<>();
        for (RequestDocumentDTO documentDTO : documentDTOs) {
            Document document = documentMapper.toDocumentEntity(documentDTO, debtor);
            Optional<Document> documentsOptional = documentRepository
                    .findByDocumentTypeAndDocumentNumber(documentDTO.documentType(), documentDTO.documentNumber());
            if (documentsOptional.isPresent()) {
                document = documentsOptional.get();
            } else {
                document = documentRepository.save(document);
            }
            documents.add(document);
        }
        return documents;
    }

    @Override
    public List<Document> findDocumentsByDebtor(Debtor debtor) {
        List<Document> documents = documentRepository.findByDebtor(debtor);
        if (documents.size() == 0) {
            throw new EntityNotFoundException("Документов с таким ID нет в базе данных");
        }
        return documents;
    }
}
