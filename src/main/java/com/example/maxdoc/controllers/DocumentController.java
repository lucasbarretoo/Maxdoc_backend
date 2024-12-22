package com.example.maxdoc.controllers;

import com.example.maxdoc.dto.DocumentDTO;
import com.example.maxdoc.dto.DocumentRequestCreateDTO;
import com.example.maxdoc.dto.DocumentResponseCreateUpdateDTO;
import com.example.maxdoc.dto.DocumentVersionDTO;
import com.example.maxdoc.entities.Document;
import com.example.maxdoc.entities.DocumentVersion;
import com.example.maxdoc.services.DocumentService;
import com.example.maxdoc.services.DocumentVersionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentVersionService documentVersionService;

    public DocumentController(
        DocumentService documentService,
        DocumentVersionService documentVersionService
    ) {
        this.documentService = documentService;
        this.documentVersionService = documentVersionService;
    }

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAll() {
        List<Document> documents = documentService.findAll();

        List<DocumentDTO> documentDTOList = documents.stream()
            .map(document -> new DocumentDTO(
                document.getId(),
                document.getTitle(),
                this.documentVersionService.getOrderedVersionsByDocumentId(document.getId())
                    .stream()
                    .map(version -> new DocumentVersionDTO(
                        version.getId(),
                        version.getVersion(),
                        version.getAbbrev(),
                        version.getDescription(),
                        version.getPhase(),
                        version.getCreatedAt()
                    )).collect(Collectors.toList())
            )).collect(Collectors.toList());

        return ResponseEntity.ok(documentDTOList);
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<DocumentDTO> getById(@PathVariable Integer id) {
        Document doc = documentService.findById(id);

        DocumentDTO documentDTO = new DocumentDTO(
            doc.getId(),
            doc.getTitle(),
            doc.getVersions().stream()
                .map(version -> new DocumentVersionDTO(
                    version.getId(),
                    version.getVersion(),
                    version.getAbbrev(),
                    version.getDescription(),
                    version.getPhase(),
                    version.getCreatedAt()
                )).collect(Collectors.toList())
        );
        return ResponseEntity.ok(documentDTO);
    }

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public ResponseEntity<DocumentResponseCreateUpdateDTO> create(@RequestBody DocumentRequestCreateDTO body) {

        Document doc = new Document();
        doc.setTitle(body.title());
        doc = documentService.save(doc);

        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setDocument(doc);
        docVersion.setVersion(1);
        docVersion.setPhase("Minuta");
        docVersion.setAbbrev(body.abbrev());
        docVersion.setDescription(body.description());

        this.documentVersionService.save(docVersion);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new DocumentResponseCreateUpdateDTO(
                doc.getTitle(),
                docVersion.getAbbrev(),
                docVersion.getDescription(),
                docVersion.getVersion(),
                docVersion.getPhase()
            ));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DocumentResponseCreateUpdateDTO> update(@PathVariable Integer id, @RequestBody DocumentRequestCreateDTO body) {

        Document doc = documentService.findById(id);
        doc.setTitle(body.title());

        List<DocumentVersion> versions = this.documentVersionService.getOrderedVersionsByDocumentId(doc.getId());
        DocumentVersion lastVersion = versions.getLast();

        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setDocument(doc);
        docVersion.setVersion(lastVersion.getVersion() + 1);
        docVersion.setPhase(lastVersion.getPhase());
        docVersion.setAbbrev(body.abbrev());
        docVersion.setDescription(body.description());

        this.documentVersionService.save(docVersion);

        versions.add(docVersion);
        doc.setVersions(versions);
        documentService.update(id, doc);

        return ResponseEntity.ok(new DocumentResponseCreateUpdateDTO(
                doc.getTitle(),
                docVersion.getAbbrev(),
                docVersion.getDescription(),
                docVersion.getVersion(),
                docVersion.getPhase()
        ));
    }

    @PutMapping(value = "/{id}/create-from-current-version")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DocumentResponseCreateUpdateDTO> createFromCurrentVersion(@PathVariable Integer id) {

        Document doc = documentService.findById(id);
        Document newDoc = new Document();
        newDoc.setTitle(doc.getTitle());
        documentService.save(newDoc);

        List<DocumentVersion> versions = this.documentVersionService.getOrderedVersionsByDocumentId(doc.getId());
        DocumentVersion lastVersion = versions.getLast();

        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setDocument(newDoc);
        docVersion.setVersion(lastVersion.getVersion() + 1);
        docVersion.setPhase("Minuta");
        docVersion.setAbbrev(lastVersion.getAbbrev());
        docVersion.setDescription(lastVersion.getDescription());

        this.documentVersionService.save(docVersion);

        return ResponseEntity.ok(new DocumentResponseCreateUpdateDTO(
                newDoc.getTitle(),
                docVersion.getAbbrev(),
                docVersion.getDescription(),
                docVersion.getVersion(),
                docVersion.getPhase()
        ));
    }

    @PutMapping(value = "/{id}/submit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DocumentDTO> submit(@PathVariable Integer id) {
        return updateDocumentPhase(id, "Vigente");
    }

    @PutMapping(value = "/{id}/obsolete")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DocumentDTO> obsolete(@PathVariable Integer id) {
        return updateDocumentPhase(id, "Obsoleto");
    }

    private ResponseEntity<DocumentDTO> updateDocumentPhase(Integer id, String phase) {
        Document doc = documentService.findById(id);
        List<DocumentVersion> versions = this.documentVersionService.getOrderedVersionsByDocumentId(doc.getId());

        DocumentVersion lastVersion = versions.getLast();

        lastVersion.setPhase("Obsoleto");
        this.documentVersionService.update(lastVersion.getId(), lastVersion);

        if (phase.equals("Vigente")) {

            DocumentVersion newVersion = new DocumentVersion();
            newVersion.setDocument(doc);
            newVersion.setVersion(lastVersion.getVersion() + 1);
            newVersion.setAbbrev(lastVersion.getAbbrev());
            newVersion.setDescription(lastVersion.getDescription());
            newVersion.setPhase(phase);

            this.documentVersionService.save(newVersion);

            versions.add(newVersion);
            doc.setVersions(versions);
            documentService.update(id, doc);
        }


        List<DocumentVersionDTO> documentVersionsDTO = convertDocumentVersionsToDTO(doc.getVersions());

        return ResponseEntity.ok(new DocumentDTO(
                doc.getId(),
                doc.getTitle(),
                documentVersionsDTO
        ));
    }

    private List<DocumentVersionDTO> convertDocumentVersionsToDTO(List<DocumentVersion> versions) {
        return versions.stream()
            .map(version -> new DocumentVersionDTO(
                version.getId(),
                version.getVersion(),
                version.getAbbrev(),
                version.getDescription(),
                version.getPhase(),
                version.getCreatedAt()
            )).collect(Collectors.toList());
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        documentService.delete(id);
    }

}
