package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.RequestDebtorDTO;
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

    @Transactional
    @Override
    public List<Debtor> initDebtors(List<RequestDebtorDTO> debtors) {
return null;
    }
}
