package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.mapper.DebtorAgreementMapper;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import ru.pozhar.collector_api.repository.DebtorAgreementRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDebtorAgreementService implements DebtorAgreementService {
    private final DebtorAgreementRepository debtorAgreementRepository;
    private final DebtorAgreementMapper debtorAgreementMapper;

    @Transactional
    @Override
    public DebtorAgreement initDebtorAgreement(Debtor debtor, Agreement agreement, RequestDebtorDTO requestDebtorDTO) {
        DebtorAgreement debtorAgreement = debtorAgreementMapper
                .toDebtorAgreementEntity(debtor, agreement, requestDebtorDTO);
        Optional<DebtorAgreement> debtorAgreementOptional = debtorAgreementRepository
                .findByDebtorAndAgreement(debtor, agreement);
        if (debtorAgreementOptional.isPresent()) {
            debtorAgreement = debtorAgreementOptional.get();
        } else {
            debtorAgreement = debtorAgreementRepository.save(debtorAgreement);
        }
        return debtorAgreement;
    }

    @Transactional
    @Override
    public void deleteDebtorFromAgreement(Long debtorId, Long agreementId) {
        DebtorAgreement debtorAgreement = findByDebtorIdAndAgreementId(debtorId, agreementId);
        debtorAgreementRepository.delete(debtorAgreement);
    }

    @Override
    public DebtorAgreement findByDebtorIdAndAgreementId(Long debtorId, Long agreementId) {
        Optional<DebtorAgreement> debtorAgreementOptional = debtorAgreementRepository
                .findByDebtorIdAndAgreementId(debtorId, agreementId);
        if (debtorAgreementOptional.isEmpty()) {
            throw new EntityNotFoundException("Связь заемщика и договора не найдена в базе данных");
        }
        return debtorAgreementOptional.get();
    }
}
