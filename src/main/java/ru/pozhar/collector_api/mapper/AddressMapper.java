package ru.pozhar.collector_api.mapper;

import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.model.DebtorAddress;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    Address toAddressEntity(RequestAddressDTO addressDTO);

    @Mapping(target = "addressStatus", source = "debtorAddress.addressStatus")
    ResponseAddressDTO toResponseAddressDTO(Address address, DebtorAddress debtorAddress);
}