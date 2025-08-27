package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAddress;

@Mapper(componentModel = "spring")
public interface DebtorAddressMapper {

    @Mapping(source = "requestAddressDTO.addressStatus", target = "addressStatus")
    @Mapping(target = "id", ignore = true)
    DebtorAddress toDebtorAddressEntity(Debtor debtor, Address address, RequestAddressDTO requestAddressDTO);
}
