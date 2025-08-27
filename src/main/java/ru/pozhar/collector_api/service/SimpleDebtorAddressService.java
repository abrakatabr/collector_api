package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.mapper.DebtorAddressMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAddress;
import ru.pozhar.collector_api.repository.DebtorAddressRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleDebtorAddressService implements DebtorAddressService {
    private final DebtorAddressRepository debtorAddressRepository;
    private final DebtorAddressMapper debtorAddressMapper;

    @Override
    public DebtorAddress initDebtorAddress(Debtor debtor, Address address, RequestAddressDTO addressDTO) {
        DebtorAddress debtorAddress = debtorAddressMapper.toDebtorAddressEntity(debtor, address, addressDTO);
        Optional<DebtorAddress> debtorAddressOptional = debtorAddressRepository
                .findByDebtorIdAndAddressId(debtor.getId(), address.getId());
        if (debtorAddressOptional.isPresent()) {
            debtorAddress = debtorAddressOptional.get();
        } else {
            debtorAddress = debtorAddressRepository.save(debtorAddress);
        }
        return debtorAddress;
    }
}
