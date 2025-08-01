package com.challenge.Desafio.Entities;

import com.challenge.Desafio.Enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Transacoes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransacoesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTransacao")
    private Long IdTransacao;

    @CreatedDate
    @Column(name = "DataTransacao", nullable = false)
    private LocalDate DataTransacao;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private TipoTransacao TipoTransacao;

    @ManyToOne
    @JoinColumn(name = "IdProduto", foreignKey = @ForeignKey(name = "FK_Produto_Transacao"))
    private ProdutosEntity Produto;

    @ManyToOne
    @JoinColumn(name = "IdReino", nullable = false, foreignKey = @ForeignKey(name = "FK_Reino_Transacao"))
    private ReinosEntity Reino;

    @ManyToOne
    @JoinColumn(name = "IdMoedaOrigem", nullable = false, foreignKey = @ForeignKey(name = "FK_MoedaOrigem_Transacao"))
    private MoedasEntity MoedaOrigem;

    @ManyToOne
    @JoinColumn(name = "IdMoedaDestino", nullable = false, foreignKey = @ForeignKey(name = "FK_MoedaDestino_Transacao"))
    private MoedasEntity MoedaDestino;

    @Column(name = "Quantidade", precision = 10, scale = 2, nullable = false)
    private BigDecimal Quantidade;

    @Column(name = "ValorTransacao", precision = 10, scale = 2, nullable = false)
    private BigDecimal ValorTransacao;

    @Column(name = "ValorFinalTransacao", precision = 10, scale = 2, nullable = false)
    private BigDecimal ValorFinalTransacao;
}
