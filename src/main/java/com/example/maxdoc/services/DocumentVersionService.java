package com.example.maxdoc.services;

import com.example.maxdoc.enitites.DocumentVersion;
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

    public List<DocumentVersion> findAll() {
        return documentVersionRepository.findAll();
    }

    public DocumentVersion save(DocumentVersion documentVersion) {
        return documentVersionRepository.save(documentVersion);
    }

    public void delete(Integer id) {
        documentVersionRepository.findById(id)
            .map(document -> {
                documentVersionRepository.delete(document);
                return Void.TYPE;
            }).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND)  );
    }

}
