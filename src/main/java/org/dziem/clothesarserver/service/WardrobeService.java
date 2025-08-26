package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.PieceOfClothingPreviewDTO;
import org.dziem.clothesarserver.model.User;
import org.dziem.clothesarserver.repository.PieceOfClothingRepository;
import org.dziem.clothesarserver.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WardrobeService {
    private final PieceOfClothingRepository pieceOfClothingRepository;
    private final UserRepository userRepository;

    public WardrobeService(PieceOfClothingRepository pieceOfClothingRepository, UserRepository userRepository) {
        this.pieceOfClothingRepository = pieceOfClothingRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<PieceOfClothingPreviewDTO>> getWardrobePreview(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(UUID.fromString(userId));
        if(optionalUser.isEmpty()) return ResponseEntity.notFound().build();
        List<PieceOfClothingPreviewDTO> pieceOfClothingPreviewDTOs =
                pieceOfClothingRepository.findPieceOfClothingPreviewListByUserId(
                        UUID.fromString(userId))
                        .stream().map(projection ->
                            new PieceOfClothingPreviewDTO(
                                    projection.getIsFavorite(),
                                    projection.getImageUrl(),
                                    projection.getWearCount()))
                        .toList();

        return ResponseEntity.ok(pieceOfClothingPreviewDTOs);
    }
}
