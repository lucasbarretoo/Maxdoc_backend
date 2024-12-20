package com.example.maxdoc.dto;

import java.sql.Timestamp;

public record DocumentVersionDTO(
    Integer id,
    Integer version,
    String abbrev,
    String description,
    String phase
) {
}
