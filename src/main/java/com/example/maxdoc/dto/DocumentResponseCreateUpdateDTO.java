package com.example.maxdoc.dto;

public record DocumentResponseCreateUpdateDTO(
    String title,
    String abbrev,
    String description,
    Integer version,
    String phase
) {
}
