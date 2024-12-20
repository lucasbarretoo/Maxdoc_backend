package com.example.maxdoc.enitites;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(
    name = "documents_versions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"version", "abbrev", "document_id"})
)
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private String abbrev;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "VARCHAR(20) CHECK (phase IN ('Minuta', 'Vigente', 'Obsoleto'))")
    private String phase;

    @Column(nullable = false)
    private final Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    public DocumentVersion() {}

    public DocumentVersion(
        Integer version,
        String abbrev,
        String description,
        String phase
    ) {
        this.version = version;
        this.abbrev = abbrev;
        this.description = description;
        this.phase = phase;
    }


    public Integer getId() {
        return id;
    }

    public Integer getVersion() {
        return version;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public String getDescription() {
        return description;
    }

    public String getPhase() {
        return phase;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }
}
