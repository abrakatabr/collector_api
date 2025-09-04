package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateAddressDTO;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.repository.AddressRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final DebtorService debtorService;

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    @Transactional
    public Address initAddress(Debtor debtor, RequestAddressDTO addressDTO) {
        Address address = addressMapper.toAddressEntity(debtor, addressDTO);
        address.setDebtor(debtor);
        List<Address> addresses = findAddressesByDebtorId(debtor.getId());
            Optional<Address> addressOptional = addressFilter(addresses, addressDTO);
            if (addressOptional.isPresent()) {
                address = addressOptional.get();
            } else {
                address = addressRepository.save(address);
            }
        return address;
    }

    public List<Address> findAddressesByDebtorId(Long debtorId) {
        List<Address> addresses = addressRepository.findByDebtorId(debtorId);
        return addresses;
    }

    @Transactional
    public ResponseUpdateAddressDTO updateAddress (Long debtorId, RequestAddressDTO addressDTO) {
        List<Address> addresses = findAddressesByDebtorId(debtorId);
        Optional<Address> addressOptional = addressFilter(addresses, addressDTO);
        Address address;
        if (addressOptional.isEmpty()) {
            Debtor debtor = debtorService.findDebtorById(debtorId);
            address = addressMapper.toAddressEntity(debtor, addressDTO);
        } else {
            address = addressOptional.get();
            address.setCountry(addressDTO.country());
            address.setCity(addressDTO.city());
            address.setStreet(addressDTO.street());
            address.setHouse(addressDTO.house());
            address.setApartment(addressDTO.apartment());
        }
        address = addressRepository.save(address);
        return addressMapper.toResponseUpdateAddressDTO(address);
    }

    private Optional<Address> addressFilter (List<Address> addresses, RequestAddressDTO addressDTO) {
        return addresses.stream()
                .filter(a -> addressDTO.addressStatus().equals(a.getAddressStatus()))
                .findFirst();
    }
}
