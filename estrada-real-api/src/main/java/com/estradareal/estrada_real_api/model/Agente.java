package com.estradareal.estrada_real_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agente")
public class Agente extends Usuario {

    private String cnpj;
    private String razaoSocial;

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
}
