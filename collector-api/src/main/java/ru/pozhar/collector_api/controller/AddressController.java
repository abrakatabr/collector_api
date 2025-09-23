package ru.pozhar.collector_api.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<ResponseUpdateAddressDTO> updateDebtorAddress(Long debtorId,
                                                                        RequestAddressDTO requestAddressDTO) {
        ResponseUpdateAddressDTO responseAddressDTO = addressService
                .updateAddress(debtorId, requestAddressDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + responseAddressDTO.getDebtorId() + "/address")
                .body(responseAddressDTO);
    }

    @Override
    public ResponseEntity<List<ResponseAddressDTO>> getDebtorAddresses(Long debtorId) {
        List<ResponseAddressDTO> responseAddressDTOs = addressService.getDebtorAddresses(debtorId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Location", "api/debtors/"
                        + debtorId + "/address")
                .body(responseAddressDTOs);
    }

    @Override
    public ResponseEntity<Void> deleteAddress(
            Long debtorId,
            @Pattern(regexp = "registration|residential", message = "Неверный статус адреса в запросе")
            AddressStatus addressStatus) {
        addressService.deleteAddress(debtorId, addressStatus);
        return ResponseEntity.noContent().build();
    }
}
