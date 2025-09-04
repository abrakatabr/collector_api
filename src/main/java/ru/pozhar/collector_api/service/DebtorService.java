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
public class DebtorService {

    private final DebtorMapper debtorMapper;

    private final DebtorRepository debtorRepository;

    private final DocumentRepository documentRepository;

    @Transactional
    public Debtor initDebtor(RequestDebtorDTO debtorDTO) {
        List<RequestDocumentDTO> documentDTOs = debtorDTO.documentDTOs();
        List<Document> documents = new LinkedList<>();
        Debtor debtor = null;
        for (RequestDocumentDTO documentDTO : documentDTOs) {
            Document document= documentRepository
                    .findByDocumentTypeAndDocumentNumber(documentDTO.documentType(), documentDTO.documentNumber());
            if (document != null) {
                debtor = debtorRepository.findByDebtorId(document.getDebtor().getId());
                if (debtor != null) {
                    validateDebtor(debtor, debtorDTO);
                }
                documents.add(document);
            }
        }
        if(documents.size() > 0 && debtor != null) {
            List<Document> findDocuments = documentRepository.findByDebtor(debtor);
            validateDocuments(findDocuments, documentDTOs);
        }
        if(documents.size() == 0) {
            debtor = debtorMapper.toDebtorEntity(debtorDTO);
            debtor = debtorRepository.save(debtor);
        }
       return debtor;
    }

    @Transactional
    public ResponseUpdatePhoneDTO updateDebtorPhoneNumber(Long debtorId, String phoneNumber) {
        Debtor debtor = debtorRepository.findByDebtorId(debtorId);
        if (debtor == null) {
            throw new EntityNotFoundException("Заемщик не найден в базе данных");
        }
        debtor.setPhoneNumber(phoneNumber);
        debtor = debtorRepository.save(debtor);
        return debtorMapper.toResponseUpdatePhoneDTO(debtor);
    }

    public Debtor findDebtorById(Long debtorId) {
        Debtor debtor = debtorRepository.findByDebtorId(debtorId);
        if (debtor == null) {
            throw new EntityNotFoundException("Заемщик с таким ID не найден в базе данных");
        }
        return debtor;
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
