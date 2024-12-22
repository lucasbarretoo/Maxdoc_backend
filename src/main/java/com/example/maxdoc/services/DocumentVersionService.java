package com.example.maxdoc.services;

import com.example.maxdoc.entities.Document;
import com.example.maxdoc.entities.DocumentVersion;
import com.example.maxdoc.respositories.DocumentVersionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DocumentVersionService {

    private final DocumentVersionRepository documentVersionRepository;

    public DocumentVersionService(DocumentVersionRepository documentVersionRepository) {
        this.documentVersionRepository = documentVersionRepository;
    }

    public DocumentVersion save(DocumentVersion documentVersion) {
        return documentVersionRepository.save(documentVersion);
    }

    public DocumentVersion update(Integer id, DocumentVersion documentVersion) {
        return documentVersionRepository.findById(id)
            .map(version -> {
                version.setPhase(documentVersion.getPhase());
                return documentVersionRepository.save(version);
            }).orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND)  );
    }

    public List<DocumentVersion> getOrderedVersionsByDocumentId(Integer documentId) {
        return documentVersionRepository.findVersionsByDocumentIdOrderByVersion(documentId);
    }
}
