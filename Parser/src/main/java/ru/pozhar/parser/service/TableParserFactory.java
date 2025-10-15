package ru.pozhar.parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TableParserFactory {
    private final ParseExcelService parseExcelService;
    private final ParseCsvService parseCsvService;

    public TableParser getTableParser(String fileName) {
        if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return parseExcelService;
        }
        if (fileName.endsWith(".csv")) {
            return parseCsvService;
        }
        throw new IllegalArgumentException("Неверный формат файла");
    }
}
