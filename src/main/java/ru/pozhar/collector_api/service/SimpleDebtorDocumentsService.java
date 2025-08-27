package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.mapper.DebtorDocumentsMapper;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorDocuments;
import ru.pozhar.collector_api.model.Documents;
import ru.pozhar.collector_api.repository.DebtorDocumentsRepository;

@Service
@RequiredArgsConstructor
public class SimpleDebtorDocumentsService implements DebtorDocumentsService {
    private final DebtorDocumentsRepository debtorDocumentsRepository;
    private final DebtorDocumentsMapper debtorDocumentsMapper;

    @Override
    public DebtorDocuments initDebtorDocuments(Debtor debtor, Documents documents) {
        return debtorDocumentsRepository
                .save(debtorDocumentsMapper.toDebtorDocumentsEntity(debtor, documents));
    }
}
