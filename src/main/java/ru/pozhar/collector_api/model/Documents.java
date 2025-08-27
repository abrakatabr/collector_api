package ru.pozhar.collector_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "documents")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Documents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Серия и номер паспорта обязательны")
    @Pattern(regexp = "^\\d{10}$", message = "Неверный формат серии и номера паспорта")
    @Column(name = "passport_number", nullable = false, unique = true, length = 20)
    private String passportNumber;

    @Pattern(regexp = "^\\d{12}$", message = "Неверный формат номера ИНН")
    @Column(name = "inn", nullable = true, unique = true, length = 20)
    private String inn;

    @Pattern(regexp = "^\\d{11}$", message = "Неверный формат номера СНИЛС")
    @Column(name = "snils", nullable = true, unique = true, length = 20)
    private String snils;
}
