package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.dto.UpdatePieceOfClothingDTO;
import org.dziem.clothesarserver.model.*;
import org.dziem.clothesarserver.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PieceOfClothingServiceImpl implements PieceOfClothingService {
    private final PieceOfClothingRepository pieceOfClothingRepository;
    private final UserService userService;
    private final CategoryRepository categoryRepository;
    private final SizeRepository sizeRepository;
    private final ConditionRepository conditionRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final WornDateRepository wornDateRepository;
    private final OccasionRepository occasionRepository;

    public PieceOfClothingServiceImpl(PieceOfClothingRepository pieceOfClothingRepository, UserService userService, CategoryRepository categoryRepository, SizeRepository sizeRepository, ConditionRepository conditionRepository, TagRepository tagRepository, UserRepository userRepository, WornDateRepository wornDateRepository, OccasionRepository occasionRepository) {
        this.pieceOfClothingRepository = pieceOfClothingRepository;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.sizeRepository = sizeRepository;
        this.conditionRepository = conditionRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.wornDateRepository = wornDateRepository;
        this.occasionRepository = occasionRepository;
    }

    @Override
    public ResponseEntity<Void> addPieceOfClothingDTO(AddPieceOfClothingDTO addPieceOfClothingDTO, String userId) {
        if(!userService.userExists(userId)) return ResponseEntity.notFound().build();

        PieceOfClothingRelations pieceOfClothingRelations = updatePieceOfClothingRelations(addPieceOfClothingDTO);

        pieceOfClothingRepository.save(PieceOfClothing.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userId)))
                .wearCount(0)
                .isFavorite(false)
                .lastWornDate(null)
                .name(addPieceOfClothingDTO.getName())
                .imageUrl(addPieceOfClothingDTO.getImageUrl())
                .category(pieceOfClothingRelations.category())
                .purchaseDate(addPieceOfClothingDTO.getPurchaseDate())
                .size(pieceOfClothingRelations.size())
                .condition(pieceOfClothingRelations.condition())
                .note(addPieceOfClothingDTO.getNote())
                .price(addPieceOfClothingDTO.getPrice())
                .tags(pieceOfClothingRelations.allTags())
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
                .name(pieceOfClothing.getName())
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

    @Override
    public ResponseEntity<Void> toggleIsFavorite(Long pieceOfClothingId, boolean isFavorite) {
        Optional<PieceOfClothing> pieceOfClothingOptional = pieceOfClothingRepository.findById(pieceOfClothingId);
        if(pieceOfClothingOptional.isEmpty()) return ResponseEntity.notFound().build();
        PieceOfClothing pieceOfClothing = pieceOfClothingOptional.get();
        pieceOfClothing.setIsFavorite(isFavorite);
        pieceOfClothingRepository.save(pieceOfClothing);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Integer> incrementWearCount(Long pieceOfClothingId) {
        Optional<PieceOfClothing> pieceOfClothingOptional = pieceOfClothingRepository.findById(pieceOfClothingId);
        if(pieceOfClothingOptional.isEmpty()) return ResponseEntity.notFound().build();

        PieceOfClothing pieceOfClothing = pieceOfClothingOptional.get();
        WornDate wornDate = WornDate.builder().date(LocalDate.now()).pieceOfClothing(pieceOfClothing).build();
        wornDateRepository.save(wornDate);
        int incrementedWearCount = pieceOfClothing.incrementWearCount();
        pieceOfClothing.setLastWornDate(LocalDate.now());
        pieceOfClothingRepository.save(pieceOfClothing);
        return ResponseEntity.ok(incrementedWearCount);
    }

    @Override
    public ResponseEntity<Void> updatePieceOfClothing(UpdatePieceOfClothingDTO updatePieceOfClothingDTO, Long pieceOfClothingId) {
        Optional<PieceOfClothing> pieceOfClothingOptional = pieceOfClothingRepository.findById(pieceOfClothingId);
        if(pieceOfClothingOptional.isEmpty()) return ResponseEntity.notFound().build();

        PieceOfClothing pieceOfClothing = pieceOfClothingOptional.get();

        PieceOfClothingRelations pieceOfClothingRelations = updatePieceOfClothingRelations(new AddPieceOfClothingDTO(updatePieceOfClothingDTO));


        List<Occasion> occasions = occasionRepository.findAllByPieceOfClothingId(pieceOfClothing.getId());

        List<String> occasionNames = updatePieceOfClothingDTO.getOccasions();
        if(!occasionNames.isEmpty()) {
            for (String name : occasionNames) {
                occasions.add(Occasion.builder().name(name).date(LocalDate.now()).build());
            }
        }

        //ocassions are not saving properly :(

        List<Occasion> savedNewOccasions = occasionRepository.saveAll(occasions);

        pieceOfClothing.setWearCount(0);
        pieceOfClothing.setOccasions(savedNewOccasions);
        pieceOfClothing.setIsFavorite(false);
        pieceOfClothing.setLastWornDate(null);
        pieceOfClothing.setName(updatePieceOfClothingDTO.getName());
        pieceOfClothing.setImageUrl(updatePieceOfClothingDTO.getImageUrl());
        pieceOfClothing.setCategory(pieceOfClothingRelations.category);
        pieceOfClothing.setPurchaseDate(updatePieceOfClothingDTO.getPurchaseDate());
        pieceOfClothing.setSize(pieceOfClothingRelations.size);
        pieceOfClothing.setCondition(pieceOfClothingRelations.condition);
        pieceOfClothing.setNote(updatePieceOfClothingDTO.getNote());
        pieceOfClothing.setPrice(updatePieceOfClothingDTO.getPrice());
        pieceOfClothing.setTags(pieceOfClothingRelations.allTags);
        pieceOfClothing.setBrand(updatePieceOfClothingDTO.getBrand());
        pieceOfClothing.setArUrl(updatePieceOfClothingDTO.getArUrl());
        pieceOfClothingRepository.save(pieceOfClothing);

        return ResponseEntity.ok().build();
    }

    private PieceOfClothingRelations updatePieceOfClothingRelations(AddPieceOfClothingDTO addPieceOfClothingDTO) {
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

        List<String> tagNames = addPieceOfClothingDTO.getTags();
        List<Tag> existingTags = tagRepository.findNamesByPieceOfClothingIds(tagNames);

        Set<String> existingNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingNames.contains(name))
                .map(name -> Tag.builder().name(name).build())
                .toList();

        List<Tag> savedNewTags = tagRepository.saveAll(newTags);

        List<Tag> allTags = new ArrayList<>(existingTags);
        allTags.addAll(savedNewTags);
        return new PieceOfClothingRelations(category, size, condition, allTags);
    }


    private record PieceOfClothingRelations(Category category, Size size, Condition condition, List<Tag> allTags) {
    }
}
