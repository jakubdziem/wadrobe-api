package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.PieceOfClothingPreviewDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WardrobeService {

    ResponseEntity<List<PieceOfClothingPreviewDTO>> getWardrobePreview();

}
