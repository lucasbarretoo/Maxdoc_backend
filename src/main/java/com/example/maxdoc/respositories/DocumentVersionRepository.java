package com.example.maxdoc.respositories;

import com.example.maxdoc.enitites.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Integer> {
}
