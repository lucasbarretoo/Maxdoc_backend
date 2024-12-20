package com.example.maxdoc.dto;

import com.example.maxdoc.enitites.DocumentVersion;

import java.util.List;

public record DocumentDTO(
    Integer id,
    String title,
    List<DocumentVersionDTO> versions
) {
}
