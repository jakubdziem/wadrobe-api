package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.AddPieceOfClothingDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingDetailsDTO;
import org.dziem.clothesarserver.dto.UpdatePieceOfClothingDTO;
import org.dziem.clothesarserver.model.*;
import org.dziem.clothesarserver.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PieceOfClothingServiceImplTest {

    @Mock private PieceOfClothingRepository pieceOfClothingRepository;
    @Mock private UserService userService;
    @Mock private CategoryRepository categoryRepository;
    @Mock private SizeRepository sizeRepository;
    @Mock private ConditionRepository conditionRepository;
    @Mock private TagRepository tagRepository;
    @Mock private UserRepository userRepository;
    @Mock private WornDateRepository wornDateRepository;
    @Mock private OccasionRepository occasionRepository;

    private PieceOfClothingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PieceOfClothingServiceImpl(
                pieceOfClothingRepository,
                userService,
                categoryRepository,
                sizeRepository,
                conditionRepository,
                tagRepository,
                userRepository,
                wornDateRepository,
                occasionRepository
        );
    }

    @Test
    void addPieceOfClothingDTO_shouldReturnNotFound_whenUserDoesNotExist() {
        // given
        String userId = UUID.randomUUID().toString();
        AddPieceOfClothingDTO dto = new AddPieceOfClothingDTO();
        dto.setName("Shirt");
        when(userService.userExists(userId)).thenReturn(false);

        // when
        ResponseEntity<Void> response = service.addPieceOfClothingDTO(dto, userId);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        verifyNoInteractions(pieceOfClothingRepository, userRepository);
    }

    @Test
    void addPieceOfClothingDTO_shouldSavePiece_whenUserExists() {
        // given
        String userId = UUID.randomUUID().toString();
        UUID uuid = UUID.fromString(userId);

        AddPieceOfClothingDTO dto = new AddPieceOfClothingDTO();
        dto.setName("Shirt");
        dto.setCategory("Casual");
        dto.setSize("M");
        dto.setCondition("New");
        dto.setTags(List.of("Summer", "Work"));
        dto.setPrice(BigDecimal.valueOf(100));
        dto.setBrand("Zara");
        dto.setImageUrl("img.jpg");

        when(userService.userExists(userId)).thenReturn(true);
        when(userRepository.getReferenceById(uuid)).thenReturn(User.builder().userId(uuid).build());
        when(categoryRepository.findAllByName("Casual")).thenReturn(List.of());
        when(sizeRepository.findAllByName("M")).thenReturn(List.of());
        when(conditionRepository.findAllByName("New")).thenReturn(List.of());
        when(categoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sizeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(conditionRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(tagRepository.findNamesByPieceOfClothingIds(dto.getTags())).thenReturn(List.of());
        when(tagRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        // when
        ResponseEntity<Void> response = service.addPieceOfClothingDTO(dto, userId);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        ArgumentCaptor<PieceOfClothing> captor = ArgumentCaptor.forClass(PieceOfClothing.class);
        verify(pieceOfClothingRepository).save(captor.capture());

        PieceOfClothing saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Shirt");
        assertThat(saved.getCategory().getName()).isEqualTo("Casual");
        assertThat(saved.getTags()).extracting("name").containsExactlyInAnyOrder("Summer", "Work");
    }

    @Test
    void getPieceOfClothingDetailsDTO_shouldReturnNotFound_whenPieceNotExists() {
        // given
        when(pieceOfClothingRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        ResponseEntity<PieceOfClothingDetailsDTO> response = service.getPieceOfClothingDetailsDTO(1L);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void getPieceOfClothingDetailsDTO_shouldReturnDetails_whenPieceExists() {
        // given
        PieceOfClothing piece = PieceOfClothing.builder()
                .id(1L)
                .name("Jacket")
                .imageUrl("jacket.png")
                .category(Category.builder().name("Outerwear").build())
                .size(Size.builder().name("L").build())
                .condition(Condition.builder().name("Used").build())
                .tags(List.of(Tag.builder().name("Winter").build()))
                .occasions(List.of(Occasion.builder().name("Party").build()))
                .brand("Nike")
                .price(BigDecimal.valueOf(250))
                .build();
        when(pieceOfClothingRepository.findById(1L)).thenReturn(Optional.of(piece));

        // when
        ResponseEntity<PieceOfClothingDetailsDTO> response = service.getPieceOfClothingDetailsDTO(1L);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        PieceOfClothingDetailsDTO dto = response.getBody();
        assertThat(dto.getName()).isEqualTo("Jacket");
        assertThat(dto.getTags()).containsExactly("Winter");
        assertThat(dto.getOccasions()).containsExactly("Party");
    }

    @Test
    void toggleIsFavorite_shouldUpdateAndSave_whenPieceExists() {
        // given
        PieceOfClothing piece = PieceOfClothing.builder().id(1L).isFavorite(false).build();
        when(pieceOfClothingRepository.findById(1L)).thenReturn(Optional.of(piece));

        // when
        ResponseEntity<Void> response = service.toggleIsFavorite(1L, true);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(piece.getIsFavorite()).isTrue();
        verify(pieceOfClothingRepository).save(piece);
    }

    @Test
    void incrementWearCount_shouldIncreaseWearCount_whenPieceExists() {
        // given
        PieceOfClothing piece = PieceOfClothing.builder().id(1L).wearCount(0).build();
        when(pieceOfClothingRepository.findById(1L)).thenReturn(Optional.of(piece));

        // when
        ResponseEntity<Integer> response = service.incrementWearCount(1L);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(1);
        assertThat(piece.getWearCount()).isEqualTo(1);
        assertThat(piece.getLastWornDate()).isEqualTo(LocalDate.now());
        verify(wornDateRepository).save(any(WornDate.class));
        verify(pieceOfClothingRepository).save(piece);
    }

    @Test
    void updatePieceOfClothing_shouldReturnNotFound_whenPieceNotExists() {
        // given
        UpdatePieceOfClothingDTO dto = new UpdatePieceOfClothingDTO();
        when(pieceOfClothingRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        ResponseEntity<Void> response = service.updatePieceOfClothing(dto, 1L);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void updatePieceOfClothing_shouldUpdateRelationsAndOccasions_whenPieceExists() {
        // given
        Long pieceId = 1L;
        PieceOfClothing existingPiece = PieceOfClothing.builder()
                .id(pieceId)
                .name("Old Shirt")
                .category(Category.builder().name("OldCat").build())
                .size(Size.builder().name("OldSize").build())
                .condition(Condition.builder().name("OldCond").build())
                .tags(List.of(Tag.builder().name("OldTag").build()))
                .occasions(new ArrayList<>())
                .build();
        when(pieceOfClothingRepository.findById(pieceId)).thenReturn(Optional.of(existingPiece));

        UpdatePieceOfClothingDTO dto = new UpdatePieceOfClothingDTO();
        dto.setName("New Shirt");
        dto.setCategory("NewCat");
        dto.setSize("NewSize");
        dto.setCondition("NewCond");
        dto.setTags(List.of("Tag1", "Tag2"));
        dto.setOccasions(List.of("Party", "Work"));
        dto.setNote("Updated note");
        dto.setPrice(BigDecimal.valueOf(200));
        dto.setBrand("NewBrand");
        dto.setImageUrl("new.jpg");

        when(categoryRepository.findAllByName("NewCat")).thenReturn(List.of());
        when(sizeRepository.findAllByName("NewSize")).thenReturn(List.of());
        when(conditionRepository.findAllByName("NewCond")).thenReturn(List.of());
        when(categoryRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sizeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(conditionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        when(tagRepository.findNamesByPieceOfClothingIds(List.of("Tag1","Tag2"))).thenReturn(List.of());
        when(tagRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        Occasion existingOccasion = Occasion.builder().name("Wedding").build();
        when(occasionRepository.findAllByPieceOfClothingId(pieceId)).thenReturn(new ArrayList<>(List.of(existingOccasion)));
        when(occasionRepository.saveAll(any())).thenAnswer(i -> i.getArgument(0));

        // when
        ResponseEntity<Void> response = service.updatePieceOfClothing(dto, pieceId);

        // then
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(categoryRepository).save(any(Category.class));
        verify(sizeRepository).save(any(Size.class));
        verify(conditionRepository).save(any(Condition.class));
        verify(tagRepository).saveAll(any());
        verify(occasionRepository).saveAll(any());
        verify(pieceOfClothingRepository).save(existingPiece);

        assertThat(existingPiece.getName()).isEqualTo("New Shirt");
        assertThat(existingPiece.getCategory().getName()).isEqualTo("NewCat");
        assertThat(existingPiece.getSize().getName()).isEqualTo("NewSize");
        assertThat(existingPiece.getCondition().getName()).isEqualTo("NewCond");
        assertThat(existingPiece.getTags()).extracting(Tag::getName).containsExactlyInAnyOrder("Tag1","Tag2");
        assertThat(existingPiece.getOccasions()).extracting(Occasion::getName).contains("Wedding","Party","Work");
        assertThat(existingPiece.getBrand()).isEqualTo("NewBrand");
        assertThat(existingPiece.getPrice()).isEqualTo(BigDecimal.valueOf(200));
        assertThat(existingPiece.getNote()).isEqualTo("Updated note");
        assertThat(existingPiece.getImageUrl()).isEqualTo("new.jpg");
    }

}
