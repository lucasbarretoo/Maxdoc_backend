package com.example.maxdoc.controllers;

import com.example.maxdoc.dto.DocumentDTO;
import com.example.maxdoc.dto.DocumentRequestCreateUpdateDTO;
import com.example.maxdoc.dto.DocumentResponseCreateDto;
import com.example.maxdoc.dto.DocumentVersionDTO;
import com.example.maxdoc.enitites.Document;
import com.example.maxdoc.enitites.DocumentVersion;
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
                    document.getVersions().stream()
                        .map(version -> new DocumentVersionDTO(
                            version.getId(),
                            version.getVersion(),
                            version.getAbbrev(),
                            version.getDescription(),
                            version.getPhase()
                        ))
                        .collect(Collectors.toList())
            ))
            .collect(Collectors.toList());

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
                                version.getPhase()
                        ))
                        .collect(Collectors.toList())
        );
        return ResponseEntity.ok(documentDTO);
    }

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public ResponseEntity<DocumentResponseCreateDto> create(@RequestBody DocumentRequestCreateUpdateDTO body) {

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
            .body(new DocumentResponseCreateDto(
                doc.getTitle(),
                docVersion.getAbbrev(),
                docVersion.getDescription(),
                docVersion.getVersion(),
                docVersion.getPhase()
            ));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DocumentResponseCreateDto> update(@PathVariable Integer id, @RequestBody DocumentRequestCreateUpdateDTO body) {

        Document doc = documentService.findById(id);

        doc.setTitle(body.title());
        doc.setId(doc.getId());

        List<DocumentVersion> versions = doc.getVersions();

        DocumentVersion docVersion = new DocumentVersion();
        docVersion.setDocument(doc);
        docVersion.setVersion(versions.getLast().getVersion() + 1);
        docVersion.setPhase("Minuta");
        docVersion.setAbbrev(body.abbrev());
        docVersion.setDescription(body.description());

        this.documentVersionService.save(docVersion);

        versions.add(docVersion);
        doc.setVersions(versions);
        documentService.update(id, doc);

        return ResponseEntity.ok(new DocumentResponseCreateDto(
            doc.getTitle(),
            docVersion.getAbbrev(),
            docVersion.getDescription(),
            docVersion.getVersion(),
            docVersion.getPhase()
        ));
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        documentService.delete(id);
    }
}
