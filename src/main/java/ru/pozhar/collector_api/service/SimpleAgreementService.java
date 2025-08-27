package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import ru.pozhar.collector_api.dto.*;
import lombok.AllArgsConstructor;
import ru.pozhar.collector_api.mapper.*;
import ru.pozhar.collector_api.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleAgreementService implements AgreementService {
    private final AgreementMapper agreementMapper;
    private final AddressMapper addressMapper;
    private final DebtorAddressMapper debtorAddressMapper;

    private final DocumentsMapper documentsMapper;

    private final DebtorDocumentsMapper debtorDocumentsMapper;

    private final DebtorMapper debtorMapper;

    private final DebtorAgreementMapper debtorAgreementMapper;

    private final DebtorService debtorService;
    private final DebtorAgreementService debtorAgreementService;
    private final DocumentsService documentsService;
    private final DebtorDocumentsService debtorDocumentsService;
    private final AddressService addressService;
    private final DebtorAddressService debtorAddressService;

    private final AddressRepository addressRepository;

    private final AgreementRepository agreementRepository;

    private final DebtorRepository debtorRepository;

    private final DebtorAgreementRepository debtorAgreementRepository;

    private final DebtorAddressRepository debtorAddressRepository;

    private final DocumentsRepository documentsRepository;

    private final DebtorDocumentsRepository debtorDocumentsRepository;


    @Override
    @Transactional
    public ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO) {
      List<DebtorAddress> createdDebtorAddresses = new LinkedList<>();
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
              Address address = addressService.initAddress(addressDTO);
              createdAddressesList.add(address);
              DebtorAddress debtorAddress = debtorAddressService.initDebtorAddress(debtor, address, addressDTO);
              createdDebtorAddresses.add(debtorAddress);
          }
          createdAddresses.put(debtor.getId(), createdAddressesList);
      }
      List<ResponseDebtorDTO> responseDebtorDTOList = new LinkedList<>();
      for (Debtor debtor : createdDebtors) {
          DebtorAgreement debtorAgreement = createdDebtorAgreements.stream()
                  .filter(da -> da.getDebtor().getId() == debtor.getId()).findFirst().get();
          List<ResponseAddressDTO> responseAddressDTOList = new LinkedList<>();
          for (Address address : createdAddresses.get(debtor.getId())) {
              DebtorAddress debtorAddress = createdDebtorAddresses.stream()
                      .filter(da -> address.getId() == da.getAddress().getId()).findFirst().get();
              responseAddressDTOList.add(addressMapper.toResponseAddressDTO(address, debtorAddress));
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
