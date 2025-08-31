package ru.pozhar.collector_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.dto.ResponseUpdatePhoneDTO;
import ru.pozhar.collector_api.service.DebtorService;

@RestController
@RequestMapping("/api/debtors")
@RequiredArgsConstructor
public class DebtorController {

    private final DebtorService debtorService;

    @PutMapping("/{debtorId}/contacts/phoneNumber/update")
    public ResponseEntity<ResponseUpdatePhoneDTO> updateDebtorPhoneNumber(
            @PathVariable Long debtorId,
            @RequestBody String phoneNumber) {
        ResponseUpdatePhoneDTO responseDebtorDTO = debtorService
                .updateDebtorPhoneNumber(debtorId, phoneNumber);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/debtors/" + responseDebtorDTO.debtorId())
                .body(responseDebtorDTO);
    }
}
