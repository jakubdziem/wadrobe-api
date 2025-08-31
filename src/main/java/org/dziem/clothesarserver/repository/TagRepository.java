package org.dziem.clothesarserver.repository;

import org.dziem.clothesarserver.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository  extends JpaRepository<Tag, Long> {
    @Query("SELECT t " +
            "FROM Tag t " +
            "WHERE t.name in (:names)")
    List<Tag> findNamesByPieceOfClothingIds(List<String> names);

    @Query("SELECT t.name " +
            "FROM Tag t " +
            "JOIN t.pieces p " +
            "WHERE p.id in (:pieceOfClothingId)")
    List<String> findNamesByPieceOfClothingIds(Long pieceOfClothingId);
}
