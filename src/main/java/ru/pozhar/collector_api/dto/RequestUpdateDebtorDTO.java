package ru.pozhar.collector_api.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import ru.pozhar.collector_api.model.Gender;

import java.time.LocalDate;

public record RequestUpdateDebtorDTO(
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z-]+$", message = "Имя может содержать только буквы и дефис")
        String firstname,
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z-]+$", message = "Фамилия может содержать только буквы и дефис")
        String lastname,
        @Pattern(regexp = "^[а-яА-ЯёЁa-zA-Z-]+$", message = "Отчество может содержать только буквы и дефис")
        String patronymic,
        @Past(message = "Дата рождения должна быть в прошлом")
        LocalDate birthday,
        Gender gender,
        @Pattern(regexp = "^(\\+7|7|8)?[0-9\\s\\-\\(\\)]{10,15}$",
                message = "Некорректный формат номера телефона")
        String phoneNumber) { }
