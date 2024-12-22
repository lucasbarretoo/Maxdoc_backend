package com.example.maxdoc.dto;

import java.time.LocalDateTime;

public record DocumentVersionDTO(
    Integer id,
    Integer version,
    String abbrev,
    String description,
    String phase,
    LocalDateTime createdAt
) {
}
