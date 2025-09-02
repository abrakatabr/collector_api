package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateStatusDTO;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.AgreementStatus;

@Service
public interface AgreementService {
    ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO, Long key);

    void deleteAgreement(Long agreementId);

    Agreement findAgreementById(Long agreementId);

    ResponseUpdateStatusDTO updateAgreementStatus(Long agreementId, AgreementStatus agreementStatus);
}
