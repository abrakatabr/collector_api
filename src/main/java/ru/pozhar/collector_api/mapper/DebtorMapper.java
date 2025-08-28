package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.*;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DebtorMapper {
    @Mapping(target = "id", ignore = true)
    Debtor toDebtorEntity(RequestDebtorDTO requestDebtorDTO);

    @Mapping(target = "id", source = "debtor.id")
    @Mapping(target = "role", source = "debtorAgreement.role")
    @Mapping(target = "addressDTOs", source = "addressDTOList")
    ResponseDebtorDTO toResponseDebtorDTO(Debtor debtor,
                                          DebtorAgreement debtorAgreement,
                                          List<ResponseAddressDTO> addressDTOList,
                                          ResponseDocumentsDTO documentsDTO);

    @Mapping(target = "id", source = "debtor.id")
    @Mapping(target = "addressDTOs", source = "addressDTOs")
    @Mapping(target = "documentsDTO", source = "documentsDTO")
    ResponseUpdateDebtorDTO toResponseUpdateDebtorDTO(Debtor debtor,
                                                      List<ResponseAddressDTO> addressDTOs,
                                                      ResponseDocumentsDTO documentsDTO);
}
