package org.dziem.clothesarserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class PieceOfClothingPreviewDTO {
    private long pieceOfClothingId;
    private Boolean isFavorite;
    private String imageUrl;
    private int wearCount;
    private List<String> tags;
    private List<OccasionDTO> occasions;

    public PieceOfClothingPreviewDTO(Boolean isFavorite, String imageUrl, int wearCount, long pieceOfClothingId) {
        this.isFavorite = isFavorite;
        this.imageUrl = imageUrl;
        this.wearCount = wearCount;
        this.pieceOfClothingId = pieceOfClothingId;
    }
}
