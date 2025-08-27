package ru.pozhar.collector_api.service;

import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestAgreementDTO;
import ru.pozhar.collector_api.dto.ResponseAgreementDTO;

@Service
public interface AgreementService {
    ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO);
}
