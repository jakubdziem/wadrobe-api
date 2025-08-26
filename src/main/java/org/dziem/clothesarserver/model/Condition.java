package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "condition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id", nullable = false)
    private Long id;

    @Column(length = 50)
    private String name;
}

