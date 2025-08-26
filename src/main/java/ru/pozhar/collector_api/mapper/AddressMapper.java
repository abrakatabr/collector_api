package ru.pozhar.collector_api.mapper;

import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    ResponseAddressDTO toResponseAddressDTO(Address address);

    @Mapping(target = "id", ignore = true)
    Address toAddressEntity(RequestAddressDTO addressDTO);
}