package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseDocumentsDTO;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.mapper.AgreementMapper;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.mapper.DocumentsMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import ru.pozhar.collector_api.model.DebtorDocuments;
import ru.pozhar.collector_api.model.Documents;
import ru.pozhar.collector_api.repository.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
}
