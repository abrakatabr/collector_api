package ru.pozhar.collector_api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.pozhar.collector_api.openapi.api.AddressApi;
import ru.pozhar.collector_api.openapi.dto.AddressStatus;
import ru.pozhar.collector_api.openapi.dto.ResponseUpdateAddressDTO;
import ru.pozhar.collector_api.openapi.dto.ResponseAddressDTO;
import ru.pozhar.collector_api.openapi.dto.RequestAddressDTO;
import ru.pozhar.collector_api.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AddressController implements AddressApi {
    private final AddressService addressService;


    @Override
    public ResponseEntity<ResponseUpdateAddressDTO> updateDebtorAddress(
            @PathVariable Long debtorId,
            @RequestBody @Valid RequestAddressDTO requestAddressDTO
            ) {
        ResponseUpdateAddressDTO responseAddressDTO = addressService
                .updateAddress(debtorId, requestAddressDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + responseAddressDTO.getDebtorId() + "/address")
                .body(responseAddressDTO);
    }

    @Override
    public ResponseEntity<List<ResponseAddressDTO>> getDebtorAddresses(
            @PathVariable Long debtorId) {
        List<ResponseAddressDTO> responseAddressDTOs = addressService.getDebtorAddresses(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + debtorId + "/address")
                .body(responseAddressDTOs);
    }

    @Override
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long debtorId,
            @Pattern(regexp = "registration|residential", message = "Неверный статус адреса в запросе")
            @RequestParam(value = "status")AddressStatus addressStatus) {
        addressService.deleteAddress(debtorId, addressStatus);
        return ResponseEntity.noContent().build();
    }
}
