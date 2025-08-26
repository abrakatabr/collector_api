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
    public List<ResponseAddressDTO> createAddresses(List<RequestAddressDTO> requestAddressDTOList) {
        List<ResponseAddressDTO> addressesList = requestAddressDTOList.stream()
                .map(requestAddressDTO -> addressMapper.toAddressEntity(requestAddressDTO))
                .map(address -> {
                    Address findAddress = address;
                    Optional<Address> optionalAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndApartment(
                            findAddress.getCountry(),
                            findAddress.getCity(),
                            findAddress.getStreet(),
                            findAddress.getHouse(),
                            findAddress.getHouse()
                    );
                    if(optionalAddress.isPresent()) {
                        findAddress = optionalAddress.get();
                    } else {
                        findAddress = addressRepository.save(findAddress);
                    }
                    return findAddress;
                })
                .map(address -> addressMapper.toResponseAddressDTO(address))
                .collect(Collectors.toList());
        return addressesList;
    }

    @Transactional
    @Override
    public Address initAddress(Address address) {
        Optional<Address> optionalAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndApartment(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getApartment()
        );
        Address findAddress = optionalAddress.isPresent() ? optionalAddress.get() : addressRepository.save(address);
        return findAddress;
    }
}
