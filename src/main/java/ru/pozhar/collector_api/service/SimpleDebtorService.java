package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pozhar.collector_api.dto.*;
import ru.pozhar.collector_api.mapper.AddressMapper;
import ru.pozhar.collector_api.mapper.DebtorMapper;
import ru.pozhar.collector_api.mapper.DocumentsMapper;
import ru.pozhar.collector_api.model.Address;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorAddress;
import ru.pozhar.collector_api.model.Documents;
import ru.pozhar.collector_api.repository.DebtorRepository;
import ru.pozhar.collector_api.repository.DocumentsRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleDebtorService implements DebtorService{

    private final DebtorMapper debtorMapper;

    private final DebtorRepository debtorRepository;

    private final DocumentsRepository documentsRepository;

    private final DocumentsMapper documentsMapper;

    private final AddressMapper addressMapper;

    private final AddressService addressService;

    private final DebtorDocumentsService debtorDocumentsService;

    private final DocumentsService documentsService;

    private final DebtorAddressService debtorAddressService;

    @Transactional
    @Override
    public Debtor initDebtor(RequestDebtorDTO debtorDTO) {
        Debtor debtor = debtorMapper.toDebtorEntity(debtorDTO);
        Optional<Documents> documentsOptional = documentsRepository
                .findByPassportNumber(debtorDTO.documentsDTO().passportNumber());
        if (documentsOptional.isPresent()) {
            Optional<Debtor> debtorOptional = debtorRepository.findByDocumentsId(documentsOptional.get().getId());
            if (debtorOptional.isPresent()) {
                debtor = debtorOptional.get();
                if (!validateDebtor(debtor, documentsOptional.get(), debtorDTO)) {
                    throw new RuntimeException("В базе данных есть другой заемщик с такими доку ментами");
                }
            } else {
                throw new RuntimeException("По паспортным данным не найден заемщик в базе даных");
            }
        } else {
            debtor = debtorRepository.save(debtor);
        }
       return debtor;
    }

    @Transactional
    @Override
    public ResponseUpdateDebtorDTO updateDebtorPhoneNumber(RequestUpdateDebtorDTO updateDebtorDTO) {
        Optional<Debtor> debtorOptional = debtorRepository.findById(updateDebtorDTO.id());
        if (debtorOptional.isEmpty()) {
            throw new RuntimeException("Заемщик не найден в базе данных");
        }
        Debtor debtor = debtorOptional.get();
        debtor.setPhoneNumber(updateDebtorDTO.phoneNumber());
        debtor = debtorRepository.save(debtor);
        List<Address> addresses = debtorAddressService
                .findAddressesIdByDebtorId(debtor.getId()).stream()
                .map(id -> addressService.findAddressById(id))
                .collect(Collectors.toList());
        List<ResponseAddressDTO> addressDTOs = new LinkedList<>();
        for (Address address : addresses) {
            DebtorAddress debtorAddress = debtorAddressService
                    .findDebtorAddressByDebtorIdAndAddressId(debtor.getId(), address.getId());
            ResponseAddressDTO addressDTO = addressMapper.toResponseAddressDTO(address, debtorAddress);
            addressDTOs.add(addressDTO);
        }
        Documents documents = documentsService
                .findDocumentsById(debtorDocumentsService.findByDebtorId(debtor.getId()).getDocuments().getId());
        ResponseDocumentsDTO documentsDTO = documentsMapper.toResponseDocumentDTO(documents);
        return debtorMapper.toResponseUpdateDebtorDTO(debtor, addressDTOs, documentsDTO);
    }

    private boolean validateDebtor(Debtor debtor, Documents documents, RequestDebtorDTO debtorDTO) {
        boolean isValidInn = true;
        boolean isValidSnils = true;
        if (!documents.getInn().isBlank() && documents.getInn() != null) {
            if (!debtorDTO.documentsDTO().inn().isBlank() && debtorDTO.documentsDTO().inn() != null) {
                isValidInn = documents.getInn().equals(debtorDTO.documentsDTO().inn());
            }
        }
        if (!documents.getSnils().isBlank() && documents.getSnils() != null) {
            if (!debtorDTO.documentsDTO().snils().isBlank() && debtorDTO.documentsDTO().snils() != null) {
                isValidSnils = documents.getSnils().equals(debtorDTO.documentsDTO().snils());
            }
        }
        return debtor.getFirstname().equals(debtorDTO.firstname())
                && debtor.getLastname().equals(debtorDTO.lastname())
                && debtor.getPatronymic().equals((debtorDTO.patronymic()))
                && debtor.getBirthday().isEqual(debtorDTO.birthday())
                && documents.getPassportNumber().equals(debtorDTO.documentsDTO().passportNumber())
                &&  isValidInn && isValidSnils;
    }
}
