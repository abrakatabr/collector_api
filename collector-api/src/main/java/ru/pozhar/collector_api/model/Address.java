package ru.pozhar.collector_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.pozhar.collector_api.openapi.dto.AddressStatus;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "ID заемщика обязателен")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "debtor_id", nullable = false)
    private Debtor debtor;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "address_status", length = 20)
    private AddressStatus addressStatus = AddressStatus.REGISTRATION;
}
