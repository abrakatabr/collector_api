package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseUpdatePhoneDTO;
import ru.pozhar.collector_api.model.Debtor;

@Service
public interface DebtorService {
    Debtor initDebtor(RequestDebtorDTO debtorDTO);

    ResponseUpdatePhoneDTO updateDebtorPhoneNumber(Long debtorId, String phoneNumber);
}
