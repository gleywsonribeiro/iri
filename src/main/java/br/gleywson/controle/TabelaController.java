/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.modelo.Avaliacao;
import br.gleywson.modelo.Opcao;
import br.gleywson.modelo.Pergunta;
import br.gleywson.modelo.Pesquisa;
import br.gleywson.modelo.Resposta;
import br.gleywson.modelo.dao.AvaliacaoFacade;
import br.gleywson.modelo.dao.PerguntaFacade;
import br.gleywson.modelo.dao.PesquisaFacade;
import br.gleywson.modelo.dao.RespostaFacade;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

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
    @EJB
    private PesquisaFacade pf;

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

    public void geraExcel() throws IOException {
        avaliacoes = dao.getAvaliacoesPorPesquisa(pf.find(3L));
        Pesquisa pesquisa = avaliacoes.get(0).getPesquisa();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("arquivo");
        
        for (Avaliacao avaliacao : avaliacoes) {
            avaliacao.getRespostas().stream().filter(r -> )
        }

//        int rownum = 0;
//        int colnum = 0;
//
//        Row linha = sheet.createRow(0);
//
//        for (int i = 0; i < avaliacoes.get(0).getPesquisa().getPerguntas().size(); i++) {
//            Cell cell = linha.createCell(i);
//            cell.setCellValue(avaliacoes.get(0).getPesquisa().getPerguntas().get(i).getDescricao());
//        }
//
//        for (int line = 1, i = 0; i < avaliacoes.size(); i++, line++) {
//            linha = sheet.createRow(line);
//            for (int j = 0; j < avaliacoes.get(i).getRespostas().size(); j++) {
//                Cell cell = linha.createCell(j);
//                cell.setCellValue(avaliacoes.get(i).getRespostas().get(j).getOpcao().getDescricao());
//            }
//
//        }
//       -------------
        
        for (int i = 1; i < avaliacoes.size(); i++) {
            if (i == 1) {
                Row header = sheet.createRow(i - 1);
                for (int j = 0; j < avaliacoes.get(i).getRespostas().size(); j++) {
                    Cell cell = header.createCell(j);
                    cell.setCellValue(avaliacoes.get(i).getRespostas().get(j).getPergunta().getDescricao());
                }
            }
            Row linha = sheet.createRow(i);
            for (int j = 0; j < avaliacoes.get(i).getRespostas().size(); j++) {
                Cell cell = linha.createCell(j);
                cell.setCellValue(avaliacoes.get(i).getRespostas().get(j).getOpcao().getDescricao());
            }

        }
//       -------------
        String file = "C:\\Users\\Gleywson\\Desktop\\teste.xls";
        File arquivo = new File(file);
        arquivo.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(arquivo);
        workbook.write(outputStream);

        outputStream.close();

    }

}
