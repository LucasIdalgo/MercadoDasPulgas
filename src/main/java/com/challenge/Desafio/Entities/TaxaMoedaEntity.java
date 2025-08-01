package com.challenge.Desafio.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "TaxaMoeda")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaxaMoedaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTaxaMoeda")
    private Long IdTaxaMoeda;

    @CreatedDate
    @Column(name = "Data", nullable = false)
    private LocalDate Data;

    @ManyToOne
    @JoinColumn(name = "IdMoedaOrigem", nullable = false, foreignKey = @ForeignKey(name = "FK_MoedaOrigem_Taxa"))
    private MoedasEntity MoedaOrigem;

    @Column(name = "Taxa", precision = 10, scale = 2, nullable = false)
    private BigDecimal Taxa;

    @ManyToOne
    @JoinColumn(name = "IdMoedaDestino", nullable = false, foreignKey = @ForeignKey(name = "FK_MoedaDestino_Taxa"))
    private MoedasEntity MoedaDestino;

    @ManyToOne
    @JoinColumn(name = "IdProduto", foreignKey = @ForeignKey(name = "FK_Produto_Taxa"))
    private ProdutosEntity Produto;

    @Column(name = "Operacao", nullable = false)
    private Long Operacao;
}
