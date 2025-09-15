package ru.pozhar.collector_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import ru.pozhar.collector_api.model.Gender;

import java.time.LocalDate;
import java.util.List;

public record RequestDebtorDTO(
        @NotBlank(message = "Имя обязательно")
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z-]+$", message = "Имя может содержать только буквы и дефис")
        String firstname,
        @NotBlank(message = "Фамилия обязательна")
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z-]+$", message = "Фамилия может содержать только буквы и дефис")
        String lastname,
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z-]+$", message = "Отчество может содержать только буквы и дефис")
        String patronymic,
        @NotNull(message = "Дата рождения обязательна")
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthday,
        @NotNull(message = "Пол обязателен")
        Gender gender,
        @NotBlank(message = "Роль заемщика в договоре обязательна.")
        @Pattern(regexp = "co-debtor|single debtor|guarantor|charger",
                message = "Роль заемщика в договоре должна быть 'co-debtor', 'single debtor', 'guarantor' или 'charger'")
        String role,
        @Pattern(regexp = "^(\\+7|7|8)?[0-9\\s\\-\\(\\)]{10,15}$",
                message = "Некорректный формат номера телефона")
        String phoneNumber,
        @Valid
        List<@Valid RequestAddressDTO> addressDTOs,
        @Valid
        List<@Valid RequestDocumentDTO> documentDTOs) { }
