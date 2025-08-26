package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.PieceOfClothingPreviewDTO;
import org.dziem.clothesarserver.repository.PieceOfClothingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WardrobeServiceImpl implements WardrobeService {
    private final PieceOfClothingRepository pieceOfClothingRepository;
    private final UserService userService;

    public WardrobeServiceImpl(PieceOfClothingRepository pieceOfClothingRepository, UserService userService) {
        this.pieceOfClothingRepository = pieceOfClothingRepository;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<List<PieceOfClothingPreviewDTO>> getWardrobePreview(String userId) {
        UUID userUUID = UUID.fromString(userId);
        if(!userService.userExists(userUUID)) return ResponseEntity.notFound().build();
        List<PieceOfClothingPreviewDTO> pieceOfClothingPreviewDTOs =
                pieceOfClothingRepository.findPieceOfClothingPreviewListByUserId(
                                userUUID)
                        .stream().map(projection ->
                            new PieceOfClothingPreviewDTO(
                                    projection.getIsFavorite(),
                                    projection.getImageUrl(),
                                    projection.getWearCount()))
                        .toList();

        return ResponseEntity.ok(pieceOfClothingPreviewDTOs);
    }
}
