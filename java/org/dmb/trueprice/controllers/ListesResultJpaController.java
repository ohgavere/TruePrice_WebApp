/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dmb.trueprice.controllers;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.entities.ListeResult;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
public class ListesResultJpaController implements Serializable {
    

    private static final Logger log 
    = InitContextListener.getLogger( ProduitListeJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;         

//    public ListesResultJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    public ListesResultJpaController(EntityManager entManager) {
        this.entManager = entManager;
    }

    
    
    public void create(ListeResult listesResult) {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            em.persist(listesResult);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void edit(ListeResult listesResult) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            listesResult = em.merge(listesResult);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = listesResult.getListesResultId();
                if (findListesResult(id) == null) {
                    throw new NonexistentEntityException("The listesResult with id " + id + " no longer exists.");
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
            ListeResult listesResult;
            try {
                listesResult = em.getReference(ListeResult.class, id);
                listesResult.getListesResultId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The listesResult with id " + id + " no longer exists.", enfe);
            }
            em.remove(listesResult);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<ListeResult> findListesResultEntities() {
        return findListesResultEntities(true, -1, -1);
    }

    public List<ListeResult> findListesResultEntities(int maxResults, int firstResult) {
        return findListesResultEntities(false, maxResults, firstResult);
    }

    private List<ListeResult> findListesResultEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(ListeResult.class));
//            Query q = em.createQuery(cq);
            Query q = entManager.createNamedQuery("ListesResult.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            em.close();
        }
    }

    public ListeResult findListesResult(Long id) {
//        EntityManager em = getEntityManager();
        try {
//            return em.find(ListeResult.class, id);
            return entManager.find(ListeResult.class, id);
        } finally {
//            em.close();
        }
    }

    public int getListesResultCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            Root<ListeResult> rt = cq.from(ListeResult.class);
//            cq.select(em.getCriteriaBuilder().count(rt));
//            Query q = em.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            Query q = entManager.createNamedQuery("ListesResult.findAll");
            return q.getResultList().size();
                          
        } finally {
//            em.close();
        }
    }
    
}
