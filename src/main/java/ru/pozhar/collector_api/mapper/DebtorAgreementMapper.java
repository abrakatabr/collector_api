package ru.pozhar.collector_api.mapper;

import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAgreement;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {DebtorMapper.class})
public abstract class DebtorAgreementMapper {
    @Autowired
    DebtorMapper debtorMapper;

    public List<DebtorAgreement> toDebtorAgreementEntityList(List<Debtor> debtors,
                                                             Agreement agreement) {
        String debtorType = debtors.size() > 1 ? "co-debtor" : "single debtor";
        List<DebtorAgreement> debtorAgreements = debtors.stream()
                .map(d -> DebtorAgreement.builder()
                        .debtor(d)
                        .agreement(agreement)
                        .debtorType(debtorType)
                        .build())
                .collect(Collectors.toList());
        return debtorAgreements;
    }
}
