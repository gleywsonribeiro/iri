/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gleywson.modelo.dao;

import br.gleywson.modelo.Avaliacao;
import br.gleywson.modelo.Pesquisa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author gleywson
 */
@javax.ejb.Stateless
public class AvaliacaoFacade extends AbstractFacade<Avaliacao> {

    @PersistenceContext(unitName = "iriPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AvaliacaoFacade() {
        super(Avaliacao.class);
    }
    
    public List<Avaliacao> getAvaliacoesPorPesquisa(Pesquisa pesquisa) {
        Query query = em.createQuery("SELECT a FROM Avaliacao AS a WHERE A.pesquisa = :pesquisa", Avaliacao.class);
        query.setParameter("pesquisa", pesquisa);
        return query.getResultList();
    }
}
