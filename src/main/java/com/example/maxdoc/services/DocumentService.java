package com.example.maxdoc.services;

import com.example.maxdoc.entities.Document;
import com.example.maxdoc.respositories.DocumentRepository;
import com.example.maxdoc.respositories.DocumentVersionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    private final DocumentVersionRepository documentVersionRepository;

    public DocumentService(DocumentRepository documentRepository, DocumentVersionRepository documentVersionRepository) {
        this.documentRepository = documentRepository;
        this.documentVersionRepository = documentVersionRepository;
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

        boolean docExists = documentRepository.existsById(id);

        if (docExists) {

            documentRepository.findById(id)
                .map(document -> {
                    document.getVersions().stream()
                            .map(version -> {
                                documentVersionRepository.deleteById(version.getId());
                                return Void.TYPE;
                            })
                            .collect(Collectors.toList());
                    return Void.TYPE;
                });
                documentRepository.deleteById(id);

        } else {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND, "Document não encontrado" );
        }
    }
}
