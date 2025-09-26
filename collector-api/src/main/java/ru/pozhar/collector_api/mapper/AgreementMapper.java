package ru.pozhar.collector_api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.pozhar.collector_api.openapi.dto.AgreementDTO;
import ru.pozhar.collector_api.openapi.dto.AgreementStatus;
import ru.pozhar.collector_api.openapi.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseUpdateStatusDTO;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.openapi.dto.UpdateAgreementDebtorNotification;
import ru.pozhar.collector_api.openapi.dto.UpdateAgreementNotification;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AgreementMapper {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.WARN)
    @Mapping(target = "id", ignore = true)
    Agreement toAgreementEntity(RequestAgreementDTO requestAgreementDTO);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    @Mapping(target = "id", source = "agreement.id")
    @Mapping(target = "debtorDTOs", source = "debtorDTOList")
    ResponseAgreementDTO toResponseAgreementDTO(Agreement agreement, List<ResponseDebtorDTO> debtorDTOList);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    @Mapping(target = "newStatus", source = "agreement.status")
    @Mapping(target = "oldStatus", source = "oldStatus")
    @Mapping(target = "debtorDTOs", source = "debtorDTOList")
    UpdateAgreementNotification toUpdateAgreementNotification(Agreement agreement,
                                                              AgreementStatus oldStatus,
                                                              List<UpdateAgreementDebtorNotification> debtorDTOList);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    @Mapping(target = "agreementId", source = "agreement.id")
    ResponseUpdateStatusDTO toResponseUpdateStatusDTO(Agreement agreement);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    AgreementDTO toAgreementDTOFromAgreement(Agreement agreement);
}
