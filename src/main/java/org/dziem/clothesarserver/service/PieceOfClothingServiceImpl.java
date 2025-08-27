package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.model.Occasion;
import org.dziem.clothesarserver.model.PieceOfClothing;
import org.dziem.clothesarserver.model.Tag;
import org.dziem.clothesarserver.repository.PieceOfClothingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PieceOfClothingServiceImpl implements PieceOfClothingService {
    private final PieceOfClothingRepository pieceOfClothingRepository;

    public PieceOfClothingServiceImpl(PieceOfClothingRepository pieceOfClothingRepository) {
        this.pieceOfClothingRepository = pieceOfClothingRepository;
    }

    @Override
    public ResponseEntity<AddPieceOfClothingDTO> addPieceOfClothingDTO() {
        return null;
    }

    @Override
    public ResponseEntity<PieceOfClothingDetailsDTO> getPieceOfClothingDetailsDTO(Long pieceOfClothingId) {
        Optional<PieceOfClothing> pieceOfClothingOptional = pieceOfClothingRepository.findById(pieceOfClothingId);
        if(pieceOfClothingOptional.isEmpty()) return ResponseEntity.notFound().build();
        PieceOfClothing pieceOfClothing = pieceOfClothingOptional.get();
        return ResponseEntity.ok(PieceOfClothingDetailsDTO.builder()
                .imageUrl(pieceOfClothing.getImageUrl())
                .category(pieceOfClothing.getCategory().getName())
                .purchaseDate(pieceOfClothing.getPurchaseDate())
                .size(pieceOfClothing.getSize().getName())
                .condition(pieceOfClothing.getCondition().getName())
                .note(pieceOfClothing.getNote())
                .price(pieceOfClothing.getPrice())
                .tags(pieceOfClothing.getTags().stream().map(Tag::getName).toList())
                .brand(pieceOfClothing.getBrand())
                .occasions(pieceOfClothing.getOccasions().stream().map(Occasion::getName).toList())
                .arUrl(pieceOfClothing.getArUrl()).build());
    }
}
