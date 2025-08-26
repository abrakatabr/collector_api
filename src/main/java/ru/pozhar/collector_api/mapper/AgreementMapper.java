package ru.pozhar.collector_api.mapper;

import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DebtorMapper.class})
public abstract class AgreementMapper {
    @Autowired
    DebtorMapper debtorMapper;

    public abstract Agreement toAgreementEntity(RequestAgreementDTO requestAgreementDTO);

    public ResponseAgreementDTO toResponseAgreementDTO(Agreement agreement, List<Debtor> debtors) {
        List<ResponseDebtorDTO> debtorDTOS = debtorMapper.toResponseDebtorDTOList(debtors);
        ResponseAgreementDTO responseAgreementDTO = new ResponseAgreementDTO(agreement.getId(),
                agreement.getOriginalDebtSum(), agreement.getActualDebtSum(), agreement.getAgreementStartDate(),
                agreement.getTransferor(), debtorDTOS);
        return responseAgreementDTO;
    }
}
