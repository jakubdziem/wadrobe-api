package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Long id;

    @Column(length = 50)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "tag_has_piece_of_clothing",
            joinColumns = @JoinColumn(name = "tag_id"),
            inverseJoinColumns = @JoinColumn(name = "piece_of_clothing_id")
    )
    private List<PieceOfClothing> pieces = new ArrayList<>();
}

