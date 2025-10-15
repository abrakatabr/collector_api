package ru.pozhar.parser.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseExcelService implements TableParser {
    private final DataFormatter dataFormatter = new DataFormatter();

    @Override
    public LinkedList<RequestAgreementDTO> parse(MultipartFile file) {
        LinkedList<RequestAgreementDTO> agreementDTOs = new LinkedList<>();
        try (Workbook workbook = getWorkBook(file)) {
            Sheet sheet = workbook.cloneSheet(0);
            Map<String, Integer> agreementLocator = new HashMap<>();
            Map<String, Integer> debtorLocator = new HashMap<>();
            Map<String, Integer> addressLocator = new HashMap<>();
            Map<String, Integer> documentLocator = new HashMap<>();
            int startRow = 0;
            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
                CellRangeAddress mergedCells = sheet.getMergedRegion(i);
                if (isAgreementZone(sheet, mergedCells)) {
                    scanAgreement(sheet, mergedCells, agreementLocator);
                }
                if (isDebtorZone(sheet, mergedCells)) {
                    scanDebtor(sheet, mergedCells, debtorLocator);
                }
                if (isAddressZone(sheet, mergedCells)) {
                    scanAddress(sheet, mergedCells, addressLocator);
                }
                if (isDocumentZone(sheet, mergedCells)) {
                    scanDocument(sheet, mergedCells, documentLocator);
                }
                startRow = Math.max(startRow, mergedCells.getLastRow() + 2);
            }
            int lastRow = 0;
            for (int i = 0;sheet.getRow(i) != null && !dataFormatter.formatCellValue(sheet.getRow(i).getCell(0)).isEmpty(); i++) {
                lastRow = i;
            }
            for (int i = startRow; i <= lastRow; i++) {
                Row row = sheet.getRow(i);
                RequestAgreementDTO agreementDTO= parseAgreement(row, agreementLocator);
                parseDebtor(agreementDTO, row, debtorLocator);
                parseAddress(agreementDTO, row, addressLocator);
                parseDocument(agreementDTO, row, documentLocator);
                agreementDTOs.add(agreementDTO);
            }
        } catch (Exception ex) {
            log.error("Исключение при парсинге Excel файла {}. Ошибка {}", file.getName(), ex.getMessage(), ex);
            throw new RuntimeException("Исключение при парсинге Excel файла {}");
        }
        return agreementDTOs;
    }

    private Workbook getWorkBook(MultipartFile file) throws IOException {
        boolean isXls = file.getOriginalFilename().toLowerCase().endsWith("xls");
        if (isXls) {
            return new HSSFWorkbook(file.getInputStream());
        } else {
            return new XSSFWorkbook(file.getInputStream());
        }
    }

    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("M/d/yy"),
                DateTimeFormatter.ofPattern("MM/dd/yy"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("d.M.yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate date =  LocalDate.parse(dateString.trim(), formatter);
                if (date.getYear() > LocalDate.now().getYear()) {
                    date = date.minusYears(100);
                }
                return date;
            } catch (DateTimeParseException e) { }
        }
        return null;
    }

    private void parseDocument(RequestAgreementDTO agreementDTO, Row row, Map<String, Integer> documentLocator) {
        List<RequestDocumentDTO> documentDTOs = new ArrayList<>();
        RequestDocumentDTO document = new RequestDocumentDTO();
        document.setDocumentNumber(dataFormatter.formatCellValue(row.getCell(documentLocator.get("documentNumber"))));
        document.setIssueDate(parseDate(dataFormatter.formatCellValue(row.getCell(documentLocator.get("issueDate")))));
        DocumentType documentType = parseDocumentType(row.getCell(documentLocator.get("documentType")).getStringCellValue());
        document.setDocumentType(documentType);
        documentDTOs.add(document);
        agreementDTO.getDebtors().stream().findFirst().get().setDocumentDTOs(documentDTOs);
    }

    private DocumentType parseDocumentType(String value) {
        DocumentType documentType = null;
        if (value.matches("(?iu)\\bпаспорт.*\\b(рф|росс).*")) {
            documentType = DocumentType.NATIONAL_PASSPORT;
        }
        if (value.matches("(?iu).*\\bзагран.*")) {
            documentType = DocumentType.INTERNATIONAL_PASSPORT;
        }
        if (value.matches("(?iu)\\b(инн|идентифик).*")) {
            documentType = DocumentType.INN;
        }
        if (value.matches("(?iu).*\\bводит.*")) {
            documentType = DocumentType.DRIVER_LICENSE;
        }
        if (value.matches("(?iu).*\\b(страхов|снилс).*")) {
            documentType = DocumentType.SNILS;
        }
        if (documentType == null) {
            log.warn("Тип документа не определен");
        }
        return documentType;
    }

    private void parseAddress(RequestAgreementDTO agreementDTO, Row row, Map<String, Integer> addressLocator) {
        List<RequestAddressDTO> addressDTOs = new ArrayList<>();
        RequestAddressDTO address = new RequestAddressDTO();
        AddressStatus addressStatus = parseAddressStatus(row.getCell(addressLocator.get("addressStatus")).getStringCellValue());
        address.setAddressStatus(addressStatus);
        Map<String, String> parsedAddress = parseStringAddress(row.getCell(addressLocator.get("address")).getStringCellValue());
        address.setCountry(parsedAddress.get("country"));
        address.setCity(parsedAddress.get("city"));
        address.setStreet(parsedAddress.get("street"));
        address.setHouse(parsedAddress.get("house"));
        address.setApartment(parsedAddress.get("apartment"));
        addressDTOs.add(address);
        agreementDTO.getDebtors().stream().findFirst().get().setAddressDTOs(addressDTOs);
    }

    public static Map<String, String> parseStringAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> result = new HashMap<>();
        Pattern pattern = Pattern.compile(
                "(?iu)(.*?)\\s+г\\.?\\s*(.*?)\\s+ул\\.?\\s*(.*?)\\s+д\\.?\\s*(.*?)(?:\\s+кв\\.?\\s*(.*))?"
        );
        Matcher matcher = pattern.matcher(address);
        if (matcher.matches()) {
            result.put("country", matcher.group(1).trim());
            result.put("city", matcher.group(2).trim());
            result.put("street", matcher.group(3).trim());
            result.put("house", matcher.group(4).trim());
            if (matcher.group(5) != null) {
                result.put("apartment", matcher.group(5).trim());
            }
        }
        return result;
    }

    private AddressStatus parseAddressStatus(String value) {
        AddressStatus addressStatus;
        if (value.matches("(?iu)\\b(регистрац|пропис).*")) {
            addressStatus = AddressStatus.REGISTRATION;
        } else {
            addressStatus = AddressStatus.RESIDENTIAL;
        }
        return  addressStatus;
    }

    private void parseDebtor(RequestAgreementDTO agreementDTO, Row row, Map<String, Integer> debtorLocator) {
        List<RequestDebtorDTO> debtorDTOs = new ArrayList<>();
        RequestDebtorDTO debtor = new RequestDebtorDTO();
        String[] fio = row.getCell(debtorLocator.get("fio")).getStringCellValue().split("\\s+");
        debtor.setLastname(fio[0]);
        debtor.setFirstname(fio[1]);
        debtor.setPatronymic(fio[2]);
        debtor.setBirthday(parseDate(dataFormatter.formatCellValue(row.getCell(debtorLocator.get("birthday")))));
        String genderParse = row.getCell(debtorLocator.get("gender")).getStringCellValue();
        Gender gender = genderParse.matches("(?iu)\\bм.*") ? Gender.MALE : Gender.FEMALE;
        debtor.setGender(gender);
        debtor.setRole(parseRole(row.getCell(debtorLocator.get("role")).getStringCellValue()));
        debtor.setPhoneNumber(row.getCell(debtorLocator.get("phoneNumber")).getStringCellValue());
        debtorDTOs.add(debtor);
        agreementDTO.setDebtors(debtorDTOs);
    }

    private Role parseRole(String value) {
        Role role = null;
        if (value.matches("(?iu)\\b(созаем|co[-_]debt).*")) {
            role = Role.CO_DEBTOR;
        }
        if (value.matches("(?iu)\\b(единствен|single).*")) {
            role = Role.SINGLE_DEBTOR;
        }
        if (value.matches("(?iu)\\b(поруч|guara).*")) {
            role = Role.GUARANTOR;
        }
        if (value.matches("(?iu)\\b(залогодат|charg).*")) {
            role = Role.CHARGER;
        }
        if (role == null) {
            log.warn("Роль заемщика не определена");
        }
        return role;
    }
    private RequestAgreementDTO parseAgreement(Row row, Map<String, Integer> agreementLocator) {
        RequestAgreementDTO agreement = new RequestAgreementDTO();
        agreement.setOriginalDebtSum(BigDecimal.valueOf(row.getCell(agreementLocator.get("originalDebtSum")).getNumericCellValue()));
        agreement.setActualDebtSum(BigDecimal.valueOf(row.getCell(agreementLocator.get("actualDebtSum")).getNumericCellValue()));
        agreement.setAgreementStartDate(parseDate(dataFormatter.formatCellValue(row.getCell(agreementLocator.get("agreementStartDate")))));
        agreement.setTransferor(row.getCell(agreementLocator.get("transferor")).getStringCellValue());
        return agreement;
    }

    private boolean isAgreementZone(Sheet sheet, CellRangeAddress mergedCells) {
        Cell cell = getCell(sheet, mergedCells.getFirstRow(), mergedCells.getFirstColumn());
        String value = cell.getStringCellValue();
        return value.matches("(?iu)\\bдоговор\\b");
    }

    private boolean isDebtorZone(Sheet sheet, CellRangeAddress mergedCells) {
        Cell cell = getCell(sheet, mergedCells.getFirstRow(), mergedCells.getFirstColumn());
        String value = cell.getStringCellValue();
        return value.matches("(?iu).*\\b(участник|участники|заемщик|заемщики)\\b.*");
    }

    private boolean isAddressZone(Sheet sheet, CellRangeAddress mergedCells) {
        Cell cell = getCell(sheet, mergedCells.getFirstRow(), mergedCells.getFirstColumn());
        String value = cell.getStringCellValue();
        return value.matches("(?iu).*\\b(адрес|адреса)\\b.*");
    }

    private boolean isDocumentZone(Sheet sheet, CellRangeAddress mergedCells) {
        Cell cell = getCell(sheet, mergedCells.getFirstRow(), mergedCells.getFirstColumn());
        String value = cell.getStringCellValue();
        return value.matches("(?iu).*\\b(документ|документы)\\b.*");
    }

    private void scanAgreement(Sheet sheet, CellRangeAddress mergedCells, Map<String, Integer> agreementLocator) {
        Row row = sheet.getRow(mergedCells.getLastRow() + 1);
        for (int i = mergedCells.getFirstColumn(); i <= mergedCells.getLastColumn(); i++) {
            Cell cell = row.getCell(i);
            String value = cell.getStringCellValue();
            if (value.matches("(?iu).*\\bсумма\\b.*")) {
                agreementLocator.put("originalDebtSum", i);
                continue;
            }
            if (value.matches("(?iu).*\\bостаток\\b.*")) {
                agreementLocator.put("actualDebtSum", i);
                continue;
            }
            if (value.matches("(?iu).*\\bдата\\b.*")) {
                agreementLocator.put("agreementStartDate", i);
                continue;
            }
            if (value.matches("(?iu).*\\bбанк.*")) {
                agreementLocator.put("transferor", i);
            }
        }
    }

    private void scanDebtor(Sheet sheet, CellRangeAddress mergedCells, Map<String, Integer> debtorLocator) {
        Row row = sheet.getRow(mergedCells.getLastRow() + 1);
        for (int i = mergedCells.getFirstColumn(); i <= mergedCells.getLastColumn(); i++) {
            Cell cell = row.getCell(i);
            String value = cell.getStringCellValue();
            if (value.matches("(?iu).*\\b(фио|ф\\.и\\.о\\.|ф\\.и\\.о)\\b.*")) {
                debtorLocator.put("fio", i);
                continue;
            }
            if (value.matches("(?iu).*\\bдата.*рожд.*\\b.*")) {
                debtorLocator.put("birthday", i);
                continue;
            }
            if (value.matches("(?iu).*\\bпол\\b.*")) {
                debtorLocator.put("gender", i);
                continue;
            }
            if (value.matches("(?iu).*\\bроль\\b.*")) {
                debtorLocator.put("role", i);
                continue;
            }
            if (value.matches("(?iu).*\\bтелефо.*\\b.*")) {
                debtorLocator.put("phoneNumber", i);
            }
        }
    }

    private void scanAddress(Sheet sheet, CellRangeAddress mergedCells, Map<String, Integer> addressLocator) {
        Row row = sheet.getRow(mergedCells.getLastRow() + 1);
        for (int i = mergedCells.getFirstColumn(); i <= mergedCells.getLastColumn(); i++) {
            Cell cell = row.getCell(i);
            String value = cell.getStringCellValue();
            if (value.matches("(?iu).*\\b(статус)\\b.*")) {
                addressLocator.put("addressStatus", i);
                continue;
            }
            if (value.matches("(?iu).*\\bадрес\\b.*")) {
                addressLocator.put("address", i);
            }
        }
    }

    private void scanDocument(Sheet sheet, CellRangeAddress mergedCells, Map<String, Integer> documentLocator) {
        Row row = sheet.getRow(mergedCells.getLastRow() + 1);
        for (int i = mergedCells.getFirstColumn(); i <= mergedCells.getLastColumn(); i++) {
            Cell cell = row.getCell(i);
            String value = cell.getStringCellValue();
            if (value.matches("(?iu).*\\b(тип)\\b.*")) {
                documentLocator.put("documentType", i);
                continue;
            }
            if (value.matches("(?iu).*\\bномер\\b.*")) {
                documentLocator.put("documentNumber", i);
                continue;
            }
            if (value.matches("(?iu).*\\bдата\\b.*")) {
                documentLocator.put("issueDate", i);
            }
        }
    }

    private Cell getCell (Sheet sheet, int row, int column) {
        return sheet.getRow(row).getCell(column);
    }
}
