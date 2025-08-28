package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorDocuments;
import ru.pozhar.collector_api.model.Documents;

@Service
public interface DebtorDocumentsService {
    DebtorDocuments initDebtorDocuments(Debtor debtor, Documents documents);

    DebtorDocuments findByDebtorId(Long debtorId);
}
