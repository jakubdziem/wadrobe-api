package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "worn_date")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WornDate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "worn_date_id", nullable = false)
    private UUID id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "piece_of_clothing_id", nullable = false)
    private PieceOfClothing pieceOfClothing;
}

