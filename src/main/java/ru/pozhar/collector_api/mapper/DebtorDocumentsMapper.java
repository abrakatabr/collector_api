package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.model.Debtor;
import ru.pozhar.collector_api.model.DebtorDocuments;
import ru.pozhar.collector_api.model.Documents;

@Mapper(componentModel = "spring")
public interface DebtorDocumentsMapper {
    @Mapping(target = "id", ignore = true)
    DebtorDocuments toDebtorDocumentsEntity(Debtor debtor, Documents documents);
}
