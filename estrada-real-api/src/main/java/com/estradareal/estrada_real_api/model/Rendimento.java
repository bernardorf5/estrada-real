package com.estradareal.estrada_real_api.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rendimento")
public class Rendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(nullable = false)
    private String entidadeEmpregadora;

    @Column(nullable = false)
    private BigDecimal valor;

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public String getEntidadeEmpregadora() { return entidadeEmpregadora; }
    public void setEntidadeEmpregadora(String e) { this.entidadeEmpregadora = e; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
