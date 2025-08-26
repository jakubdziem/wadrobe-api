package org.dziem.clothesarserver.repository;

import org.dziem.clothesarserver.model.PieceOfClothing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PieceOfClothingRepository extends JpaRepository<PieceOfClothing, Long> {

    @Query("SELECT p.isFavorite AS isFavorite, " +
            "       p.imageUrl AS imageUrl, " +
            "       p.wearCount AS wearCount " +
            "FROM PieceOfClothing p " +
            "WHERE p.user.userId = :userId")
    List<PieceOfClothingPreviewProjection> findPieceOfClothingPreviewListByUserId(UUID userId);
}
