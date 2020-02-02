/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author gleywson
 */
@Entity
public class Pergunta implements Serializable {

    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    private boolean invertido;
    
    @Enumerated(EnumType.STRING)
    private Qualificador qualificador; 
    
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    
    @ManyToOne
    private Pesquisa pesquisa;
    
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL)
    private List<Opcao> opcoes;

    public Pergunta() {
        this.tipo = Tipo.NORMAL;
        this.opcoes = new ArrayList<Opcao>();
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isInvertido() {
        return invertido;
    }

    public void setInvertido(boolean invertido) {
        this.invertido = invertido;
    }

    public Qualificador getQualificador() {
        return qualificador;
    }

    public void setQualificador(Qualificador qualificador) {
        this.qualificador = qualificador;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public List<Opcao> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<Opcao> opcoes) {
        this.opcoes = opcoes;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pergunta other = (Pergunta) obj;
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        return true;
    }

    

    @Override
    public String toString() {
        return "br.gleywson.modelo.Pergunta[ id=" + id + " ]";
    }
    
}
