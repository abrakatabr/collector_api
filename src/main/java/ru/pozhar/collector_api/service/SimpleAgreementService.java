package ru.pozhar.collector_api.service;

import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import lombok.AllArgsConstructor;
import ru.pozhar.collector_api.mapper.AgreementMapper;
import ru.pozhar.collector_api.mapper.DebtorAgreementMapper;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.repository.AgreementRepository;
import ru.pozhar.collector_api.repository.DebtorAgreementRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SimpleAgreementService implements AgreementService {

    private final AgreementMapper agreementMapper;

    private final DebtorService debtorService;

    private final DebtorAgreementMapper debtorAgreementMapper;


    private final AgreementRepository agreementRepository;

    private final DebtorAgreementRepository debtorAgreementRepository;

    @Override
    @Transactional
    public ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO) {
        Agreement agreement = agreementMapper.toAgreementEntity(requestAgreementDTO);
        List<Debtor> createdDebtors = debtorService.initDebtors(requestAgreementDTO.debtors());
        Agreement createdAgreement = agreementRepository.save(agreement);
        List<DebtorAgreement> debtorAgreements = debtorAgreementMapper.toDebtorAgreementEntityList(createdDebtors,
                createdAgreement);
        debtorAgreementRepository.saveAll(debtorAgreements);
        return agreementMapper.toResponseAgreementDTO(createdAgreement, createdDebtors);
    }
}
