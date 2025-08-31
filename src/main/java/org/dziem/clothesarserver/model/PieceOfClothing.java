package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "piece_of_clothing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PieceOfClothing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "piece_of_clothing_id", nullable = false)
    private Long id;

    private String name;
    private String brand;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    private String imageUrl;
    private String arUrl;
    private String note;

    private Date purchaseDate;
    private Date lastWornDate;

    private Integer wearCount = 0;
    private Boolean isFavorite = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne @JoinColumn(name = "condition_id")
    private Condition condition;

    @ManyToOne @JoinColumn(name = "size_id")
    private Size size;

    @ManyToOne @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "tag_has_piece_of_clothing",
            joinColumns = @JoinColumn(name = "piece_of_clothing_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "pieceOfClothing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WornDate> wornDates = new ArrayList<>();

    @ManyToMany(mappedBy = "pieces")
    private List<Occasion> occasions = new ArrayList<>();

    public int incrementWearCount() {
        this.wearCount++;
        return wearCount;
    }
}

