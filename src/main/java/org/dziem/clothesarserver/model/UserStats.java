package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_stats_id", nullable = false)
    private UUID id;

    @Column(nullable = false, precision = 15, scale = 2)
    private Double wardrobePrice;

    @Column(nullable = false)
    private Short outfitsCount;

    @Column(nullable = false)
    private Short clothesCount;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}