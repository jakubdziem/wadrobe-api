package org.dziem.clothesarserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class UpdatePieceOfClothingDTO {
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
    private List<String> occasions;
    private String brand;
    private String arUrl;
}
