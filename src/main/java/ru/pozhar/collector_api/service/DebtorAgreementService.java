package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;

@Service
public interface DebtorAgreementService {
    DebtorAgreement initDebtorAgreement(Debtor debtor, Agreement agreement, RequestDebtorDTO requestDebtorDTO);

    void deleteDebtorFromAgreement(Long debtorId, Long agreementId);

    DebtorAgreement findByDebtorIdAndAgreementId(Long debtorId, Long agreementId);
}
