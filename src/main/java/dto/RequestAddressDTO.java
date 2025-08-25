package dto;

public record RequestAddressDTO(String country,
                                String city,
                                String street,
                                String house,
                                String apartment) { }
