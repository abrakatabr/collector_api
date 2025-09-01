package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateAddressDTO;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;

import java.util.List;

@Service
public interface AddressService {
    Address initAddress(Debtor debtor, RequestAddressDTO addressDTO);
    List<Address> findAddressesByDebtorId(Long debtorId);
    ResponseUpdateAddressDTO updateAddress (Long debtorId, RequestAddressDTO addressDTO);
}
