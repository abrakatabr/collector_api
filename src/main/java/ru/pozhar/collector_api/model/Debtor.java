package ru.pozhar.collector_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "debtors")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
