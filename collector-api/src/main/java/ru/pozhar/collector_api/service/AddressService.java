package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateAddressDTO;
import ru.pozhar.collector_api.exception.EntityNotFoundException;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.repository.AddressRepository;
import ru.pozhar.collector_api.repository.DebtorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final DebtorRepository debtorRepository;

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    @Transactional
    public Address initAddress(Debtor debtor, RequestAddressDTO addressDTO) {
        List<Address> addresses = addressRepository.findByFilters(
                debtor.getId(),
                null,
                null,
                null,
                null,
                null,
                addressDTO.addressStatus()
        );
        Address address;
        if (addresses.size() != 0) {
            address = addresses.stream().findFirst().get();
        } else {
            address = addressMapper.toAddressEntity(debtor, addressDTO);
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
        List<Address> addresses = addressRepository.findByFilters(
                debtorId,
                null,
                null,
                null,
                null,
                null,
                addressDTO.addressStatus()
        );
        Address address;
        Debtor debtor = debtorRepository.findByDebtorId(debtorId);
        if (addresses.size() == 0) {
            if (debtor == null) {
                throw new EntityNotFoundException("Заемщик с таким ID не найден");
            }
            address = addressMapper.toAddressEntity(debtor, addressDTO);
        } else {
            address = addresses.stream().findFirst().get();
            Address newAddress = addressMapper.toAddressEntity(debtor, addressDTO);
            newAddress.setId(address.getId());
            address = newAddress;
        }
        address = addressRepository.save(address);
        return addressMapper.toResponseUpdateAddressDTO(address);
    }

    @Transactional
    public List<ResponseAddressDTO> getDebtorAddresses(Long debtorId) {
        List<Address> addresses = addressRepository.findByDebtorId(debtorId);
        if (addresses.size() == 0) {
            throw new EntityNotFoundException("Адреса не найдены в базе данных");
        }
        List<ResponseAddressDTO> addressDTOList = addresses.stream()
                .map(a -> addressMapper.toResponseAddressDTO(a))
                .collect(Collectors.toList());
        return addressDTOList;
    }

    public void deleteAddress(Long debtorId, String addressStatus) {
        List<Address> addresses = addressRepository.findByFilters(
                debtorId,
                null,
                null,
                null,
                null,
                null,
                addressStatus
        );
        if(addresses.size() > 0) {
            Address address = addresses.stream().findFirst().get();
            addressRepository.delete(address);
        }
    }
}
