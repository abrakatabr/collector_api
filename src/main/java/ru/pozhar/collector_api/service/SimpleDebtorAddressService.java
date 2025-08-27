package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.mapper.DebtorAddressMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAddress;
import ru.pozhar.collector_api.repository.DebtorAddressRepository;

@Service
@RequiredArgsConstructor
public class SimpleDebtorAddressService implements DebtorAddressService {
    private final DebtorAddressRepository debtorAddressRepository;
    private final DebtorAddressMapper debtorAddressMapper;

    @Override
    public DebtorAddress initDebtorAddress(Debtor debtor, Address address, RequestAddressDTO addressDTO) {
        return debtorAddressRepository
                .save(debtorAddressMapper.toDebtorAddressEntity(debtor, address, addressDTO));
    }
}
