package ru.pozhar.collector_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.dto.RequestUpdateDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateDebtorDTO;
import ru.pozhar.collector_api.service.DebtorService;

@RestController
@RequestMapping("/api/debtors")
@RequiredArgsConstructor
public class DebtorController {

    private final DebtorService debtorService;

    @PatchMapping("/{debtorId}")
    public ResponseEntity<ResponseUpdateDebtorDTO> updateDebtorPhoneNumber(
            @PathVariable Long debtorId,
            @RequestBody @Valid RequestUpdateDebtorDTO debtorDTO) {
        ResponseUpdateDebtorDTO responseDebtorDTO = debtorService.updateDebtor(debtorId, debtorDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "/api/debtors/" + responseDebtorDTO.id())
                .body(responseDebtorDTO);
    }
}
