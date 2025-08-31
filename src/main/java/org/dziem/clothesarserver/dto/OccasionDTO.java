package org.dziem.clothesarserver.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OccasionDTO {
    private String name;
    private LocalDate date;

    public OccasionDTO(String name, LocalDate date) {
        this.name = name;
        this.date = date;
    }
}
