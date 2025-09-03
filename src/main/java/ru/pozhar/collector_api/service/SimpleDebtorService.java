package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.RequestDocumentDTO;
import ru.pozhar.collector_api.dto.ResponseUpdatePhoneDTO;
import ru.pozhar.collector_api.exception.BusinessLogicException;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.repository.DebtorRepository;
import ru.pozhar.collector_api.repository.DocumentRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDebtorService implements DebtorService {

    private final DebtorMapper debtorMapper;

    private final DebtorRepository debtorRepository;

    private final DocumentRepository documentRepository;

    @Transactional
    @Override
    public Debtor initDebtor(RequestDebtorDTO debtorDTO) {
        Debtor debtor = debtorMapper.toDebtorEntity(debtorDTO);
        List<RequestDocumentDTO> documentDTOs = debtorDTO.documentDTOs();
        List<Document> documents = new LinkedList<>();
        for (RequestDocumentDTO documentDTO : documentDTOs) {
            Optional<Document> documentOptional = documentRepository
                    .findByDocumentTypeAndDocumentNumber(documentDTO.documentType(), documentDTO.documentNumber());
            if (documentOptional.isPresent()) {
                Document document = documentOptional.get();
                Optional<Debtor> debtorOptional = debtorRepository.findByDebtorId(document.getDebtor().getId());
                if (debtorOptional.isPresent()) {
                    Debtor findDebtor = debtorOptional.get();
                    validateDebtor(findDebtor, debtorDTO);
                }
                debtor = debtorOptional.get();
                documents.add(document);
            }
        }
        if(documents.size() > 0) {
            List<Document> findDocuments = documentRepository.findByDebtor(debtor);
            validateDocuments(findDocuments, documentDTOs);
        }
        if(documents.size() == 0) {
            debtor = debtorRepository.save(debtor);
        }
       return debtor;
    }

    @Transactional
    @Override
    public ResponseUpdatePhoneDTO updateDebtorPhoneNumber(Long debtorId, String phoneNumber) {
        Optional<Debtor> debtorOptional = debtorRepository.findByDebtorId(debtorId);
        if (debtorOptional.isEmpty()) {
            throw new EntityNotFoundException("Заемщик не найден в базе данных");
        }
        Debtor debtor = debtorOptional.get();
        debtor.setPhoneNumber(phoneNumber);
        debtor = debtorRepository.save(debtor);
        return debtorMapper.toResponseUpdatePhoneDTO(debtor);
    }

    @Override
    public Debtor findDebtorById(Long debtorId) {
        Optional<Debtor> debtorOptional = debtorRepository.findByDebtorId(debtorId);
        if (debtorOptional.isEmpty()) {
            throw new EntityNotFoundException("Заемщик с таким ID не найден в базе данных");
        }
        return debtorOptional.get();
    }

    private void validateDebtor(Debtor debtor, RequestDebtorDTO debtorDTO) {
        boolean isValid = debtor.getFirstname().equals(debtorDTO.firstname())
                && debtor.getLastname().equals(debtorDTO.lastname())
                && debtor.getPatronymic().equals((debtorDTO.patronymic()))
                && debtor.getBirthday().isEqual(debtorDTO.birthday());
        if (!isValid) {
            throw new BusinessLogicException("В базе данных есть другой заемщик с такими документами");
        }
    }

    private void validateDocuments(List<Document> documents, List<RequestDocumentDTO> documentDTOs) {
        boolean isValid = true;
        for (RequestDocumentDTO documentDTO : documentDTOs) {
            Optional<Document> documentOptional = documents.stream()
                    .filter(d -> d.getDocumentType() == documentDTO.documentType())
                    .findFirst();
            if (documentOptional.isPresent()) {
                isValid = documentOptional.get().getDocumentNumber().equals(documentDTO.documentNumber());
            }
            if (!isValid) {
               throw new BusinessLogicException("Документы заемщика в базе данных имеют другой номер");
            }
        }
    }
}
