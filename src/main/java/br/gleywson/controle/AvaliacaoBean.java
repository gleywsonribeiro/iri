/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.jsf.util.JsfUtil;
import br.gleywson.modelo.Avaliacao;
import br.gleywson.modelo.Pesquisa;
import br.gleywson.modelo.dao.AvaliacaoFacade;
import br.gleywson.modelo.dao.PesquisaFacade;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author gleyw
 */
@ManagedBean
@ViewScoped
public class AvaliacaoBean implements Serializable {

    private Pesquisa pesquisa;
    private List<Pesquisa> pesquisas;

    private List<Avaliacao> avaliacoes;
    private Avaliacao avaliacao;

    @EJB
    private PesquisaFacade pesquisaFacade;
    @EJB
    private AvaliacaoFacade avaliacaoFacade;

    @PostConstruct
    public void init() {
        pesquisas = pesquisaFacade.findAll();
    }

    public void atualizaAvaliacoesPorPesquisa() {
        avaliacoes = avaliacaoFacade.getAvaliacoesPorPesquisa(pesquisa);
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public List<Pesquisa> getPesquisas() {
        return pesquisas;
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }
    
    public int getTotalPesquisas() {
        return avaliacaoFacade.count();
    }

    public void remover() {
        try {
            avaliacaoFacade.remove(avaliacao);
            atualizaAvaliacoesPorPesquisa();
            JsfUtil.addMessage("Excluído com sucesso!");
        } catch (Exception e) {
            JsfUtil.addMessage("Não foi possível excluir avaliação! \n" + e.getMessage());
        }
    }

}
