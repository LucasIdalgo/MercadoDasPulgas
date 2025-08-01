package com.challenge.Desafio.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Moedas")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoedasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdMoeda")
    private Long IdMoeda;

    @Column(name = "Nome", length = 25, nullable = false)
    private String Nome;

    @Column(name = "Ativo", columnDefinition = "BIT DEFAULT 1", nullable = false)
    private Boolean Ativo;
}
