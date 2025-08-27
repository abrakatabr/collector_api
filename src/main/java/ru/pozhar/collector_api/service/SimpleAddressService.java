package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.repository.AddressRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleAddressService implements AddressService {

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    @Transactional
    @Override
    public Address initAddress(RequestAddressDTO addressDTO) {
        Address address = addressMapper.toAddressEntity(addressDTO);
        Optional<Address> optionalAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndApartment(
                addressDTO.country(),
                addressDTO.city(),
                addressDTO.street(),
                addressDTO.house(),
                addressDTO.apartment()
        );
        address= optionalAddress.isPresent() ? optionalAddress.get() : addressRepository.save(address);
        return address;
    }
}
