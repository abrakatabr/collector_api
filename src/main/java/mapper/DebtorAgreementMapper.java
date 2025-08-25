package mapper;

import dto.RequestAgreementDTO;
import model.Agreement;
import model.DebtorAgreement;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DebtorMapper.class})
public abstract class DebtorAgreementMapper {
    @Autowired
    DebtorMapper debtorMapper;

    public List<DebtorAgreement> toDebtorAgreementEntityList(RequestAgreementDTO requestAgreementDTO,
                                                             Agreement agreement) {
        String debtorType = requestAgreementDTO.debtors().size() > 1 ? "co-debtor" : "single debtor";
        List<DebtorAgreement> debtorAgreements = requestAgreementDTO.debtors().stream()
                .map(d -> DebtorAgreement.builder()
                        .debtor(debtorMapper.toDebtorEntity(d))
                        .agreement(agreement)
                        .debtorType(debtorType)
                        .build())
                .collect(Collectors.toList());
        return debtorAgreements;
    }
}
