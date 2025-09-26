package ru.pozhar.collector_api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import ru.pozhar.collector_api.openapi.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.RequestUpdateDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseGetDebtorDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseUpdateDebtorDTO;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import ru.pozhar.collector_api.openapi.dto.UpdateAgreementDebtorNotification;
import ru.pozhar.collector_api.openapi.dto.Role;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DebtorMapper {
    @Mapping(target = "id", ignore = true)
    Debtor toDebtorEntity(RequestDebtorDTO requestDebtorDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Debtor toUpdateDebtorEntity(@MappingTarget Debtor debtor, RequestUpdateDebtorDTO requestDebtorDTO);

    @Mapping(target = "id", source = "debtor.id")
    @Mapping(target = "role", source = "debtorAgreement.role")
    @Mapping(target = "addressDTOs", source = "addressDTOList")
    @Mapping(target = "documentDTOs", source = "documentDTOList")
    ResponseDebtorDTO toResponseDebtorDTO(Debtor debtor,
                                          DebtorAgreement debtorAgreement,
                                          List<ResponseAddressDTO> addressDTOList,
                                          List<ResponseDocumentDTO> documentDTOList);

    @Mapping(target = "id", source = "debtor.id")
    @Mapping(target = "addressDTOs", source = "addressDTOList")
    @Mapping(target = "documentDTOs", source = "documentDTOList")
    ResponseGetDebtorDTO toResponseGetDebtorDTO(Debtor debtor,
                                             List<ResponseAddressDTO> addressDTOList,
                                             List<ResponseDocumentDTO> documentDTOList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", source = "role")
    UpdateAgreementDebtorNotification toDebtorNotification(Debtor debtor, Role role);

    ResponseUpdateDebtorDTO toResponseUpdateDebtorDTO(Debtor debtor);
}
