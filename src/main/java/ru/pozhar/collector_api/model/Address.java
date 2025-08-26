package ru.pozhar.collector_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Страна обязательна для заполнения.")
    @Column(name = "country", nullable = false, length = 128)
    private String country;

    @NotBlank(message = "Город обязателен для заполнения.")
    @Column(name = "city", nullable = false, length = 128)
    private String city;

    @NotBlank(message = "Название улицы обязательно для заполнения.")
    @Column(name = "street", nullable = false, length = 128)
    private String street;

    @NotBlank(message = "Номер дома обязателен для заполнения.")
    @Column(name = "house", nullable = false, length = 50)
    private String house;

    @Column(name = "apartment", length = 50)
    private String apartment = "N/A";
}
