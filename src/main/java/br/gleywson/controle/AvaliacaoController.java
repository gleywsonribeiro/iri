/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.jsf.util.JsfUtil;
import br.gleywson.modelo.Avaliacao;
import br.gleywson.modelo.Pergunta;
import br.gleywson.modelo.Pesquisa;
import br.gleywson.modelo.Resposta;
import br.gleywson.modelo.dao.AvaliacaoFacade;
import br.gleywson.modelo.dao.PesquisaFacade;
import br.gleywson.modelo.dao.RespostaFacade;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author gleyw
 */
@ManagedBean
@ViewScoped
public class AvaliacaoController {

    private Pesquisa pesquisa;
    private List<Avaliacao> avaliacoes;
    
    private Avaliacao avaliacao;
    @EJB
    private PesquisaFacade pesquisaFacade;
    
    @EJB
    private AvaliacaoFacade avaliacaoFacade;
        
    private List<Resposta> respostas;

    @PostConstruct
    public void init() {
        String codigo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cod_pesquisa");
        pesquisa = pesquisaFacade.find(Long.parseLong(codigo));
        avaliacao = new Avaliacao();
        avaliacao.setPesquisa(pesquisa);
        
        respostas = new ArrayList<Resposta>();
        
        for (Pergunta p : avaliacao.getPesquisa().getPerguntas()) {
            Resposta resposta = new Resposta();
            resposta.setAvaliacao(avaliacao);
            resposta.setPergunta(p);
            avaliacao.getRespostas().add(resposta);
//            respostas.add(resposta);
            
        }
        
    }

    public AvaliacaoController() {
        avaliacoes = new ArrayList<Avaliacao>();
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

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public String salvar() {
        avaliacao.setDataHora(new Date());
       if(avaliacao.getId() == null) {
           avaliacaoFacade.create(avaliacao);
       } else {
           avaliacaoFacade.edit(avaliacao);
       }
        JsfUtil.addMessage("salvo com sucesso!");
        System.out.println("Chamou");
        return "concluido?faces-redirect=true";
    }

    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    
    
    
}
