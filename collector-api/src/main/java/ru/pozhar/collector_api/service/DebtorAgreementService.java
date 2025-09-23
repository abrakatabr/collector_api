package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.openapi.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.mapper.DebtorAgreementMapper;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import ru.pozhar.collector_api.repository.DebtorAgreementRepository;

@Service
@RequiredArgsConstructor
public class DebtorAgreementService {
    private final DebtorAgreementRepository debtorAgreementRepository;
    private final DebtorAgreementMapper debtorAgreementMapper;

    @Transactional
    public DebtorAgreement initDebtorAgreement(Debtor debtor, Agreement agreement, RequestDebtorDTO requestDebtorDTO) {
        DebtorAgreement debtorAgreement = debtorAgreementRepository.findByDebtorAndAgreement(debtor, agreement);
        if (debtorAgreement == null) {
            debtorAgreement = debtorAgreementMapper.toDebtorAgreementEntity(debtor, agreement, requestDebtorDTO);
            debtorAgreement = debtorAgreementRepository.save(debtorAgreement);
        }
        return debtorAgreement;
    }

    @Transactional
    public void deleteDebtorFromAgreement(Long debtorId, Long agreementId) {
        DebtorAgreement debtorAgreement = findByDebtorIdAndAgreementId(debtorId, agreementId);
        debtorAgreementRepository.delete(debtorAgreement);
    }

    public DebtorAgreement findByDebtorIdAndAgreementId(Long debtorId, Long agreementId) {
        DebtorAgreement debtorAgreement = debtorAgreementRepository.findByDebtorIdAndAgreementId(debtorId, agreementId);
        if (debtorAgreement == null) {
            throw new EntityNotFoundException("Связь заемщика и договора не найдена в базе данных");
        }
        return debtorAgreement;
    }
}
