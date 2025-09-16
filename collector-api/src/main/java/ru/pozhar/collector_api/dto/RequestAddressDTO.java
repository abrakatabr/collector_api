package ru.pozhar.collector_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RequestAddressDTO(
        @NotBlank(message = "Страна обязательна")
        String country,
        @NotBlank(message = "Название города обязательно")
        String city,
        @NotBlank(message = "Название улицы обязательно")
        String street,
        @NotBlank(message = "Дом обязателен")
        String house,
        String apartment,
        @NotBlank(message = "Статус обязателен")
        @Pattern(regexp = "registration|residential",
                message = "Статус адреса должен быть 'registration' или 'residential'")
        String addressStatus) { }

