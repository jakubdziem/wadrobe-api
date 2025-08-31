package org.dziem.clothesarserver.repository;

import org.dziem.clothesarserver.dto.OccasionDTO;
import org.dziem.clothesarserver.model.Occasion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OccasionRepository extends JpaRepository<Occasion, Long> {
    @Query("SELECT o " +
            "FROM Occasion o " +
            "JOIN o.pieces p " +
            "WHERE p.id = :pieceOfClothingId")
    List<Occasion> findAllByPieceOfClothingId(Long pieceOfClothingId);

    @Query("SELECT new org.dziem.clothesarserver.dto.OccasionDTO(o.name, o.date) " +
            "FROM Occasion o " +
            "JOIN o.pieces p " +
            "WHERE p.id = :pieceOfClothingId")
    List<OccasionDTO> findAllDTOsByPieceOfClothing(Long pieceOfClothingId);
}
