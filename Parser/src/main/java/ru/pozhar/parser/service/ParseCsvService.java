package ru.pozhar.parser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.parser.openapi.dto.AddressStatus;
import ru.pozhar.parser.openapi.dto.DocumentType;
import ru.pozhar.parser.openapi.dto.Gender;
import ru.pozhar.parser.openapi.dto.RequestAddressDTO;
import ru.pozhar.parser.openapi.dto.RequestAgreementDTO;
import ru.pozhar.parser.openapi.dto.RequestDebtorDTO;
import ru.pozhar.parser.openapi.dto.RequestDocumentDTO;
import ru.pozhar.parser.openapi.dto.Role;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParseCsvService {

    public List<List<RequestAgreementDTO>> parseCsv(MultipartFile file) {
        int[] existFlags = new int[4];
        LinkedList<RequestAgreementDTO> agreementDTOs = new LinkedList<>();
        try(Reader reader = new InputStreamReader(file.getInputStream());
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            for (CSVRecord record : parser) {
                if (Integer.parseInt(record.get("agreement_number")) != existFlags[0]) {
                    agreementDTOs.addLast(parseAgreement(record, existFlags));
                    continue;
                }
                if (Integer.parseInt(record.get("debtor_number")) != existFlags[1]) {
                    agreementDTOs.getLast().addDebtorsItem(parseDebtor(record, existFlags));
                    continue;
                }
                List<RequestDebtorDTO> debtors = agreementDTOs.getLast().getDebtors();
                if (Integer.parseInt(record.get("address_number")) != existFlags[2]) {
                    debtors.get(debtors.size() - 1).addAddressDTOsItem(parseAddress(record, existFlags));
                }
                if (Integer.parseInt(record.get("number_of_document")) != existFlags[3]) {
                    debtors.get(debtors.size() - 1).addDocumentDTOsItem(parseDocuments(record, existFlags));
                }
            }
        } catch (Exception ex) {
            log.error("Исключение при парсинге CSV файла {}. Ошибка {}", file.getName(), ex.getMessage(), ex);
            throw new RuntimeException("Исключение при парсинге CSV файла {}");
        }
        List<List<RequestAgreementDTO>> agreementBatches =
                IntStream.iterate(0, i -> i < agreementDTOs.size(), i -> i + 5)
                .mapToObj(i -> agreementDTOs.subList(i, Math.min(i + 5, agreementDTOs.size())))
                .collect(Collectors.toList());
        return agreementBatches;
    }

    private RequestAgreementDTO parseAgreement(CSVRecord record, int[] existFlags) {
        RequestAgreementDTO agreement = new RequestAgreementDTO();
        List<RequestDebtorDTO> debtors = new ArrayList<>();
        agreement.setOriginalDebtSum(new BigDecimal(record.get("originalDebtSum")));
        agreement.setActualDebtSum(new BigDecimal(record.get("actualDebtSum")));
        agreement.setAgreementStartDate(LocalDate.parse(record.get("agreementStartDate")));
        agreement.setTransferor(record.get("transferor"));
        debtors.add(parseDebtor(record, existFlags));
        agreement.setDebtors(debtors);
        existFlags[0] = Integer.parseInt(record.get("agreement_number"));
        return agreement;
    }
    private RequestDebtorDTO parseDebtor(CSVRecord record, int[] existFlags) {
        RequestDebtorDTO debtor = new RequestDebtorDTO();
        List<RequestAddressDTO> addresses = new ArrayList<>();
        List<RequestDocumentDTO> documents = new ArrayList<>();
        debtor.setFirstname(record.get("debtor_firstname"));
        debtor.setLastname(record.get("debtor_lastname"));
        debtor.setPatronymic(record.get("debtor_patronymic"));
        debtor.setBirthday(LocalDate.parse(record.get("debtor_birthday")));
        debtor.setGender(Gender.fromValue(record.get("debtor_gender")));
        debtor.setRole(Role.fromValue(record.get("debtor_role")));
        debtor.setPhoneNumber(record.get("debtor_phoneNumber"));
        addresses.add(parseAddress(record, existFlags));
        debtor.setAddressDTOs(addresses);
        documents.add(parseDocuments(record, existFlags));
        debtor.setDocumentDTOs(documents);
        existFlags[1] = Integer.parseInt(record.get("debtor_number"));
        return debtor;
    }

    private RequestAddressDTO parseAddress(CSVRecord record, int[] existFlags) {
        RequestAddressDTO address = new RequestAddressDTO();
        address.setCountry(record.get("address_country"));
        address.setCity(record.get("address_city"));
        address.setStreet(record.get("address_street"));
        address.setHouse(record.get("address_house"));
        address.setApartment(record.get("address_apartment"));
        address.setAddressStatus(AddressStatus.fromValue(record.get("address_status")));
        existFlags[2] = Integer.parseInt(record.get("address_number"));
        return address;
    }

    private RequestDocumentDTO parseDocuments(CSVRecord record, int[] existFlags) {
        RequestDocumentDTO document = new RequestDocumentDTO();
        document.setDocumentType(DocumentType.fromValue(record.get("document_type")));
        document.setDocumentNumber(record.get("document_number"));
        document.setIssueDate(LocalDate.parse(record.get("document_issueDate")));
        existFlags[3] = Integer.parseInt(record.get("number_of_document"));
        return document;
    }
}
