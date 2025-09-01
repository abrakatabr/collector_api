package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.*;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.mapper.AgreementMapper;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.mapper.DocumentsMapper;
import ru.pozhar.collector_api.model.*;
import ru.pozhar.collector_api.repository.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SimpleAgreementService implements AgreementService {
    private final AgreementMapper agreementMapper;

    private final AddressMapper addressMapper;

    private final DocumentsMapper documentsMapper;

    private final DebtorMapper debtorMapper;

    private final DebtorService debtorService;

    private final DebtorAgreementService debtorAgreementService;

    private final DocumentsService documentsService;

    private final DebtorDocumentsService debtorDocumentsService;

    private final AddressService addressService;

    private final AgreementRepository agreementRepository;


    @Override
    @Transactional
    public ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO) {
      List<DebtorDocuments> createdDebtorDocuments = new LinkedList<>();
      List<DebtorAgreement> createdDebtorAgreements = new LinkedList<>();
      List<Debtor> createdDebtors = new LinkedList<>();
      Map<Long, List<Address>> createdAddresses = new HashMap<>();
      Map<Long,Documents> createdDocuments = new HashMap<>();
      Agreement createdAgreement = agreementRepository.save(agreementMapper.toAgreementEntity(requestAgreementDTO));
      for (RequestDebtorDTO debtorDTO: requestAgreementDTO.debtors()) {
          Debtor debtor = debtorService.initDebtor(debtorDTO);
          createdDebtors.add(debtor);
          DebtorAgreement debtorAgreement = debtorAgreementService
                  .initDebtorAgreement(debtor, createdAgreement, debtorDTO);
          createdDebtorAgreements.add(debtorAgreement);
          Documents documents = documentsService.initDocuments(debtorDTO.documentsDTO());
          createdDocuments.put(debtor.getId(), documents);
          DebtorDocuments debtorDocuments = debtorDocumentsService.initDebtorDocuments(debtor, documents);
          createdDebtorDocuments.add(debtorDocuments);
          List<Address> createdAddressesList = new LinkedList<>();
          for(RequestAddressDTO addressDTO : debtorDTO.addressDTOs()) {
              Address address = addressService.initAddress(debtor, addressDTO);
              createdAddressesList.add(address);
          }
          createdAddresses.put(debtor.getId(), createdAddressesList);
      }
      List<ResponseDebtorDTO> responseDebtorDTOList = new LinkedList<>();
      for (Debtor debtor : createdDebtors) {
          DebtorAgreement debtorAgreement = createdDebtorAgreements.stream()
                  .filter(da -> da.getDebtor().getId() == debtor.getId()).findFirst().get();
          List<ResponseAddressDTO> responseAddressDTOList = new LinkedList<>();
          for (Address address : createdAddresses.get(debtor.getId())) {
              responseAddressDTOList.add(addressMapper.toResponseAddressDTO(address));
          }
          ResponseDocumentsDTO responseDocumentsDTO = documentsMapper
                  .toResponseDocumentDTO(createdDocuments.get(debtor.getId()));
          ResponseDebtorDTO responseDebtorDTO = debtorMapper
                  .toResponseDebtorDTO(debtor, debtorAgreement, responseAddressDTOList, responseDocumentsDTO);
          responseDebtorDTOList.add(responseDebtorDTO);
      }
      return agreementMapper.toResponseAgreementDTO(createdAgreement, responseDebtorDTOList);
    }

    @Transactional
    @Override
    public void deleteAgreement(Long agreementId) {
        Agreement agreement = findAgreementById(agreementId);
        agreement.setStatus(AgreementStatus.deleted);
        agreementRepository.save(agreement);
    }

    @Override
    public Agreement findAgreementById(Long agreementId) {
        Optional<Agreement> agreementOptional = agreementRepository.findById(agreementId);
        if (agreementOptional.isEmpty()) {
            throw new RuntimeException("Договор с таким ID не найден");
        }
        return agreementOptional.get();
    }

    @Transactional
    @Override
    public ResponseUpdateStatusDTO updateAgreementStatus(Long agreementId, AgreementStatus agreementStatus) {
        Agreement agreement = findAgreementById(agreementId);
        if (agreement.getStatus() == AgreementStatus.deleted) {
            throw new RuntimeException("Договор уже удален");
        }
        agreement.setStatus(agreementStatus);
        agreement = agreementRepository.save(agreement);
        ResponseUpdateStatusDTO updateStatusDTO = agreementMapper.toResponseUpdateStatusDTO(agreement);
        return updateStatusDTO;
    }
}
