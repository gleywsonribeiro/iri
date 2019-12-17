/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.jsf.util.JsfUtil;
import br.gleywson.modelo.Opcao;
import br.gleywson.modelo.Pergunta;
import br.gleywson.modelo.Pesquisa;
import br.gleywson.modelo.dao.PerguntaFacade;
import br.gleywson.modelo.dao.PesquisaFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author gleywson
 */
@ManagedBean
@ViewScoped
public class PerguntaEdicaoController {

    private Pesquisa pesquisa;
    private List<Pesquisa> pesquisas;
    private Opcao opcao;

    private Pergunta pergunta;

    @EJB
    private PesquisaFacade pesquisaFacade;
    @EJB
    private PerguntaFacade perguntaFacade;

    public PerguntaEdicaoController() {
        pergunta = new Pergunta();
        opcao = new Opcao();
    }

    public void addOpcao() {
        opcao.setPergunta(pergunta);
        pergunta.getOpcoes().add(opcao);
        opcao = new Opcao();
    }

    public void atualizar() {
        if (pergunta.getId() != null) {
            perguntaFacade.edit(pergunta);
            JsfUtil.addMessage("Pergunta atualizada com sucesso!");
        } else {
            JsfUtil.addMessage("Selecione uma pergunta para editar");
        }
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public List<Pesquisa> getPesquisas() {
        pesquisas = pesquisaFacade.findAll();
        return pesquisas;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public Opcao getOpcao() {
        return opcao;
    }

    public void setOpcao(Opcao opcao) {
        this.opcao = opcao;
    }

}
