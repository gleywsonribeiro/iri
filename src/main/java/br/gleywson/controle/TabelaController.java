/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.modelo.Avaliacao;
import br.gleywson.modelo.Pergunta;
import br.gleywson.modelo.Resposta;
import br.gleywson.modelo.dao.AvaliacaoFacade;
import br.gleywson.modelo.dao.PerguntaFacade;
import br.gleywson.modelo.dao.PesquisaFacade;
import br.gleywson.modelo.dao.RespostaFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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

    public void geraExcel() throws IOException {
        avaliacoes = dao.getAvaliacoesPorPesquisa(pf.find(1L));
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("arquivo");

        avaliacoes.forEach((avaliacao) -> {
            avaliacao.getRespostas().sort((p1, p2) -> p1.getPergunta().getDescricao().compareTo(p2.getPergunta().getDescricao()));
        });

        int line = 1;
        int coluna = 0;

        //cabecalho
        Row cabecalho = sheet.createRow(line - 1);
        for (Resposta resposta : avaliacoes.get(0).getRespostas()) {
            Cell celula = cabecalho.createCell(coluna++);
            celula.setCellValue(resposta.getPergunta().getDescricao());
            CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);

            cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            cellStyle.setFont(font);
            celula.setCellStyle(cellStyle);
        }

//        Estilo
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFont(font);

        Cell cell = cabecalho.createCell(coluna++);
        cell.setCellValue("PT");
        cell.setCellStyle(cellStyle);

        cell = cabecalho.createCell(coluna++);
        cell.setCellValue("PD");
        cell.setCellStyle(cellStyle);

        cell = cabecalho.createCell(coluna++);
        cell.setCellValue("EC");
        cell.setCellStyle(cellStyle);

        cell = cabecalho.createCell(coluna++);
        cell.setCellValue("FS");
        cell.setCellStyle(cellStyle);

        //corpo da tabela
        for (Avaliacao avaliacao : avaliacoes) {
            Row linha = sheet.createRow(line++);
            int col = 0;
            for (Resposta resposta : avaliacao.getRespostas()) {
                Cell celula = linha.createCell(col++);
                celula.setCellValue(resposta.getOpcao().getDescricao());
            }
            Cell celula = linha.createCell(col++);
            celula.setCellValue(avaliacao.getPT());

            celula = linha.createCell(col++);
            celula.setCellValue(avaliacao.getPD());

            celula = linha.createCell(col++);
            celula.setCellValue(avaliacao.getEC());

            celula = linha.createCell(col++);
            celula.setCellValue(avaliacao.getFS());

        }

        int quantidadeColunas = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int i = 0; i < quantidadeColunas; i++) {
            sheet.autoSizeColumn(i);
        }

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment; filename=dados.xls");

        try {
            try (ServletOutputStream out = response.getOutputStream()) {
                workbook.write(out);
                out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FacesContext faces = FacesContext.getCurrentInstance();
        faces.responseComplete();

    }
    
   
}
