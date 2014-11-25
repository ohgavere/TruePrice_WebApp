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
import org.dmb.trueprice.entities.Category;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
@Singleton
public class CategoryJpaController implements Serializable {

    
private static final Logger log 
= InitContextListener.getLogger( CategoryJpaController.class) ;
    
    
//    public MembreJpaController(EntityManager em) {
//        this.entManager = em;
//    }
//    private EntityManagerFactory emf = null;
//    @PersistenceUnit(unitName = "TruePrice_PersistenceUnit")

    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;
        
    
//    
//    public CategoryJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }

//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Category category) {

        try {
            entManager.persist(category);
            log.info("The entity was successfully saved !");
        } catch (Exception e) {
            log.error("The entity could not be saved.");
        }
    }

    public void edit(Category category) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            category = entManager.merge(category);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = category.getCtgId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = entManager.getReference(Category.class, id);
                category.getCtgId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
//            em.remove(category);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Category.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("Category.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } catch (Exception e) {
            log.error("Error inside Category Controller to get List<>, because > " + e.getMessage());            
        } finally {
//            em.close();
        }
        return null;        
    }

    public Category findCategory(Integer id) {
//        EntityManager em = getEntityManager();
        try {
//            return em.find(Category.class, id);
            return entManager.find(Category.class, id);
        } finally {
//            em.close();
        }
    }

    public int getCategoryCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<Category> rt = cq.from(Category.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
   
            Query q = entManager.createNamedQuery("Category.findAll");
            return q.getResultList().size();
                     
            
        } catch (Exception e) {
            log.error("Error inside Category Controller to get count, because > " + e.getMessage());
        } finally {
//            em.close();
        }
        return 0;
    }
    
}
