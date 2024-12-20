package com.example.maxdoc.dto;

public record DocumentResponseCreateDto(
    String title,
    String abbrev,
    String description,
    Integer version,
    String phase
) {
}
