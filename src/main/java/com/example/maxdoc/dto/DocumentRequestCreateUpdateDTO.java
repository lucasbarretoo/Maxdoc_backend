package com.example.maxdoc.dto;

public record DocumentRequestCreateUpdateDTO(
    String title,
    String abbrev,
    String description
) {

}
