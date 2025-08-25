package mapper;

import dto.RequestAddressDTO;
import dto.ResponseAddressDTO;
import model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    ResponseAddressDTO toResponseAddressDTO(Address address);

    @Mapping(target = "id", ignore = true)
    Address toAddressEntity(RequestAddressDTO addressDTO);
}