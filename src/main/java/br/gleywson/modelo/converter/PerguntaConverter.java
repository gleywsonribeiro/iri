/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.modelo.converter;

import br.gleywson.modelo.Pergunta;
import br.gleywson.modelo.dao.PerguntaFacade;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

/**
 *
 * @author gleyw
 */
@FacesConverter(value = "perguntaConverter")
public class PerguntaConverter implements Converter{

    private PerguntaFacade repositorio;
    
    private PerguntaFacade getFacade() {
        try {
            return (PerguntaFacade) new InitialContext().lookup("java:global/iri/PerguntaFacade"); //[java:global/template/PerguntaFacade!br.gleywson.modelo.dao.PerguntaFacade, java:global/template/PerguntaFacade]
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        Pergunta pergunta = null;
        repositorio = getFacade();
        if (value != null) {
            Long id = new Long(value);
            pergunta = repositorio.find(id);
        }
        return pergunta;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        if (value != null) {
            return ((Pergunta) value).getId().toString();
        } else {
            return "";
        }
    }
    
}
