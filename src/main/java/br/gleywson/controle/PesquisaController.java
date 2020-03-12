/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.jsf.util.JsfUtil;
import br.gleywson.modelo.Avaliacao;
import br.gleywson.modelo.Pesquisa;
import br.gleywson.modelo.Resposta;
import br.gleywson.modelo.Tipo;
import br.gleywson.modelo.dao.AvaliacaoFacade;
import br.gleywson.modelo.dao.PesquisaFacade;
import java.io.IOException;
import java.util.List;
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
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author gleywson
 */
@ManagedBean
@ViewScoped
public class PesquisaController {

    private Pesquisa pesquisa;
    private List<Pesquisa> pesquisas;
    @EJB
    private PesquisaFacade pesquisaFacade;
    @EJB
    private AvaliacaoFacade dao;

    public PesquisaController() {
        pesquisa = new Pesquisa();
    }

    public void salvar() {
        if (pesquisa.getId() == null) {
            pesquisaFacade.create(pesquisa);
            JsfUtil.addMessage("Salvo com sucesso!");
        } else {
            pesquisaFacade.edit(pesquisa);
            JsfUtil.addMessage("Atualizado com sucesso!");
        }
        pesquisa = new Pesquisa();
    }

    public Pesquisa getPesquisa() {
        return pesquisa;
    }

    public void setPesquisa(Pesquisa pesquisa) {
        this.pesquisa = pesquisa;
    }

    public List<Pesquisa> getPesquisas() {
        this.pesquisas = pesquisaFacade.findAll();
        return this.pesquisas;
    }

    public List<Pesquisa> getPesquisasAtivas() {
        return pesquisaFacade.findAllActive();
    }

    public void remover() {
        try {
            pesquisaFacade.remove(pesquisa);
            JsfUtil.addMessage("Removido com sucesso!");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Existem avaliações para esta pesquisa.");
        }
    }

    public void exportar() throws IOException {
        List<Avaliacao> avaliacoes = dao.getAvaliacoesPorPesquisa(pesquisaFacade.find(pesquisa.getId()));
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("tabulacao");

        avaliacoes.forEach((avaliacao) -> {
            avaliacao.getRespostas().sort((p1, p2) -> p1.getPergunta().getId().compareTo(p2.getPergunta().getId()));
        });

        int line = 1;
        int coluna = 0;

        //cabecalho
        Row cabecalho = sheet.createRow(line - 1);
        for (Resposta resposta : avaliacoes.get(0).getRespostas()) {
            Cell celula = cabecalho.createCell(coluna++);
            if (resposta.getPergunta().getTipo().equals(Tipo.AUTOMATICO)) {
                String[] valor = resposta.getPergunta().getDescricao().split(".");
                celula.setCellValue(valor[0]);
            } else {
                celula.setCellValue(resposta.getPergunta().getDescricao());
            }
            
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
