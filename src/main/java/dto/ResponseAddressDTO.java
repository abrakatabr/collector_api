package dto;

public record ResponseAddressDTO(Long id,
                                 String country,
                                 String city,
                                 String street,
                                 String house,
                                 String apartment) {
}
