package org.dziem.clothesarserver.service;

import org.dziem.clothesarserver.dto.OccasionDTO;
import org.dziem.clothesarserver.dto.PieceOfClothingPreviewDTO;
import org.dziem.clothesarserver.model.User;
import org.dziem.clothesarserver.repository.OccasionRepository;
import org.dziem.clothesarserver.repository.PieceOfClothingPreviewProjection;
import org.dziem.clothesarserver.repository.PieceOfClothingRepository;
import org.dziem.clothesarserver.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WardrobeServiceImplTest {

    @Mock private PieceOfClothingRepository pieceOfClothingRepository;
    @Mock private OccasionRepository occasionRepository;
    @Mock private UserService userService;
    @Mock private TagRepository tagRepository;

    private WardrobeServiceImpl wardrobeService;

    @BeforeEach
    void setUp() {
        wardrobeService = new WardrobeServiceImpl(
                pieceOfClothingRepository,
                occasionRepository,
                userService,
                tagRepository
        );
        SecurityContextHolder.clearContext();
    }

    private void setAuthenticatedUser(UUID userId) {
        User user = new User();
        user.setUserId(userId);
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getWardrobePreview_shouldReturnNotFound_whenUserDoesNotExist() {
        // Given
        UUID userId = UUID.randomUUID();
        setAuthenticatedUser(userId);
        when(userService.userExists(userId)).thenReturn(false);

        // When
        ResponseEntity<List<PieceOfClothingPreviewDTO>> response = wardrobeService.getWardrobePreview();

        // Then
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
        verifyNoInteractions(pieceOfClothingRepository, occasionRepository, tagRepository);
    }

    @Test
    void getWardrobePreview_shouldReturnEmptyList_whenUserExistsButNoClothes() {
        // Given
        UUID userId = UUID.randomUUID();
        setAuthenticatedUser(userId);
        when(userService.userExists(userId)).thenReturn(true);
        when(pieceOfClothingRepository.findPieceOfClothingPreviewListByUserId(userId))
                .thenReturn(List.of());

        // When
        ResponseEntity<List<PieceOfClothingPreviewDTO>> response = wardrobeService.getWardrobePreview();

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEmpty();
        verify(pieceOfClothingRepository).findPieceOfClothingPreviewListByUserId(userId);
    }

    @Test
    void getWardrobePreview_shouldReturnSortedPreviewList_whenUserExistsAndHasClothes() {
        // Given
        UUID userId = UUID.randomUUID();
        setAuthenticatedUser(userId);
        when(userService.userExists(userId)).thenReturn(true);

        PieceOfClothingPreviewProjection projection1 = mock(PieceOfClothingPreviewProjection.class);
        PieceOfClothingPreviewProjection projection2 = mock(PieceOfClothingPreviewProjection.class);

        OccasionDTO occasion1 = new OccasionDTO("Occasion1", LocalDate.now());
        OccasionDTO occasion2 = new OccasionDTO("Occasion2", LocalDate.now());

        when(projection1.getPieceOfClothingId()).thenReturn(2L);
        when(projection1.getImageUrl()).thenReturn("img2");
        when(projection1.getIsFavorite()).thenReturn(true);
        when(projection1.getWearCount()).thenReturn(5);

        when(projection2.getPieceOfClothingId()).thenReturn(1L);
        when(projection2.getImageUrl()).thenReturn("img1");
        when(projection2.getIsFavorite()).thenReturn(false);
        when(projection2.getWearCount()).thenReturn(2);

        when(pieceOfClothingRepository.findPieceOfClothingPreviewListByUserId(userId))
                .thenReturn(List.of(projection1, projection2));

        when(occasionRepository.findAllDTOsByPieceOfClothing(2L)).thenReturn(List.of(occasion2));
        when(tagRepository.findNamesByPieceOfClothingIds(2L)).thenReturn(List.of("Tag2"));

        when(occasionRepository.findAllDTOsByPieceOfClothing(1L)).thenReturn(List.of(occasion1));
        when(tagRepository.findNamesByPieceOfClothingIds(1L)).thenReturn(List.of("Tag1"));

        // When
        ResponseEntity<List<PieceOfClothingPreviewDTO>> response = wardrobeService.getWardrobePreview();

        // Then
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        List<PieceOfClothingPreviewDTO> result = response.getBody();
        assertThat(result).hasSize(2);

        assertThat(result.get(0).getPieceOfClothingId()).isEqualTo(1L);
        assertThat(result.get(1).getPieceOfClothingId()).isEqualTo(2L);

        assertThat(result.get(0).getImageUrl()).isEqualTo("img1");
        assertThat(result.get(0).getOccasions()).containsExactly(occasion1);
        assertThat(result.get(0).getTags()).containsExactly("Tag1");

        assertThat(result.get(1).getImageUrl()).isEqualTo("img2");
        assertThat(result.get(1).getOccasions()).containsExactly(occasion2);
        assertThat(result.get(1).getTags()).containsExactly("Tag2");

        verify(pieceOfClothingRepository).findPieceOfClothingPreviewListByUserId(userId);
        verify(occasionRepository).findAllDTOsByPieceOfClothing(1L);
        verify(occasionRepository).findAllDTOsByPieceOfClothing(2L);
        verify(tagRepository).findNamesByPieceOfClothingIds(1L);
        verify(tagRepository).findNamesByPieceOfClothingIds(2L);
    }

    @Test
    void getWardrobePreview_shouldReturnEmptyOccasions_whenNoneFound() {
        // Given
        UUID userId = UUID.randomUUID();
        setAuthenticatedUser(userId);
        when(userService.userExists(userId)).thenReturn(true);

        PieceOfClothingPreviewProjection projection = mock(PieceOfClothingPreviewProjection.class);
        when(projection.getPieceOfClothingId()).thenReturn(1L);
        when(projection.getImageUrl()).thenReturn("img");
        when(projection.getIsFavorite()).thenReturn(false);
        when(projection.getWearCount()).thenReturn(0);

        when(pieceOfClothingRepository.findPieceOfClothingPreviewListByUserId(userId))
                .thenReturn(List.of(projection));
        when(occasionRepository.findAllDTOsByPieceOfClothing(1L)).thenReturn(List.of());
        when(tagRepository.findNamesByPieceOfClothingIds(1L)).thenReturn(List.of("Tag1"));

        // When
        ResponseEntity<List<PieceOfClothingPreviewDTO>> response = wardrobeService.getWardrobePreview();

        // Then
        assertThat(response.getBody()).singleElement().satisfies(dto -> {
            assertThat(dto.getOccasions()).isEmpty();
            assertThat(dto.getTags()).containsExactly("Tag1");
        });
    }
}
