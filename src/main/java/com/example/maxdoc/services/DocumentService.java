package com.example.maxdoc.services;

import com.example.maxdoc.dto.DocumentVersionDTO;
import com.example.maxdoc.enitites.Document;
import com.example.maxdoc.respositories.DocumentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentVersionService documentVersionService;


    public DocumentService(DocumentRepository documentRepository, DocumentVersionService documentVersionService) {
        this.documentRepository = documentRepository;
        this.documentVersionService = documentVersionService;
    }

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document findById(Integer id) {
        return documentRepository.findById(id).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND)  );
    }

    public Document save(Document document) {
        return documentRepository.save(document);
    }

    public Document update(Integer id, Document doc) {
        return documentRepository.findById(id)
            .map(document -> {
                document.setTitle(doc.getTitle());
                return documentRepository.save(document);
            }).orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND)  );
    }

    public void delete(Integer id) {
        documentRepository.findById(id)
            .map(document -> {
                document.getVersions().stream()
                    .map(version -> {

                        documentVersionService.delete(version.getId());
                        return Void.TYPE;
                    })
                    .collect(Collectors.toList());
                documentRepository.delete(document);
                return Void.TYPE;
            }).orElseThrow( () -> new ResponseStatusException( HttpStatus.NOT_FOUND)  );
    }
}
