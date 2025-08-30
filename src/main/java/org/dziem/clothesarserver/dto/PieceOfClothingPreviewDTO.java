package org.dziem.clothesarserver.dto;

import lombok.Data;

@Data
public class PieceOfClothingPreviewDTO {
    private long pieceOfClothingId;
    private Boolean isFavorite;
    private String imageUrl;
    private int wearCount;

    public PieceOfClothingPreviewDTO(Boolean isFavorite, String imageUrl, int wearCount, long pieceOfClothingId) {
        this.isFavorite = isFavorite;
        this.imageUrl = imageUrl;
        this.wearCount = wearCount;
        this.pieceOfClothingId = pieceOfClothingId;
    }
}
