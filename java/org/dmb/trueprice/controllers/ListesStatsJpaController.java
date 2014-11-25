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
import org.dmb.trueprice.entities.ListeStats;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
public class ListesStatsJpaController implements Serializable {


    private static final Logger log 
    = InitContextListener.getLogger( ProduitListeJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;         
    
//    public ListesStatsJpaController(EntityManagerFactory emf) {
    public ListesStatsJpaController(EntityManager entManager) {
//        this.emf = emf;
        this.entManager = entManager;
    }
//    private EntityManagerFactory emf = null;

//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(ListeStats listesStats) {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            em.persist(listesStats);
            entManager.persist(listesStats);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void edit(ListeStats listesStats) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            listesStats = em.merge(listesStats);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = listesStats.getListesStatsId();
                if (findListesStats(id) == null) {
                    throw new NonexistentEntityException("The listesStats with id " + id + " no longer exists.");
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
            ListeStats listesStats;
            try {
                listesStats = em.getReference(ListeStats.class, id);
                listesStats.getListesStatsId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The listesStats with id " + id + " no longer exists.", enfe);
            }
            em.remove(listesStats);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<ListeStats> findListesStatsEntities() {
        return findListesStatsEntities(true, -1, -1);
    }

    public List<ListeStats> findListesStatsEntities(int maxResults, int firstResult) {
        return findListesStatsEntities(false, maxResults, firstResult);
    }

    private List<ListeStats> findListesStatsEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(ListeStats.class));
//            Query q = em.createQuery(cq);
//            Query q = em.createNamedQuery("ListeStats.findAll");
            Query q = entManager.createNamedQuery("ListeStats.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            em.close();
        }
    }

    public ListeStats findListesStats(Integer id) {
//        EntityManager em = getEntityManager();
        try {
//            return em.find(ListeStats.class, id);
            return entManager.find(ListeStats.class, id);
        } finally {
//            em.close();
        }
    }

    public int getListesStatsCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
//            Root<ListeStats> rt = cq.from(ListeStats.class);
//            cq.select(em.getCriteriaBuilder().count(rt));
//            Query q = em.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            Query q = entManager.createNamedQuery("ListesStats.findAll");
            return q.getResultList().size();
                   
        } finally {
//            em.close();
        }
    }
    
}
