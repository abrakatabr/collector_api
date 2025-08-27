package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAddress;

@Service
public interface DebtorAddressService {
    DebtorAddress initDebtorAddress(Debtor debtor, Address address, RequestAddressDTO addressDTO);
}
