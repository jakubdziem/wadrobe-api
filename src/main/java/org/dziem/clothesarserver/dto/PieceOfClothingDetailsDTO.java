package org.dziem.clothesarserver.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class PieceOfClothingDetailsDTO {
    private String imageUrl;
    private String category;
    private Date purchaseDate;
    private String size;
    private String condition;
    private String note;
    private BigDecimal price;
    private List<String> tags;
    private String brand;
    private List<String> occasions;
    private String arUrl;
}
