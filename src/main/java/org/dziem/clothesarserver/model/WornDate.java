package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "worn_date")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WornDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worn_date_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "piece_of_clothing_id", nullable = false)
    private PieceOfClothing pieceOfClothing;
}

