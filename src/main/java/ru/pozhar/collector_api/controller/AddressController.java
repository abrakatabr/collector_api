package ru.pozhar.collector_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateAddressDTO;
import ru.pozhar.collector_api.service.AddressService;

@RestController
@RequestMapping("api/debtors/{debtorId}/contacts")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PutMapping("/address")
    public ResponseEntity<ResponseUpdateAddressDTO> updateDebtorAddress(
            @PathVariable Long debtorId,
            @RequestBody @Valid RequestAddressDTO requestAddressDTO
            ) {
        ResponseUpdateAddressDTO responseAddressDTO = addressService
                .updateAddress(debtorId, requestAddressDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/" + responseAddressDTO.debtorId())
                .body(responseAddressDTO);
    }
}
