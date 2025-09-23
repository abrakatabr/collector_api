package ru.pozhar.collector_api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.openapi.dto.RequestDocumentDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.mapper.DocumentMapper;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.repository.DocumentRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
                    .findByDocumentTypeAndDocumentNumber(documentDTO.getDocumentType(), documentDTO.getDocumentNumber());
            if (document == null) {
                document = documentMapper.toDocumentEntity(documentDTO, debtor);
                document = documentRepository.save(document);
            }
            documents.add(document);
        }
        return documents;
    }

    @Transactional
    public List<Document> findDocumentsByDebtor(Debtor debtor) {
        List<Document> documents = documentRepository.findByDebtorId(debtor.getId());
        if (documents.size() == 0) {
            throw new EntityNotFoundException("Документов с таким ID нет в базе данных");
        }
        return documents;
    }

    @Transactional
    public List<ResponseDocumentDTO> getDebtorDocuments(Long debtorId) {
        List<Document> documents = documentRepository.findByDebtorId(debtorId);
        if (documents.size() == 0) {
            throw new EntityNotFoundException("Документы не найдены в базе данных");
        }
        List<ResponseDocumentDTO> documentDTOList = documents.stream()
                .map(d -> documentMapper.toResponseDocumentDTO(d))
                .collect(Collectors.toList());
        return documentDTOList;
    }
}
