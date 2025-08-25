package service;

import dto.RequestAgreementDTO;
import dto.ResponseAgreementDTO;
import lombok.AllArgsConstructor;
import mapper.AgreementMapper;
import mapper.DebtorAgreementMapper;
import mapper.DebtorMapper;
import model.Address;
import model.Agreement;
import model.Debtor;
import model.DebtorAgreement;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AddressRepository;
import repository.AgreementRepository;
import repository.DebtorAgreementRepository;
import repository.DebtorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SimpleAgreementService implements AgreementService {
    private final AgreementMapper agreementMapper;

    private final DebtorMapper debtorMapper;

    private final DebtorAgreementMapper debtorAgreementMapper;

    private final AddressRepository addressRepository;

    private final AgreementRepository agreementRepository;

    private final DebtorRepository debtorRepository;

    private final DebtorAgreementRepository debtorAgreementRepository;

    @Override
    @Transactional
    public ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO) {
        Agreement agreement = agreementMapper.toAgreementEntity(requestAgreementDTO);
        List<Debtor> debtors = debtorMapper.toDebtorEntityList(requestAgreementDTO.debtors());
        List<Address> addresses = debtors.stream()
                        .map(d -> d.getAddress()).collect(Collectors.toList());
        addressRepository.saveAll(addresses);
        Agreement createdAgreement = agreementRepository.save(agreement);
        List<DebtorAgreement> debtorAgreements = debtorAgreementMapper.toDebtorAgreementEntityList(requestAgreementDTO,
                createdAgreement);
        List<Debtor> createdDebtors = debtorRepository.saveAll(debtors);
        return agreementMapper.toResponseAgreementDTO(createdAgreement, createdDebtors);
    }
}
