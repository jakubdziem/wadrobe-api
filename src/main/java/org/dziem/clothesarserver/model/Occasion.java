package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "occasion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Occasion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "occasion_id", nullable = false)
    private Long id;

    @Column(length = 200)
    private String name;

    private LocalDate date;

    @ManyToMany
    @JoinTable(
            name = "occasion_has_piece_of_clothing",
            joinColumns = @JoinColumn(name = "occasion_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_of_clothing_id")
    )
    private List<PieceOfClothing> pieces = new ArrayList<>();
}

