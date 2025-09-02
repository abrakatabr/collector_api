package ru.pozhar.collector_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.pozhar.collector_api.model.Agreement;
import ru.pozhar.collector_api.model.AgreementKey;

@Mapper(componentModel = "spring")
public interface AgreementKeyMapper {

    @Mapping(target = "id", ignore = true)
    AgreementKey toAgreementKeyEntity(Agreement agreement, Long key);
}
