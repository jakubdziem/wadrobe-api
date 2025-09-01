package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.PieceOfClothingPreviewDTO;
import org.dziem.clothesarserver.model.User;
import org.dziem.clothesarserver.repository.OccasionRepository;
import org.dziem.clothesarserver.repository.PieceOfClothingRepository;
import org.dziem.clothesarserver.repository.TagRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class WardrobeServiceImpl implements WardrobeService {
    private final PieceOfClothingRepository pieceOfClothingRepository;
    private final OccasionRepository occasionRepository;
    private final UserService userService;
    private final TagRepository tagRepository;

    public WardrobeServiceImpl(PieceOfClothingRepository pieceOfClothingRepository, OccasionRepository occasionRepository, UserService userService, TagRepository tagRepository) {
        this.pieceOfClothingRepository = pieceOfClothingRepository;
        this.occasionRepository = occasionRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
    }

    @Override
    public ResponseEntity<List<PieceOfClothingPreviewDTO>> getWardrobePreview() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userUUID = currentUser.getUserId();

        if(!userService.userExists(userUUID)) return ResponseEntity.notFound().build();
        List<PieceOfClothingPreviewDTO> pieceOfClothingPreviewDTOs =
                pieceOfClothingRepository.findPieceOfClothingPreviewListByUserId(
                                userUUID)
                        .stream().map(projection -> {
                            PieceOfClothingPreviewDTO pieceOfClothingPreviewDTO = new PieceOfClothingPreviewDTO(
                                    projection.getIsFavorite(),
                                    projection.getImageUrl(),
                                    projection.getWearCount(),
                                    projection.getPieceOfClothingId());
                            pieceOfClothingPreviewDTO.setOccasions(occasionRepository.findAllDTOsByPieceOfClothing(projection.getPieceOfClothingId()));
                            pieceOfClothingPreviewDTO.setTags(tagRepository.findNamesByPieceOfClothingIds(projection.getPieceOfClothingId()));
                            return pieceOfClothingPreviewDTO;
                        }).sorted(Comparator.comparingLong(PieceOfClothingPreviewDTO::getPieceOfClothingId))
                        .toList();

        return ResponseEntity.ok(pieceOfClothingPreviewDTOs);
    }
}
