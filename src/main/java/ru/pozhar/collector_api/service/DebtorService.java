package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.model.Debtor;

import java.util.List;

@Service
public interface DebtorService {
    Debtor initDebtor(RequestDebtorDTO debtorDTO);
}
