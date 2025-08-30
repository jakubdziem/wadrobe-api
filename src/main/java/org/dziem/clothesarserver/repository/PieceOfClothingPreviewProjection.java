package org.dziem.clothesarserver.repository;

public interface PieceOfClothingPreviewProjection {
    Boolean getIsFavorite();
    String getImageUrl();
    int getWearCount();
    long getPieceOfClothingId();
}
