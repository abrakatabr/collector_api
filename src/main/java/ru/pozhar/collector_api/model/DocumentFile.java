package ru.pozhar.collector_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "documents_files")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false, unique = true)
    private Document document;

    @NotNull
    @Column(name = "path", nullable = false, length = 128)
    private String path;

    @NotNull
    @Column(name = "filename", nullable = false, length = 128)
    private String fileName;

    @Column(name = "filesize")
    private Long fileSize;

    @NotNull
    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;
}
