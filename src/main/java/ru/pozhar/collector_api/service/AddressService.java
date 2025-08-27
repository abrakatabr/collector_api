package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.model.Address;

import java.util.List;

@Service
public interface AddressService {

    List<ResponseAddressDTO> createAddresses(List<RequestAddressDTO> requestAddressDTOList);

    Address initAddress(Address address);
}
