package ru.pozhar.collector_api.mapper;

import org.mapstruct.ReportingPolicy;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateAddressDTO;
import ru.pozhar.collector_api.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.model.Debtor;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "debtor", source = "debtor")
    Address toAddressEntity(Debtor debtor, RequestAddressDTO addressDTO);

    ResponseAddressDTO toResponseAddressDTO(Address address);

    @Mapping(target = "debtorId", source = "address.debtor.id")
    ResponseUpdateAddressDTO toResponseUpdateAddressDTO(Address address);
}