package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.RequestDocumentDTO;
import ru.pozhar.collector_api.dto.ResponseDocumentDTO;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.Document;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "debtor", source = "debtor")
    Document toDocumentEntity(RequestDocumentDTO requestDocumentDTO, Debtor debtor);

    ResponseDocumentDTO toResponseDocumentDTO(Document document);
}
