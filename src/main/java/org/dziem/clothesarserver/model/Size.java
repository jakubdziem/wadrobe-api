package org.dziem.clothesarserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "size")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "size_id", nullable = false)
    private UUID id;

    @Column(length = 50)
    private String name;
}

