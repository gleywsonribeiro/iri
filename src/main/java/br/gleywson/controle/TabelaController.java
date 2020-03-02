/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.modelo.Avaliacao;
import br.gleywson.modelo.Opcao;
import br.gleywson.modelo.Pergunta;
import br.gleywson.modelo.Resposta;
import br.gleywson.modelo.dao.AvaliacaoFacade;
import br.gleywson.modelo.dao.PerguntaFacade;
import br.gleywson.modelo.dao.RespostaFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author Gleywson
 */
@ManagedBean
@ViewScoped
public class TabelaController implements Serializable {

    private static final long serialVersionUID = 735937576058996679L;

    @EJB
    private AvaliacaoFacade dao;
    @EJB
    private PerguntaFacade pdao;
    @EJB
    private RespostaFacade rdao;
    
    private List<Avaliacao> avaliacoes = new ArrayList<Avaliacao>();
    private List<Resposta> respostas;
    private List<Pergunta> perguntas;

    @PostConstruct
    private void init() {
         avaliacoes = dao.findAll();
         perguntas = pdao.findAll();
         respostas = rdao.findAll();
    }
    
    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }

    public List<Pergunta> getPerguntas() {
        return perguntas;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }
    
    public void teste() {
        Map<Pergunta, Opcao> mapa = new HashMap<Pergunta, Opcao>();
        for (Avaliacao avaliacao : avaliacoes) {
            for (Resposta resposta : avaliacao.getRespostas()) {
                mapa.put(resposta.getPergunta(), resposta.getOpcao());
            }
        }
    }

}
