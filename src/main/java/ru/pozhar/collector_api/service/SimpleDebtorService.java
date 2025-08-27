package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.Documents;
import ru.pozhar.collector_api.repository.DebtorRepository;
import ru.pozhar.collector_api.repository.DocumentsRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDebtorService implements DebtorService{

    private final DebtorMapper debtorMapper;

    private final DebtorRepository debtorRepository;

    private final DocumentsRepository documentsRepository;

    @Transactional
    @Override
    public Debtor initDebtor(RequestDebtorDTO debtorDTO) {
        Debtor debtor = debtorMapper.toDebtorEntity(debtorDTO);
        Optional<Documents> documentsOptional = documentsRepository
                .findByPassportNumber(debtorDTO.documentsDTO().passportNumber());
        if (documentsOptional.isPresent()) {
            Optional<Debtor> debtorOptional = debtorRepository.findByDocumentsId(documentsOptional.get().getId());
            if (debtorOptional.isPresent()) {
                debtor = debtorOptional.get();
            } else {
                throw new RuntimeException("По паспортным данным не найден заемщик в базе даных");
            }
        } else {
            debtor = debtorRepository.save(debtor);
        }
       return debtor;
    }
}
