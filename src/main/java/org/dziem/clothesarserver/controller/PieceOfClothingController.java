package org.dziem.clothesarserver.controller;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.service.PieceOfClothingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PieceOfClothingController {

    private final PieceOfClothingService pieceOfClothingService;

    public PieceOfClothingController(PieceOfClothingService pieceOfClothingService) {
        this.pieceOfClothingService = pieceOfClothingService;
    }

    @PostMapping("/piece-of-clothing/{userId}")
    public ResponseEntity<Void> addPieceOfClothing(@RequestBody AddPieceOfClothingDTO addPieceOfClothingDTO, @PathVariable String userId) {
        return pieceOfClothingService.addPieceOfClothingDTO(addPieceOfClothingDTO, userId);
    }

    @GetMapping("/piece-of-clothing/{pieceOfClothingId}")
    public ResponseEntity<PieceOfClothingDetailsDTO> getPieceOfClothing(@PathVariable Long pieceOfClothingId) {
        return pieceOfClothingService.getPieceOfClothingDetailsDTO(pieceOfClothingId);
    }

}
