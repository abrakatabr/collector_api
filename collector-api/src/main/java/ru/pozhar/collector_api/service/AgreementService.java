package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.dto.ResponsePage;
import ru.pozhar.collector_api.dto.ResponseUpdateStatusDTO;
import ru.pozhar.collector_api.exception.BusinessLogicException;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.mapper.AgreementKeyMapper;
import ru.pozhar.collector_api.mapper.AgreementMapper;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.mapper.DocumentMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.AgreementKey;
import ru.pozhar.collector_api.model.AgreementStatus;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import ru.pozhar.collector_api.model.Document;
import ru.pozhar.collector_api.repository.AgreementKeyRepository;
import ru.pozhar.collector_api.repository.AgreementRepository;
import ru.pozhar.collector_api.repository.DebtorAgreementRepository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgreementService {
    private final AgreementMapper agreementMapper;

    private final AddressMapper addressMapper;

    private final DocumentMapper documentMapper;

    private final DebtorMapper debtorMapper;

    private final AgreementKeyMapper agreementKeyMapper;

    private final DebtorService debtorService;

    private final DebtorAgreementService debtorAgreementService;

    private final DocumentService documentService;

    private final AddressService addressService;

    private final AgreementRepository agreementRepository;

    private final AgreementKeyRepository agreementKeyRepository;

    private final DebtorAgreementRepository debtorAgreementRepository;

    @Transactional
    public ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO, Long key) {
      List<DebtorAgreement> createdDebtorAgreements = new LinkedList<>();
      List<Debtor> createdDebtors = new LinkedList<>();
      Map<Long, List<Address>> createdAddresses = new HashMap<>();
      Map<Long, List<Document>> createdDocuments = new HashMap<>();
      Agreement createdAgreement;
      AgreementKey agreementKey = agreementKeyRepository.findByKey(key);
      if (agreementKey != null && isEqualsAgreements(agreementKey.getAgreement(), requestAgreementDTO)) {
          createdAgreement = agreementKey.getAgreement();
          List<DebtorAgreement> debtorAgreements = debtorAgreementRepository.findByAgreement(createdAgreement);
          for (DebtorAgreement debtorAgreement : debtorAgreements) {
              Debtor debtor = debtorService.findDebtorById(debtorAgreement.getDebtor().getId());
              createdDebtors.add(debtor);
              createdDebtorAgreements.add(debtorAgreement);
              List<Document> documents = documentService.findDocumentsByDebtor(debtor);
              createdDocuments.put(debtor.getId(), documents);
              List<Address> createdAddressesList = addressService.findAddressesByDebtorId(debtor.getId());
              createdAddresses.put(debtor.getId(), createdAddressesList);
          }
      } else {
          createdAgreement = agreementRepository.save(agreementMapper.toAgreementEntity(requestAgreementDTO));
          agreementKeyRepository.save(agreementKeyMapper.toAgreementKeyEntity(createdAgreement, key));
          for (RequestDebtorDTO debtorDTO : requestAgreementDTO.debtors()) {
              Debtor debtor = debtorService.initDebtor(debtorDTO);
              createdDebtors.add(debtor);
              DebtorAgreement debtorAgreement = debtorAgreementService
                      .initDebtorAgreement(debtor, createdAgreement, debtorDTO);
              createdDebtorAgreements.add(debtorAgreement);
              List<Document> documents = documentService.initDocuments(debtorDTO.documentDTOs(), debtor);
              createdDocuments.put(debtor.getId(), documents);
              List<Address> createdAddressesList = new LinkedList<>();
              for (RequestAddressDTO addressDTO : debtorDTO.addressDTOs()) {
                  Address address = addressService.initAddress(debtor, addressDTO);
                  createdAddressesList.add(address);
              }
              createdAddresses.put(debtor.getId(), createdAddressesList);
          }
      }
      List<ResponseDebtorDTO> responseDebtorDTOList = new LinkedList<>();
      for (Debtor debtor : createdDebtors) {
          DebtorAgreement debtorAgreement = createdDebtorAgreements.stream()
                  .filter(da -> da.getDebtor().getId() == debtor.getId()).findFirst().get();
          List<ResponseAddressDTO> responseAddressDTOList = new LinkedList<>();
          for (Address address : createdAddresses.get(debtor.getId())) {
              responseAddressDTOList.add(addressMapper.toResponseAddressDTO(address));
          }
          List<ResponseDocumentDTO> responseDocumentDTOList = new LinkedList<>();
          for (Document document : createdDocuments.get(debtor.getId())) {
              responseDocumentDTOList.add(documentMapper.toResponseDocumentDTO(document));
          }
          ResponseDebtorDTO responseDebtorDTO = debtorMapper
                  .toResponseDebtorDTO(debtor, debtorAgreement, responseAddressDTOList, responseDocumentDTOList);
          responseDebtorDTOList.add(responseDebtorDTO);
      }
      return agreementMapper.toResponseAgreementDTO(createdAgreement, responseDebtorDTOList);
    }

    public ResponsePage<Agreement> getAllAgreements(Pageable pageable, String transferor, AgreementStatus status) {
        Page<Agreement> agreementsPage = agreementRepository.findAllAgreements(transferor, status, pageable);
        ResponsePage<Agreement> responsePage = new ResponsePage<>(agreementsPage);
        return responsePage;
    }

    @Transactional
    public void deleteAgreement(Long agreementId) {
        Agreement agreement = findAgreementById(agreementId);
        if(agreement.getStatus() != AgreementStatus.deleted) {
            agreement.setStatus(AgreementStatus.deleted);
            agreementRepository.save(agreement);
        }
    }

    public Agreement findAgreementById(Long agreementId) {
        Optional<Agreement> agreementOptional = agreementRepository.findById(agreementId);
        if (agreementOptional.isEmpty()) {
            throw new EntityNotFoundException("Договор с таким ID не найден");
        }
        return agreementOptional.get();
    }

    @Transactional
    public ResponseUpdateStatusDTO updateAgreementStatus(Long agreementId, AgreementStatus agreementStatus) {
        Agreement agreement = findAgreementById(agreementId);
        if (agreement.getStatus() == AgreementStatus.deleted) {
            throw new BusinessLogicException("Договор уже удален");
        }
        agreement.setStatus(agreementStatus);
        agreement = agreementRepository.save(agreement);
        ResponseUpdateStatusDTO updateStatusDTO = agreementMapper.toResponseUpdateStatusDTO(agreement);
        return updateStatusDTO;
    }

    @Transactional(readOnly = true)
    public ResponseAgreementDTO getAgreement(Long agreementId) {
        Agreement agreement = agreementRepository.findById(agreementId)
                .orElseThrow(() -> new EntityNotFoundException("Договор с таким ID не найден"));
        if (agreement.getStatus() == AgreementStatus.deleted) {
            throw new BusinessLogicException("Договор удален");
        }
        List<DebtorAgreement> debtorAgreements = debtorAgreementRepository.findByAgreement(agreement);
        List<ResponseDebtorDTO> responseDebtorDTOs = new LinkedList<>();
        for (DebtorAgreement debtorAgreement : debtorAgreements) {
            Debtor debtor = debtorAgreement.getDebtor();
            List<ResponseAddressDTO> responseAddressDTOs = addressService.getDebtorAddresses(debtor.getId());
            List<ResponseDocumentDTO> responseDocumentDTOs = documentService.getDebtorDocuments(debtor.getId());
            responseDebtorDTOs.add(debtorMapper.toResponseDebtorDTO(
                    debtor, debtorAgreement, responseAddressDTOs, responseDocumentDTOs));
        }
        ResponseAgreementDTO responseAgreementDTO = agreementMapper.toResponseAgreementDTO(agreement,
                responseDebtorDTOs);
        return responseAgreementDTO;
    }

    private boolean isEqualsAgreements(Agreement agreement, RequestAgreementDTO agreementDTO) {
        boolean isEquals = agreement.getOriginalDebtSum().compareTo(agreementDTO.originalDebtSum()) == 0
                && agreement.getActualDebtSum().compareTo(agreementDTO.actualDebtSum()) == 0
                && agreement.getAgreementStartDate().isEqual(agreementDTO.agreementStartDate())
                && agreement.getTransferor().equals(agreementDTO.transferor());
        if (!isEquals) {
            throw new BusinessLogicException("Такой ключ идемпотентности уже привязан к другому договору");
        }
        return isEquals;
    }
}
