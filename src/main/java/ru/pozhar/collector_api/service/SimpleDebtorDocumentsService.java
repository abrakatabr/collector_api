package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.mapper.DebtorDocumentsMapper;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorDocuments;
import ru.pozhar.collector_api.model.Documents;
import ru.pozhar.collector_api.repository.DebtorDocumentsRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDebtorDocumentsService implements DebtorDocumentsService {
    private final DebtorDocumentsRepository debtorDocumentsRepository;
    private final DebtorDocumentsMapper debtorDocumentsMapper;

    @Override
    public DebtorDocuments initDebtorDocuments(Debtor debtor, Documents documents) {
        DebtorDocuments debtorDocuments = debtorDocumentsMapper.toDebtorDocumentsEntity(debtor, documents);
        Optional<DebtorDocuments> debtorDocumentsOptional = debtorDocumentsRepository.findByDebtorId(debtor.getId());
        if(debtorDocumentsOptional.isPresent()) {
            debtorDocuments = debtorDocumentsOptional.get();
        } else {
            debtorDocuments = debtorDocumentsRepository.save(debtorDocuments);
        }
        return debtorDocuments;
    }

    @Override
    public DebtorDocuments findByDebtorId(Long debtorId) {
        Optional<DebtorDocuments> debtorDocumentsOptional = debtorDocumentsRepository.findByDebtorId(debtorId);
        if (debtorDocumentsOptional.isEmpty()) {
            throw new RuntimeException("В базе данных не найдены документы заемщика");
        }
        return debtorDocumentsOptional.get();
    }
}
