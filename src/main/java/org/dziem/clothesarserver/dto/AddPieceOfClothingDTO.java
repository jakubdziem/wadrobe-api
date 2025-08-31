package org.dziem.clothesarserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class AddPieceOfClothingDTO {
    private String name;
    private String imageUrl;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    private String size;
    private String condition;
    private String note;
    private BigDecimal price;
    private List<String> tags;
    private String brand;
    private String arUrl;

    public AddPieceOfClothingDTO(UpdatePieceOfClothingDTO updatePieceOfClothingDTO) {
        this.name = updatePieceOfClothingDTO.getName();
        this.imageUrl = updatePieceOfClothingDTO.getImageUrl();
        this.category = updatePieceOfClothingDTO.getCategory();
        this.purchaseDate = updatePieceOfClothingDTO.getPurchaseDate();
        this.size = updatePieceOfClothingDTO.getSize();
        this.condition = updatePieceOfClothingDTO.getCondition();
        this.note = updatePieceOfClothingDTO.getNote();
        this.price = updatePieceOfClothingDTO.getPrice();
        this.tags = updatePieceOfClothingDTO.getTags();
        this.brand = updatePieceOfClothingDTO.getBrand();
        this.arUrl = updatePieceOfClothingDTO.getArUrl();
    }
}
