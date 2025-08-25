package mapper;

import dto.RequestDebtorDTO;
import dto.ResponseAddressDTO;
import dto.ResponseDebtorDTO;
import model.Debtor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public abstract class DebtorMapper {
    @Autowired
    private AddressMapper addressMapper;

    public abstract Debtor toDebtorEntity(RequestDebtorDTO debtorDTO);

    public abstract List<Debtor> toDebtorEntityList(List<RequestDebtorDTO> requestDebtorDTOList);

    public List<ResponseDebtorDTO> toResponseDebtorDTOList(List<Debtor> debtors) {
        String debtorType = debtors.size() > 1 ? "co-debtor" : "single debtor";
        return debtors.stream()
                .map(d -> {
                    ResponseAddressDTO responseAddressDTO = addressMapper.toResponseAddressDTO(d.getAddress());
                    ResponseDebtorDTO responseDebtorDTO = new ResponseDebtorDTO(d.getId(),
                            d.getFirstname(), d.getLastname(), d.getPatronymic(), d.getPassportNumber(),
                            responseAddressDTO, d.getBirthday(), d.getGender(), d.getPhoneNumber(), debtorType);
                    return responseDebtorDTO;
                }).collect(Collectors.toList());
    }
}
