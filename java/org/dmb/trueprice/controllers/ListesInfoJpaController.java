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
import org.dmb.trueprice.entities.ListeInfo;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
public class ListesInfoJpaController implements Serializable {


    private static final Logger log 
    = InitContextListener.getLogger( ProduitListeJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;         
    
//    public ListesInfoJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    
    public ListesInfoJpaController(EntityManager entManager) {
        this.entManager = entManager;
    }
    
    

    public void create(ListeInfo listesInfo) {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            em.persist(listesInfo);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void edit(ListeInfo listesInfo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            listesInfo = em.merge(listesInfo);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = listesInfo.getListesInfoId();
                if (findListesInfo(id) == null) {
                    throw new NonexistentEntityException("The listesInfo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            ListeInfo listesInfo;
            try {
                listesInfo = em.getReference(ListeInfo.class, id);
                listesInfo.getListesInfoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The listesInfo with id " + id + " no longer exists.", enfe);
            }
            em.remove(listesInfo);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<ListeInfo> findListesInfoEntities() {
        return findListesInfoEntities(true, -1, -1);
    }

    public List<ListeInfo> findListesInfoEntities(int maxResults, int firstResult) {
        return findListesInfoEntities(false, maxResults, firstResult);
    }

    private List<ListeInfo> findListesInfoEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(ListeInfo.class));
//            Query q = em.createQuery(cq);
            Query q = entManager.createNamedQuery("ListesInfo.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            em.close();
        }
    }

    public ListeInfo findListesInfo(Integer id) {
//        EntityManager em = getEntityManager();
        try {
//            return em.find(ListeInfo.class, id);
            return entManager.find(ListeInfo.class, id);
        } finally {
//            em.close();
        }
    }

    public int getListesInfoCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            Root<ListeInfo> rt = cq.from(ListeInfo.class);
//            cq.select(em.getCriteriaBuilder().count(rt));
//            Query q = em.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            Query q = entManager.createNamedQuery("ListesInfo.findAll");
            return q.getResultList().size();
                      
        } finally {
//            em.close();
        }
    }
    
}
