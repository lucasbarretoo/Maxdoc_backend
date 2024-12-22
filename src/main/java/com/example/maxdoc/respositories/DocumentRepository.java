package com.example.maxdoc.respositories;

import com.example.maxdoc.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

}
