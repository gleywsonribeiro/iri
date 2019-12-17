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
import br.gleywson.modelo.Qualificador;
import br.gleywson.modelo.Tipo;
import br.gleywson.modelo.dao.PerguntaFacade;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
public class PerguntaController {

    private Pesquisa pesquisa;

    private Pergunta pergunta;
    private Opcao opcao;
    private List<Pergunta> perguntas;

    private final List<String> options;

    @EJB
    private PerguntaFacade perguntaFacade;

    public PerguntaController() {
        this.options = Arrays.asList(
                "A - Não me descreve bem",
                "B - Não me descreve",
                "C - Sou neutro",
                "D - Me descreve",
                "E - Me descreve muito bem"
        );
//        pesquisa = new Pesquisa();
        pergunta = new Pergunta();
        opcao = new Opcao();
    }

    public void salvar() {

        pergunta.setPesquisa(pesquisa);
        pesquisa.getPerguntas().add(pergunta);
        
        if (pergunta.getId() == null) {
            if (pergunta.getTipo() == Tipo.AUTOMATICO) {
                geraOptions();
            }

            perguntaFacade.create(pergunta);
            JsfUtil.addMessage("Salvo");
        } else {
            perguntaFacade.edit(pergunta);
            JsfUtil.addMessage("Alterado com sucesso!");
        }
        pergunta = new Pergunta();
    }

    //não esta sendo usado por enquanto
    public void insereOpcao() {
        opcao.setPergunta(pergunta);
        pergunta.getOpcoes().add(opcao);
        opcao = new Opcao();
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
    }

    public List<Pergunta> getPerguntas() {
        perguntas = perguntaFacade.findAll();
        return perguntas;
    }

    public void setPerguntas(List<Pergunta> perguntas) {
        this.perguntas = perguntas;
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public Opcao getOpcao() {
        return opcao;
    }

    public void setOpcao(Opcao opcao) {
        this.opcao = opcao;
    }

    public Qualificador[] listaQualificadores() {
        return Qualificador.values();
    }

    public Tipo[] listaTipos() {
        return Tipo.values();
    }

    private void geraOptions() {
        //limpa a lista de opcoes antes
        pergunta.getOpcoes().clear();

        if (pergunta.isInvertido()) {
            List temp = options;
            Collections.reverse(temp);
            for (int i = 0; i < options.size(); i++) {
                Opcao o = new Opcao();
                o.setDescricao(options.get(i));
                o.setPeso(i);
                o.setPergunta(pergunta);
                pergunta.getOpcoes().add(o);

            }
        } else {
            for (int i = 0; i < options.size(); i++) {
                Opcao o = new Opcao();
                o.setDescricao(options.get(i));
                o.setPeso(i);
                o.setPergunta(pergunta);
                pergunta.getOpcoes().add(o);
            }
        }
    }

}
