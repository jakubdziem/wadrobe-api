package org.dziem.clothesarserver.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.dto.UpdatePieceOfClothingDTO;
import org.dziem.clothesarserver.service.PieceOfClothingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api")
public class PieceOfClothingController {

    private final PieceOfClothingService pieceOfClothingService;

    public PieceOfClothingController(PieceOfClothingService pieceOfClothingService) {
        this.pieceOfClothingService = pieceOfClothingService;
    }

    @PostMapping("/piece-of-clothing")
    public ResponseEntity<Void> addPieceOfClothing(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(
                            name = "Example",
                            value = """
                                    {
                                        "name": "Blue shirt",
                                        "category": "Polo",
                                        "purchaseDate": "2024-07-07",
                                        "size": "XLLL",
                                        "condition": "New condition",
                                        "note": "Comfy",
                                        "price": "199.9",
                                        "tags": ["fire", "casual"],
                                        "brand": "Nike",
                                        "arUrl": "url"
                                    }
                                    """
                    )
            }
    )
    )@RequestBody AddPieceOfClothingDTO addPieceOfClothingDTO) {
        return pieceOfClothingService.addPieceOfClothingDTO(addPieceOfClothingDTO);
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
    public ResponseEntity<Void> updatePieceOfClothing(@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(
            examples = {
                    @ExampleObject(
                            name = "Example",
                            value = """
                                    {
                                        "name": "Blue shirt",
                                        "category": "Polo",
                                        "imageUrl": "newURL",
                                        "purchaseDate": "2024-07-07",
                                        "size": "XLLL",
                                        "condition": "New condition",
                                        "note": "Comfy",
                                        "price": "199.9",
                                        "tags": ["fire", "casual"],
                                        "occasions": ["Sister's birthday"],
                                        "brand": "Nike",
                                        "arUrl": "url"
                                    }
                                    """
                    )
            }
    )
    )@RequestBody UpdatePieceOfClothingDTO updatePieceOfClothingDTO, @PathVariable Long pieceOfClothingId) {
        return pieceOfClothingService.updatePieceOfClothing(updatePieceOfClothingDTO, pieceOfClothingId);
    }

}
