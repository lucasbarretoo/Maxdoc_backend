package com.example.maxdoc.respositories;

import com.example.maxdoc.entities.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Integer> {

    @Query("SELECT dv FROM DocumentVersion dv WHERE dv.document.id = :documentId ORDER BY dv.version ASC")
    List<DocumentVersion> findVersionsByDocumentIdOrderByVersion(@Param("documentId") Integer documentId);

}
