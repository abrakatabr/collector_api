package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;

@Service
public interface AddressService {
    Address initAddress(Debtor debtor, RequestAddressDTO addressDTO);
    Address findAddressById(Long addressId);
}
