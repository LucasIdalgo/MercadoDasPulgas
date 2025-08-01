package com.challenge.Desafio.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Reinos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReinosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdReino")
    private Long IdReino;

    @Column(name = "Nome", length = 25, nullable = false)
    private String Nome;

    @Column(name = "Ativo", columnDefinition = "BIT DEFAULT 1", nullable = false)
    private Boolean Ativo;
}
