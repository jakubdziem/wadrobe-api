package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.dto.UpdatePieceOfClothingDTO;
import org.springframework.http.ResponseEntity;

public interface PieceOfClothingService {

    ResponseEntity<Void> addPieceOfClothingDTO(AddPieceOfClothingDTO addPieceOfClothingDTO);

    ResponseEntity<PieceOfClothingDetailsDTO> getPieceOfClothingDetailsDTO(Long pieceOfClothingId);

    ResponseEntity<Void> toggleIsFavorite(Long pieceOfClothingId, boolean isFavorite);

    ResponseEntity<Integer> incrementWearCount(Long pieceOfClothingId);

    ResponseEntity<Void> updatePieceOfClothing(UpdatePieceOfClothingDTO updatePieceOfClothingDTO, Long pieceOfClothingId);
}
