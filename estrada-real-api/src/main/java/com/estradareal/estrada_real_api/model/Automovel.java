package com.estradareal.estrada_real_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "automovel")
public class Automovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id")
    private Usuario proprietario;

    @Column(nullable = false)
    private String matricula;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false, unique = true)
    private String placa;

    public Long getId() { return id; }
    public Usuario getProprietario() { return proprietario; }
    public void setProprietario(Usuario proprietario) { this.proprietario = proprietario; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
}
