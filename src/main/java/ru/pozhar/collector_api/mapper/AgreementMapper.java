package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.model.Agreement;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @Mapping(target = "id", ignore = true)
    Agreement toAgreementEntity(RequestAgreementDTO requestAgreementDTO);

    @Mapping(target = "id", source = "agreement.id")
    @Mapping(target = "debtorsDTOs", source = "debtorDTOList")
    ResponseAgreementDTO toResponseAgreementDTO(Agreement agreement, List<ResponseDebtorDTO> debtorDTOList);
}
