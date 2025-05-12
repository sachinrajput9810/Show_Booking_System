package com.nt.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Show {
    private String name;
    private String genre;
    private Map<String, Integer> slots = new LinkedHashMap<>();

    public Show(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }
}
