package org.dziem.clothesarserver.dto;

import lombok.Data;

@Data
public class PieceOfClothingPreviewDTO {
    private Boolean isFavorite;
    private String imageUrl;
    private int wearCount;

    public PieceOfClothingPreviewDTO(Boolean isFavorite, String imageUrl, int wearCount) {
        this.isFavorite = isFavorite;
        this.imageUrl = imageUrl;
        this.wearCount = wearCount;
    }
}
