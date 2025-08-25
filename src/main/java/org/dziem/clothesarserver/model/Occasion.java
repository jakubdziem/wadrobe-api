package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "occasion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Occasion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "occasion_id", nullable = false)
    private UUID id;

    @Column(length = 200)
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToMany
    @JoinTable(
            name = "occasion_has_piece_of_clothing",
            joinColumns = @JoinColumn(name = "occasion_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_of_clothing_id")
    )
    private List<PieceOfClothing> pieces = new ArrayList<>();
}

