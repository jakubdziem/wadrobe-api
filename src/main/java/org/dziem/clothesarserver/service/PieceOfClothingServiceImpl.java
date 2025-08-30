package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.model.*;
import org.dziem.clothesarserver.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PieceOfClothingServiceImpl implements PieceOfClothingService {
    private final PieceOfClothingRepository pieceOfClothingRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final SizeRepository sizeRepository;
    private final ConditionRepository conditionRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public PieceOfClothingServiceImpl(PieceOfClothingRepository pieceOfClothingRepository, UserService userService, CategoryRepository categoryRepository, SizeRepository sizeRepository, ConditionRepository conditionRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.pieceOfClothingRepository = pieceOfClothingRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.sizeRepository = sizeRepository;
        this.conditionRepository = conditionRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<Void> addPieceOfClothingDTO(AddPieceOfClothingDTO addPieceOfClothingDTO, String userId) {
        if(!userService.userExists(userId)) return ResponseEntity.notFound().build();

        List<Category> categories = categoryRepository.findAllByName(addPieceOfClothingDTO.getCategory());
        Category category;
        category = categories.isEmpty() ?
                Category.builder().name(addPieceOfClothingDTO.getCategory()).build() : categories.getFirst();
        category = categoryRepository.save(category);

        List<Size> sizes = sizeRepository.findAllByName(addPieceOfClothingDTO.getSize());
        Size size;
        size = sizes.isEmpty() ?
                Size.builder().name(addPieceOfClothingDTO.getSize()).build() : sizes.getFirst();
        size = sizeRepository.save(size);


        List<Condition> conditions = conditionRepository.findAllByName(addPieceOfClothingDTO.getCondition());
        Condition condition;
        condition = conditions.isEmpty() ?
                Condition.builder().name(addPieceOfClothingDTO.getCondition()).build() : conditions.getFirst();
        condition = conditionRepository.save(condition);

        List<Tag> tags = tagRepository.findAllByNames(addPieceOfClothingDTO.getTags());
        if(tags.isEmpty()) {
            List<Tag> finalTags = tags;
            addPieceOfClothingDTO.getTags().forEach(t -> finalTags.add(Tag.builder().name(t).build()));
            tags.addAll(finalTags);
        }
        tags = tagRepository.saveAll(tags);

        pieceOfClothingRepository.save(PieceOfClothing.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userId)))
                .wearCount(0)
                .isFavorite(false)
                .lastWornDate(null)
                .name(addPieceOfClothingDTO.getName())
                .imageUrl(addPieceOfClothingDTO.getImageUrl())
                .category(category)
                .purchaseDate(addPieceOfClothingDTO.getPurchaseDate())
                .size(size)
                .condition(condition)
                .note(addPieceOfClothingDTO.getNote())
                .price(addPieceOfClothingDTO.getPrice())
                .tags(tags)
                .brand(addPieceOfClothingDTO.getBrand())
                .arUrl(addPieceOfClothingDTO.getArUrl())
                .build());

        return ResponseEntity.ok().build();
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
