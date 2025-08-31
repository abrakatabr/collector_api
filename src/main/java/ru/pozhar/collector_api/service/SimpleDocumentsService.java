package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDocumentsDTO;
import ru.pozhar.collector_api.mapper.DocumentsMapper;
import ru.pozhar.collector_api.model.Documents;
import ru.pozhar.collector_api.repository.DocumentsRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDocumentsService implements DocumentsService{
    private final DocumentsMapper documentsMapper;
    private final DocumentsRepository documentsRepository;
    @Override
    public Documents initDocuments(RequestDocumentsDTO documentsDTO) {
        Documents documents = documentsMapper.toDocumentsEntity(documentsDTO);
        Optional<Documents> documentsOptional = documentsRepository.findByPassportNumber(documentsDTO.passportNumber());
        if (documentsOptional.isPresent()) {
            documents = documentsOptional.get();
        } else {
            documents = documentsRepository.save(documents);
        }
        return documents;
    }

    @Override
    public Documents findDocumentsById(Long documentsId) {
        Optional<Documents> documentsOptional = documentsRepository.findById(documentsId);
        if (documentsOptional.isEmpty()) {
            throw new RuntimeException("Документов с таким ID нет в базе данных");
        }
        return documentsOptional.get();
    }
}
