package ru.pozhar.collector_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.dto.RequestAddressDTO;
import ru.pozhar.collector_api.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.dto.ResponseUpdateAddressDTO;
import ru.pozhar.collector_api.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("api/debtors/{debtorId}/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PutMapping
    public ResponseEntity<ResponseUpdateAddressDTO> updateDebtorAddress(
            @PathVariable Long debtorId,
            @RequestBody @Valid RequestAddressDTO requestAddressDTO
            ) {
        ResponseUpdateAddressDTO responseAddressDTO = addressService
                .updateAddress(debtorId, requestAddressDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + responseAddressDTO.debtorId() + "/address")
                .body(responseAddressDTO);
    }

    @GetMapping
    public ResponseEntity<List<ResponseAddressDTO>> getDebtorAddresses(
            @PathVariable Long debtorId) {
        List<ResponseAddressDTO> responseAddressDTOs = addressService.getDebtorAddresses(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + debtorId + "/address")
                .body(responseAddressDTOs);
    }
}
