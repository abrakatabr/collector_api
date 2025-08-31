package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseUpdatePhoneDTO;
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
                if (!validateDebtor(debtor, documentsOptional.get(), debtorDTO)) {
                    throw new RuntimeException("В базе данных есть другой заемщик с такими документами");
                }
            } else {
                throw new RuntimeException("По паспортным данным не найден заемщик в базе даных");
            }
        } else {
            debtor = debtorRepository.save(debtor);
        }
       return debtor;
    }

    @Transactional
    @Override
    public ResponseUpdatePhoneDTO updateDebtorPhoneNumber(Long debtorId, String phoneNumber) {
        Optional<Debtor> debtorOptional = debtorRepository.findByDebtorId(debtorId);
        if (debtorOptional.isEmpty()) {
            throw new RuntimeException("Заемщик не найден в базе данных");
        }
        Debtor debtor = debtorOptional.get();
        debtor.setPhoneNumber(phoneNumber);
        debtor = debtorRepository.save(debtor);
        return debtorMapper.toResponseUpdatePhoneDTO(debtor);
    }

    private boolean validateDebtor(Debtor debtor, Documents documents, RequestDebtorDTO debtorDTO) {
        boolean isValidInn = true;
        boolean isValidSnils = true;
        if (!documents.getInn().isBlank() && documents.getInn() != null) {
            if (!debtorDTO.documentsDTO().inn().isBlank() && debtorDTO.documentsDTO().inn() != null) {
                isValidInn = documents.getInn().equals(debtorDTO.documentsDTO().inn());
            }
        }
        if (!documents.getSnils().isBlank() && documents.getSnils() != null) {
            if (!debtorDTO.documentsDTO().snils().isBlank() && debtorDTO.documentsDTO().snils() != null) {
                isValidSnils = documents.getSnils().equals(debtorDTO.documentsDTO().snils());
            }
        }
        return debtor.getFirstname().equals(debtorDTO.firstname())
                && debtor.getLastname().equals(debtorDTO.lastname())
                && debtor.getPatronymic().equals((debtorDTO.patronymic()))
                && debtor.getBirthday().isEqual(debtorDTO.birthday())
                && documents.getPassportNumber().equals(debtorDTO.documentsDTO().passportNumber())
                &&  isValidInn && isValidSnils;
    }
}
