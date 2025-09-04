package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateDebtorDTO;
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
    @Mapping(target = "documentDTOs", source = "documentDTOList")
    ResponseDebtorDTO toResponseDebtorDTO(Debtor debtor,
                                          DebtorAgreement debtorAgreement,
                                          List<ResponseAddressDTO> addressDTOList,
                                          List<ResponseDocumentDTO> documentDTOList);

    ResponseUpdateDebtorDTO toResponseUpdateDebtorDTO(Debtor debtor);
}
