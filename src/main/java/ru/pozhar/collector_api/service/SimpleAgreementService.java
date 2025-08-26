package ru.pozhar.collector_api.service;

import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import lombok.AllArgsConstructor;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.mapper.AgreementMapper;
import ru.pozhar.collector_api.mapper.DebtorAgreementMapper;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.repository.AddressRepository;
import ru.pozhar.collector_api.repository.AgreementRepository;
import ru.pozhar.collector_api.repository.DebtorAgreementRepository;
import ru.pozhar.collector_api.repository.DebtorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleAgreementService implements AgreementService {
    private final AgreementMapper agreementMapper;
    private final DebtorService debtorService;

    private final DebtorMapper debtorMapper;

    private final DebtorAgreementMapper debtorAgreementMapper;

    private final AddressRepository addressRepository;

    private final AgreementRepository agreementRepository;

    private final DebtorRepository debtorRepository;

    private final DebtorAgreementRepository debtorAgreementRepository;

    @Override
    @Transactional
    public ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO) {
        Agreement agreement = agreementMapper.toAgreementEntity(requestAgreementDTO);
        List<ResponseDebtorDTO> debtors = debtorService.createDebtors(requestAgreementDTO.debtors());
        List<Address> addresses = debtors.stream()
        Agreement createdAgreement = agreementRepository.save(agreement);
        List<Debtor> createdDebtors = debtorRepository.saveAll(debtors);
        List<DebtorAgreement> debtorAgreements = createdDebtors.stream()
                .map(d -> {
                    String debtorType = createdDebtors.size() > 1 ? "co-debtor" : "single debtor";
                    return DebtorAgreement.builder()
                            .debtor(d)
                            .agreement(createdAgreement)
                            .debtorType(debtorType)
                            .build();
                })
                .collect(Collectors.toList());
        debtorAgreementRepository.saveAll(debtorAgreements);
        return agreementMapper.toResponseAgreementDTO(createdAgreement, createdDebtors);
    }
}
