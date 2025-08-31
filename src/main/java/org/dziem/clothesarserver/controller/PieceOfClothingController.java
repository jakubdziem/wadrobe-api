package org.dziem.clothesarserver.controller;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.dto.UpdatePieceOfClothingDTO;
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

    @PostMapping("/piece-of-clothing/toggle-favorite/{pieceOfClothingId}")
    public ResponseEntity<Void> toggleIsFavorite(@PathVariable Long pieceOfClothingId, @RequestParam boolean isFavorite) {
        return pieceOfClothingService.toggleIsFavorite(pieceOfClothingId, isFavorite);
    }

    @PostMapping("/piece-of-clothing/increment-wear-count/{pieceOfClothingId}")
    public ResponseEntity<Integer> incrementWearCount(@PathVariable Long pieceOfClothingId) {
        return pieceOfClothingService.incrementWearCount(pieceOfClothingId);
    }

    @PutMapping("/piece-of-clothing/{pieceOfClothingId}")
    public ResponseEntity<Void> updatePieceOfClothing(@RequestBody UpdatePieceOfClothingDTO updatePieceOfClothingDTO, @PathVariable Long pieceOfClothingId) {
        return pieceOfClothingService.updatePieceOfClothing(updatePieceOfClothingDTO, pieceOfClothingId);
    }

}
