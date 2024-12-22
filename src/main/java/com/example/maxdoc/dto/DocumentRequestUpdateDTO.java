package com.example.maxdoc.dto;

public record DocumentRequestUpdateDTO(
    Integer id,
    String title,
    String abbrev,
    String description
) {
}
