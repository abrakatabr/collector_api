package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.model.Debtor;

@Mapper(componentModel = "spring")
public interface DebtorMapper {
    @Mapping(target = "id", ignore = true)
    Debtor toDebtorEntity(RequestDebtorDTO requestDebtorDTO);
}
