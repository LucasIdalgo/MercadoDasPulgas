package com.challenge.Desafio.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Produtos")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProdutosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdProduto")
    private Long IdProduto;

    @Column(name = "Descricao", length = 100, nullable = false)
    private String Descricao;

    @Column(name = "Valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal Valor;

    @ManyToOne
    @JoinColumn(name = "IdMoeda", nullable = false, foreignKey = @ForeignKey(name = "FK_Moeda_Produto"))
    private MoedasEntity Moeda;

    @ManyToOne
    @JoinColumn(name = "IdReino", nullable = false, foreignKey = @ForeignKey(name = "FK_Reino_Produto"))
    private ReinosEntity Reino;

    @Column(name = "Ativo", columnDefinition = "BIT DEFAULT 1", nullable = false)
    private Boolean Ativo;
}
