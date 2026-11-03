package com.estradareal.estrada_real_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "contrato_credito")
public class ContratoCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "banco_id")
    private Agente banco;

    @Column(nullable = false)
    private BigDecimal valor;

    public Long getId() { return id; }
    public Agente getBanco() { return banco; }
    public void setBanco(Agente banco) { this.banco = banco; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
