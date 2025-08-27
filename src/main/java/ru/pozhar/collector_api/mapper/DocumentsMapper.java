package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.dto.RequestDocumentsDTO;
import ru.pozhar.collector_api.model.Documents;

@Mapper(componentModel = "spring")
public interface DocumentsMapper {
    @Mapping(target = "id", ignore = true)
    Documents toDocumentsEntity(RequestDocumentsDTO requestDocumentsDTO);
}
