package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.pozhar.collector_api.openapi.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DebtorAgreementMapper {
    @Mapping(source = "requestDebtorDTO.role", target = "role")
    @Mapping(target = "id", ignore = true)
    DebtorAgreement toDebtorAgreementEntity(Debtor debtor, Agreement agreement, RequestDebtorDTO requestDebtorDTO);
}
