package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.mapper.DebtorAgreementMapper;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import ru.pozhar.collector_api.repository.DebtorAgreementRepository;

@Service
@RequiredArgsConstructor
public class SimpleDebtorAgreementService implements DebtorAgreementService {
    private final DebtorAgreementRepository debtorAgreementRepository;
    private final DebtorAgreementMapper debtorAgreementMapper;

    @Override
    public DebtorAgreement initDebtorAgreement(Debtor debtor, Agreement agreement, RequestDebtorDTO requestDebtorDTO) {
        return debtorAgreementRepository
                .save(debtorAgreementMapper.toDebtorAgreementEntity(debtor, agreement, requestDebtorDTO));
    }
}
