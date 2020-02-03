/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.controle;

import br.gleywson.jsf.util.JsfUtil;
import br.gleywson.modelo.Usuario;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gleyw
 */
@ManagedBean
@SessionScoped
public class LoginController implements Serializable {

    private Usuario usuario;

    public LoginController() {
        this.usuario = new Usuario();
    }

    public String login() {
        if (usuario.getLogin().toLowerCase().equals("admin") && usuario.getSenha().equals("admin")) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession httpSession = (HttpSession) context.getExternalContext().getSession(false);
            //this.usuario = user;
            usuario.setNome("Administrador");
            httpSession.setAttribute("currentUser", usuario);
            return "dashboard?faces-redirect=true";
        } else if (usuario.getLogin().toLowerCase().equals("pesquisa") && usuario.getSenha().equals("empatia")) {
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession httpSession = (HttpSession) context.getExternalContext().getSession(false);
            //this.usuario = user;
            usuario.setNome("Visitante");
            httpSession.setAttribute("currentUser", usuario);
            return "dashboard?faces-redirect=true";
        } else {
            JsfUtil.addErrorMessage("Usuario ou senha inválidos!");
            return "";
        }

    }

    public void logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession) fc.getExternalContext().getSession(false);
        httpSession.setAttribute("currentUser", null);
        try {
            fc.getExternalContext().redirect(fc.getExternalContext().getRequestContextPath());
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        httpSession.invalidate();
//        JsfUtil.redirect("/index.xhtml");
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String saudacao() {
        return "Olá " + usuario.getNome();
    }
    
    public boolean isAdministrador() {
        return usuario.getLogin().toLowerCase().equals("admin");
    }

}
