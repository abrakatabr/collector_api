package ru.pozhar.collector_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.dto.RequestUpdateDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseGetDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateDebtorDTO;
import ru.pozhar.collector_api.service.DebtorService;
import ru.pozhar.collector_api.service.NotificationService;

import java.io.IOException;

@RestController
@RequestMapping("/api/debtors")
@RequiredArgsConstructor
public class DebtorController {

    private final DebtorService debtorService;
    private final NotificationService notificationService;

    @PatchMapping("/{debtorId}")
    public ResponseEntity<ResponseUpdateDebtorDTO> updateDebtor(
            @PathVariable Long debtorId,
            @RequestBody @Valid RequestUpdateDebtorDTO debtorDTO) {
        ResponseUpdateDebtorDTO responseDebtorDTO = debtorService.updateDebtor(debtorId, debtorDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/debtors/" + responseDebtorDTO.id())
                .body(responseDebtorDTO);
    }

    @GetMapping("/{debtorId}")
    public ResponseEntity<ResponseGetDebtorDTO> getDebtor(@PathVariable Long debtorId) {
        ResponseGetDebtorDTO response = debtorService.getDebtor(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.LOCATION, "/api/debtors/" + response.id())
                .body(response);
    }

    @GetMapping("/{debtorId}/notification/{agreementId}")
    public ResponseEntity<ByteArrayResource> getNotification(
            @PathVariable Long debtorId,
            @PathVariable Long agreementId) throws IOException {
            ByteArrayResource response = notificationService.getNotification(debtorId, agreementId);
            String filename = "notification_" + debtorId + "_" + agreementId + ".txt";
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(response.getByteArray().length))
                    .header("Location", "/api/debtors/"
                            + debtorId + "/notification/" + agreementId)
                    .body(response);
    }
}
