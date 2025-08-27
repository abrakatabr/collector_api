package ru.pozhar.collector_api.service;

import ru.pozhar.collector_api.dto.*;
import lombok.AllArgsConstructor;
import ru.pozhar.collector_api.mapper.*;
import ru.pozhar.collector_api.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.repository.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleAgreementService implements AgreementService {
    private final AgreementMapper agreementMapper;
    private final AddressMapper addressMapper;
    private final DebtorAddressMapper debtorAddressMapper;

    private final DocumentsMapper documentsMapper;

    private final DebtorDocumentsMapper debtorDocumentsMapper;

    private final DebtorMapper debtorMapper;

    private final DebtorAgreementMapper debtorAgreementMapper;

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
      List<Address> createdAddresses = new LinkedList<>();
      List<Documents> createdDocuments = new LinkedList<>();
      Agreement createdAgreement = agreementRepository.save(agreementMapper.toAgreementEntity(requestAgreementDTO));
      for (RequestDebtorDTO debtorDTO: requestAgreementDTO.debtors()) {
          Debtor debtor = debtorRepository.save(debtorMapper.toDebtorEntity(debtorDTO));
          createdDebtors.add(debtor);
          DebtorAgreement debtorAgreement = debtorAgreementRepository
                  .save(debtorAgreementMapper.toDebtorAgreementEntity(debtor, createdAgreement, debtorDTO));
          createdDebtorAgreements.add(debtorAgreement);
          for(RequestAddressDTO addressDTO : debtorDTO.addressDTOs()) {
              Address address = addressRepository.save(addressMapper.toAddressEntity(addressDTO));
              createdAddresses.add(address);
              DebtorAddress debtorAddress = debtorAddressRepository
                      .save(debtorAddressMapper.toDebtorAddressEntity(debtor, address, addressDTO));
              createdDebtorAddresses.add(debtorAddress);
          }
          for(RequestDocumentsDTO documentsDTO : debtorDTO.documentsDTOs()) {
              Documents documents = documentsRepository.save(documentsMapper.toDocumentsEntity(documentsDTO));
              createdDocuments.add(documents);
              DebtorDocuments debtorDocuments = debtorDocumentsRepository
                      .save(debtorDocumentsMapper.toDebtorDocumentsEntity(debtor, documents));
              createdDebtorDocuments.add(debtorDocuments);
          }
      }
    }
}
