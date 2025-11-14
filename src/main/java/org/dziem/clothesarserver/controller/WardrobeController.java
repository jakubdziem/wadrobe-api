package org.dziem.clothesarserver.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.dziem.clothesarserver.dto.PieceOfClothingPreviewDTO;
import org.dziem.clothesarserver.service.WardrobeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api")
public class WardrobeController {
    private final WardrobeService wardrobeService;

    public WardrobeController(WardrobeService wardrobeService) {
        this.wardrobeService = wardrobeService;
    }

    @GetMapping("/wardrobe-preview")
    public ResponseEntity<List<PieceOfClothingPreviewDTO>> getWardrobePreview() {
        return wardrobeService.getWardrobePreview();
    }
}
