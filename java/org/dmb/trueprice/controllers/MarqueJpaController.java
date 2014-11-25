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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.entities.Marque;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
@Singleton
public class MarqueJpaController implements Serializable {

//    public MarqueJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    

    private static final Logger log 
    = InitContextListener.getLogger( ProduitListeJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;     

    public void create(Marque marque) {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            entManager.persist(marque);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void edit(Marque marque) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            marque =  entManager.merge(marque);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = marque.getMrqId();
                if (findMarque(id) == null) {
                    throw new NonexistentEntityException("The marque with id " + id + " no longer exists.");
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
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            Marque marque;
            try {
                marque = entManager.getReference(Marque.class, id);
                marque.getMrqId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The marque with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(marque);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<Marque> findMarqueEntities() {
        return findMarqueEntities(true, -1, -1);
    }

    public List<Marque> findMarqueEntities(int maxResults, int firstResult) {
        return findMarqueEntities(false, maxResults, firstResult);
    }

    private List<Marque> findMarqueEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Marque.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("Marque.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            entManager.close();
        }
    }

    public Marque findMarque(Integer id) {
//        EntityManager em = getEntityManager();
        try {
            return entManager.find(Marque.class, id);
        } finally {
//            em.close();
        }
    }

    public int getMarqueCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<Marque> rt = cq.from(Marque.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            Query q = entManager.createNamedQuery("Marque.findAll");
            return q.getResultList().size();
                     
        } finally {
//            em.close();
        }
    }
    
}
