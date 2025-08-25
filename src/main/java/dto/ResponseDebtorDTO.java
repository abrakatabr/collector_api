package dto;

import model.Gender;
import java.time.LocalDate;

public record ResponseDebtorDTO(Long id,
                                String firstname,
                                String lastname,
                                String patronymic,
                                String passportNumber,
                                ResponseAddressDTO address,
                                LocalDate birthday,
                                Gender gender,
                                String phoneNumber,
                                String debtorType) { }
