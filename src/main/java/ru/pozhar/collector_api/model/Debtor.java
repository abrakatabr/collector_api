package ru.pozhar.collector_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

@Entity
@Table(name = "debtors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Debtor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя обязательно.")
    @Column(name = "firstname", nullable = false, length = 128)
    private String firstname;

    @NotBlank(message = "Фамилия обязательна.")
    @Column(name = "lastname", nullable = false, length = 128)
    private String lastname;

    @NotBlank(message = "Отчество обязательно.")
    @Column(name = "patronymic", nullable = false, length = 128)
    private String patronymic;

    @NotBlank(message = "Серия и номер пасспорта обязательны.")
    @Column(name = "passport_number", nullable = false, unique = true, length = 128)
    private String passportNumber;

    @NotNull(message = "Адрес обязателен.")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @NotNull(message = "Дата рождения обязательна.")
    @Past(message = "Дата рождения должна быть в прошлом.")
    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender = Gender.unknown;

    @Pattern(regexp = "^(\\+7|7|8)?[0-9\\s\\-\\(\\)]{10,15}$",
            message = "Некорректный формат номера телефона")
    @Column(name = "phone_number", length = 20)
    private String phoneNumber = "N/A";
}
