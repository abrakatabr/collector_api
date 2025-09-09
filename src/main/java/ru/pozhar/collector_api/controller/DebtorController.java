package ru.pozhar.collector_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import ru.pozhar.collector_api.dto.ErrorResponse;
import ru.pozhar.collector_api.dto.RequestUpdateDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateDebtorDTO;
import ru.pozhar.collector_api.service.DebtorService;
import ru.pozhar.collector_api.service.NotificationService;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/debtors")
@RequiredArgsConstructor
public class DebtorController {

    private final DebtorService debtorService;
    private final NotificationService notificationService;

    @PatchMapping("/{debtorId}")
    public ResponseEntity<ResponseUpdateDebtorDTO> updateDebtorPhoneNumber(
            @PathVariable Long debtorId,
            @RequestBody @Valid RequestUpdateDebtorDTO debtorDTO) {
        ResponseUpdateDebtorDTO responseDebtorDTO = debtorService.updateDebtor(debtorId, debtorDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/debtors/" + responseDebtorDTO.id())
                .body(responseDebtorDTO);
    }

    @GetMapping("/{debtorId}/notification/{agreementId}")
    public ResponseEntity<String> getNotification(
            @PathVariable Long debtorId,
            @PathVariable Long agreementId,
            WebRequest request) {
        try {
            String response = notificationService.getNotification(debtorId, agreementId);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Location", "/api/debtors/"
                            + debtorId + "/notification/" + agreementId)
                    .body(response);
        } catch (IOException exception) {
                ErrorResponse response = new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        exception.getMessage(),
                        request.getDescription(false).replace("uri=", "")
                );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.toString());
        }
    }
}
