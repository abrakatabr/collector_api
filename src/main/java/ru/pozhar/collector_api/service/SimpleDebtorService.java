package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
import ru.pozhar.collector_api.dto.ResponseDebtorDTO;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.repository.DebtorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleDebtorService implements DebtorService{
    private final AddressService addressService;
    private final DebtorMapper debtorMapper;
    private final DebtorRepository debtorRepository;

    @Override
    public List<ResponseDebtorDTO> createDebtors(List<RequestDebtorDTO> requestDebtorDTOList) {
        List<Debtor> debtors = debtorMapper.toDebtorEntityList(requestDebtorDTOList);
        debtors.stream()
                .peek(d -> d.setAddress(addressService.initAddress(d.getAddress())))
                .collect(Collectors.toList());
        debtors = debtorRepository.saveAll(debtors);
        return debtorMapper.toResponseDebtorDTOList(debtors);
    }
}
