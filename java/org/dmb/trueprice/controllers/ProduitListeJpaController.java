/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.controllers;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.entities.Produit;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */

@Singleton
public class ProduitListeJpaController implements Serializable {

    private static final Logger log 
    = InitContextListener.getLogger( ProduitListeJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;    
    
//    public ProduitListeJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Produit produitListe) {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            entManager.persist(produitListe);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void edit(Produit produitListe) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            produitListe = entManager.merge(produitListe);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = produitListe.getPdtId();
                if (findProduitListe(id) == null) {
                    throw new NonexistentEntityException("The produitListe with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Produit produitListe;
            try {
                produitListe = entManager.getReference(Produit.class, id);
                produitListe.getPdtId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produitListe with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(produitListe);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<Produit> findProduitListeEntities() {
        return findProduitListeEntities(true, -1, -1);
    }

    public List<Produit> findProduitListeEntities(int maxResults, int firstResult) {
        return findProduitListeEntities(false, maxResults, firstResult);
    }

    private List<Produit> findProduitListeEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Produit.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("ProduitListe.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            em.close();
        }
    }

    public Produit findProduitListe(Long id) {
//        EntityManager em = getEntityManager();
        try {
            return entManager.find(Produit.class, id);
        } finally {
//            em.close();
        }
    }

    public int getProduitListeCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<Produit> rt = cq.from(Produit.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            Query q = entManager.createNamedQuery("ProduitListe.findAll");
            return q.getResultList().size();
                     
        } finally {
//            em.close();
        }
    }
    
}
