package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.springframework.http.ResponseEntity;

public interface PieceOfClothingService {

    ResponseEntity<AddPieceOfClothingDTO> addPieceOfClothingDTO();

    ResponseEntity<PieceOfClothingDetailsDTO> getPieceOfClothingDetailsDTO(Long pieceOfClothingId);
}
