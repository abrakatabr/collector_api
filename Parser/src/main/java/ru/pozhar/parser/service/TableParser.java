package ru.pozhar.parser.service;

import org.springframework.web.multipart.MultipartFile;
import ru.pozhar.parser.openapi.dto.RequestAgreementDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface TableParser {
    default List<List<RequestAgreementDTO>> getBatches(MultipartFile file) {
        LinkedList<RequestAgreementDTO> agreementDTOs = parse(file);
        List<List<RequestAgreementDTO>> agreementBatches =
                IntStream.iterate(0, i -> i < agreementDTOs.size(), i -> i + 5)
                        .mapToObj(i -> agreementDTOs.subList(i, Math.min(i + 5, agreementDTOs.size())))
                        .collect(Collectors.toList());
        return agreementBatches;
    }

    LinkedList<RequestAgreementDTO> parse(MultipartFile file);
}
