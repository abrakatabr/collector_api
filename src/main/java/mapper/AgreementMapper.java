package mapper;

import dto.RequestAgreementDTO;
import dto.ResponseAgreementDTO;
import dto.ResponseDebtorDTO;
import model.Agreement;
import model.Debtor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
