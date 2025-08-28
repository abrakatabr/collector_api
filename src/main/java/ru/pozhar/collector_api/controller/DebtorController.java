package ru.pozhar.collector_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.RequestUpdateDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateDebtorDTO;
import ru.pozhar.collector_api.service.DebtorService;

@RestController
@RequestMapping("/api/debtors")
@RequiredArgsConstructor
public class DebtorController {

    private final DebtorService debtorService;

    @PutMapping("/{debtorId}/contacts/phoneNumber/update")
    public ResponseEntity<ResponseUpdateDebtorDTO> updateDebtorPhoneNumber(
            @PathVariable Long debtorId,
            @RequestBody RequestUpdateDebtorDTO requestUpdateDebtorDTO) {
        ResponseUpdateDebtorDTO responseDebtorDTO = debtorService
                .updateDebtorPhoneNumber(requestUpdateDebtorDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/debtors/" + responseDebtorDTO.id())
                .body(responseDebtorDTO);
    }
}
