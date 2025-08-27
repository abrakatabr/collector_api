package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.model.Agreement;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @Mapping(target = "id", ignore = true)
    Agreement toAgreementEntity(RequestAgreementDTO requestAgreementDTO);
}
