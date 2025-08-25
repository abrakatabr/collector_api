package service;

import dto.RequestAgreementDTO;
import dto.ResponseAgreementDTO;

public interface AgreementService {
    ResponseAgreementDTO createAgreement(RequestAgreementDTO requestAgreementDTO);
}
