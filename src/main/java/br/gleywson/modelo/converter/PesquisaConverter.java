/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.modelo.converter;

import br.gleywson.modelo.Pesquisa;
import br.gleywson.modelo.dao.PesquisaFacade;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;

/**
 *
 * @author gleywson
 */
@FacesConverter(value = "pesquisaConverter")
public class PesquisaConverter implements Converter {

    
    private PesquisaFacade repositorio;
    
     private PesquisaFacade getFacade() {
        try {
            return (PesquisaFacade) new InitialContext().lookup("java:global/iri/PesquisaFacade"); //[java:global/template/PerguntaFacade!br.gleywson.modelo.dao.PerguntaFacade, java:global/template/PerguntaFacade]
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Pesquisa pesquisa = null;
        repositorio = getFacade();
        if (value != null) {
            Long id = new Long(value);
            pesquisa = repositorio.find(id);
        }
        return pesquisa;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) {
            return ((Pesquisa) value).getId().toString();
        } else {
            return "";
        }
    }

}
