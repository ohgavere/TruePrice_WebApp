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
import org.dmb.trueprice.entities.Subcategory;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
@Singleton
public class SubcategoryJpaController implements Serializable {

private static final Logger log 
= InitContextListener.getLogger( SubcategoryJpaController.class) ;
     
    
        @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;
        

//    public SubcategoryJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Subcategory subcategory) {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            em.persist(subcategory);
            entManager.persist(subcategory);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void edit(Subcategory subcategory) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
//            subcategory = em.merge(subcategory);
            subcategory = entManager.merge(subcategory);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = subcategory.getSctgId();
                if (findSubcategory(id) == null) {
                    throw new NonexistentEntityException("The subcategory with id " + id + " no longer exists.");
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
            Subcategory subcategory;
            try {
                subcategory = entManager.getReference(Subcategory.class, id);
                subcategory.getSctgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subcategory with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(subcategory);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<Subcategory> findSubcategoryEntities() {
        return findSubcategoryEntities(true, -1, -1);
    }

    public List<Subcategory> findSubcategoryEntities(int maxResults, int firstResult) {
        return findSubcategoryEntities(false, maxResults, firstResult);
    }

    private List<Subcategory> findSubcategoryEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Subcategory.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("Subcategory.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            em.close();
        }
    }

    public Subcategory findSubcategory(Integer id) {
//        EntityManager em = getEntityManager();
        try {
            return entManager.find(Subcategory.class, id);
        } finally {
//            em.close();
        }
    }

    public int getSubcategoryCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<Subcategory> rt = cq.from(Subcategory.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
            
            Query q = entManager.createNamedQuery("Subcategory.findAll");
            
//            return ((Long) q.getSingleResult()).intValue();
            return q.getResultList().size();
        } catch (Exception e) {
            log.error("Error inside SubCategory Controller to get count, because > " + e.getMessage());            
        } finally {
//            em.close();
        }
    return 0;
    }
    
}
