package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.repository.AddressRepository;

import java.util.List;

@Service
public interface AddressService {

    List<ResponseAddressDTO> createAddresses(List<RequestAddressDTO> requestAddressDTOList);

    Address initAddress(Address address);
}
