package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.exception.ValidationException;
import ru.pozhar.collector_api.openapi.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.RequestDocumentDTO;
import ru.pozhar.collector_api.openapi.dto.RequestUpdateDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseGetDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseUpdateDebtorDTO;
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

    private final AddressService addressService;

    private final DocumentService documentService;

    @Transactional
    public Debtor initDebtor(RequestDebtorDTO debtorDTO) {
        List<RequestDocumentDTO> documentDTOs = debtorDTO.getDocumentDTOs();
        List<Document> documents = new LinkedList<>();
        Debtor debtor = null;
        for (RequestDocumentDTO documentDTO : documentDTOs) {
            Document document= documentRepository
                    .findByDocumentTypeAndDocumentNumber(documentDTO.getDocumentType(), documentDTO.getDocumentNumber());
            if (document != null) {
                debtor = debtorRepository.findByDebtorId(document.getDebtor().getId());
                if (debtor != null) {
                    validateDebtor(debtor, debtorDTO);
                }
                documents.add(document);
            }
        }
        if(documents.size() > 0 && debtor != null) {
            List<Document> findDocuments = documentRepository.findByDebtorId(debtor.getId());
            validateDocuments(findDocuments, documentDTOs);
        }
        if(documents.size() == 0) {
            debtor = debtorMapper.toDebtorEntity(debtorDTO);
            debtor = debtorRepository.save(debtor);
        }
       return debtor;
    }

    @Transactional
    public ResponseUpdateDebtorDTO updateDebtor(Long debtorId, RequestUpdateDebtorDTO debtorDTO) {
        Debtor debtor = debtorRepository.findByDebtorId(debtorId);
        debtor = debtorMapper.toUpdateDebtorEntity(debtor, debtorDTO);
        debtor = debtorRepository.save(debtor);
        return debtorMapper.toResponseUpdateDebtorDTO(debtor);
    }

    public Debtor findDebtorById(Long debtorId) {
        Debtor debtor = debtorRepository.findByDebtorId(debtorId);
        if (debtor == null) {
            throw new EntityNotFoundException("Заемщик с таким ID не найден в базе данных");
        }
        return debtor;
    }

    @Transactional(readOnly = true)
    public ResponseGetDebtorDTO getDebtor(Long debtorId) {
        Debtor debtor = debtorRepository.findByDebtorId(debtorId);
        if (debtor == null) {
            throw new EntityNotFoundException("Заемщик с таким ID не найден");
        }
        List<ResponseAddressDTO> responseAddressDTOs = addressService.getDebtorAddresses(debtor.getId());
        List<ResponseDocumentDTO> responseDocumentDTOs = documentService.getDebtorDocuments(debtor.getId());
        return debtorMapper.toResponseGetDebtorDTO(debtor, responseAddressDTOs, responseDocumentDTOs);
    }

    private void validateDebtor(Debtor debtor, RequestDebtorDTO debtorDTO) {
        boolean isValid = debtor.getFirstname().equals(debtorDTO.getFirstname())
                && debtor.getLastname().equals(debtorDTO.getLastname())
                && debtor.getPatronymic().equals((debtorDTO.getPatronymic()))
                && debtor.getBirthday().isEqual(debtorDTO.getBirthday());
        if (!isValid) {
            throw new BusinessLogicException("В базе данных есть другой заемщик с такими документами");
        }
    }

    private void validateDocuments(List<Document> documents, List<RequestDocumentDTO> documentDTOs) {
        boolean isValid = true;
        for (RequestDocumentDTO documentDTO : documentDTOs) {
            Optional<Document> documentOptional = documents.stream()
                    .filter(d -> d.getDocumentType() == documentDTO.getDocumentType())
                    .findFirst();
            if (documentOptional.isPresent()) {
                isValid = documentOptional.get().getDocumentNumber().equals(documentDTO.getDocumentNumber());
            }
            if (!isValid) {
               throw new BusinessLogicException("Документы заемщика в базе данных имеют другой номер");
            }
        }
    }
}
