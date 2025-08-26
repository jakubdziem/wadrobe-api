package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "size")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id", nullable = false)
    private Long id;

    @Column(length = 50)
    private String name;
}

