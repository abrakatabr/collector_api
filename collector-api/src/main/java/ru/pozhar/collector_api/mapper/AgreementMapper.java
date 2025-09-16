package ru.pozhar.collector_api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateStatusDTO;
import ru.pozhar.collector_api.model.Agreement;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.WARN)
    @Mapping(target = "id", ignore = true)
    Agreement toAgreementEntity(RequestAgreementDTO requestAgreementDTO);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    @Mapping(target = "id", source = "agreement.id")
    @Mapping(target = "debtorsDTOs", source = "debtorDTOList")
    ResponseAgreementDTO toResponseAgreementDTO(Agreement agreement, List<ResponseDebtorDTO> debtorDTOList);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    @Mapping(target = "agreementId", source = "agreement.id")
    ResponseUpdateStatusDTO toResponseUpdateStatusDTO(Agreement agreement);
}
