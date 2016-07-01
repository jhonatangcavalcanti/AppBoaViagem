package com.exemplolivroandroid.jhonatan.boaviagem.domain;

import java.util.Date;

/**
 * Created by jhonatan on 30/06/16. *
 */
public class Gasto {
    private Long id;
    private Date data;
    private String categoria;
    private String descricao;
    private Double valor;
    private String local;
    private Integer viagemID;

    public Gasto() {}

    public Gasto(Long id, Date data, String categoria, String descricao, Double valor, String local, Integer viagemID) {
        this.id = id;
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.local = local;
        this.viagemID = viagemID;
    }

    public Gasto(Date data, String categoria, String descricao, Double valor, String local, Integer viagemID) {
        this.data = data;
        this.categoria = categoria;
        this.descricao = descricao;
        this.valor = valor;
        this.local = local;
        this.viagemID = viagemID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getViagemID() {
        return viagemID;
    }

    public void setViagemID(Integer viagemID) {
        this.viagemID = viagemID;
    }
}
