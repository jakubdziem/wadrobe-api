package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.springframework.http.ResponseEntity;

public interface PieceOfClothingService {

    ResponseEntity<Void> addPieceOfClothingDTO(AddPieceOfClothingDTO addPieceOfClothingDTO, String userId);

    ResponseEntity<PieceOfClothingDetailsDTO> getPieceOfClothingDetailsDTO(Long pieceOfClothingId);

    ResponseEntity<Void> toggleIsFavorite(Long pieceOfClothingId, boolean isFavorite);

    ResponseEntity<Integer> incrementWearCount(Long pieceOfClothingId);
}
