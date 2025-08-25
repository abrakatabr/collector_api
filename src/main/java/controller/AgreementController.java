package controller;

import dto.RequestAgreementDTO;
import dto.ResponseAgreementDTO;
import lombok.RequiredArgsConstructor;
import model.Address;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.AgreementService;

@RestController
@RequestMapping("/api/agreement")
@RequiredArgsConstructor
public class AgreementController {
    private final AgreementService agreementService;

    @PostMapping
    public ResponseEntity<ResponseAgreementDTO> createAgreement(
            @RequestBody RequestAgreementDTO requestAgreementDTO) {
        ResponseAgreementDTO responseAgreementDTO = agreementService.createAgreement(requestAgreementDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "api/agreements/" + responseAgreementDTO.id())
                .body(responseAgreementDTO);
    }
}
