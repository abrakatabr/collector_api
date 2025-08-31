package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.repository.AddressRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleAddressService implements AddressService {

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    @Transactional
    @Override
    public Address initAddress(Debtor debtor, RequestAddressDTO addressDTO) {
        Address address = addressMapper.toAddressEntity(debtor, addressDTO);
        address.setDebtor(debtor);
        List<Address> addresses = addressRepository.findByDebtorId(debtor.getId());
        if (addresses.size() > 0) {
            address = addresses.stream()
                    .filter(a -> addressDTO.addressStatus().equals(a.getAddressStatus()))
                    .findFirst()
                    .get();
        } else {
            address = addressRepository.save(address);
        }
        return address;
    }

    @Override
    public Address findAddressById(Long addressId) {
        Optional<Address> addressOptional = addressRepository.findById(addressId);
        if (addressOptional.isEmpty()) {
            throw new RuntimeException("Адрес с таким id не найден в репозитории");
        }
        return addressOptional.get();
    }
}
