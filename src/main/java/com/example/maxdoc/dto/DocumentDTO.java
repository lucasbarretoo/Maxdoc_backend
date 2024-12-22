package com.example.maxdoc.dto;

import java.util.List;

public record DocumentDTO(
    Integer id,
    String title,
    List<DocumentVersionDTO> versions
) {
}
