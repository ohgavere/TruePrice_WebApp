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
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.entities.Liste;
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
@Singleton
public class ListesJpaController implements Serializable {
    
private static final Logger log 
= InitContextListener.getLogger( ListesJpaController.class) ;    
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;    

//    public ListesJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Liste listes) {
//        EntityManager entManager = null;
        try {
//            entManager = getEntityManager();
//            entManager.getTransaction().begin();
            entManager.persist(listes);
//            entManager.getTransaction().commit();
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public void edit(Liste listes) throws NonexistentEntityException, Exception {
//        EntityManager entManager = null;
        try {
//            entManager = getEntityManager();
//            entManager.getTransaction().begin();
            listes = entManager.merge(listes);
//            entManager.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = listes.getLstId();
                if (findListes(id) == null) {
                    throw new NonexistentEntityException("The listes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager entManager = null;
        try {
//            entManager = getEntityManager();
//            entManager.getTransaction().begin();
            Liste listes;
            try {
                listes = entManager.getReference(Liste.class, id);
                listes.getLstId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The listes with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(listes);
//            entManager.getTransaction().commit();
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public List<Liste> findListesEntities() {
        return findListesEntities(true, -1, -1);
    }

    public List<Liste> findListesEntities(int maxResults, int firstResult) {
        return findListesEntities(false, maxResults, firstResult);
    }

    private List<Liste> findListesEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Liste.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("Liste.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            entManager.close();
        }
    }

    public Liste findListes(Integer id) {
//        EntityManager entManager = getEntityManager();
        try {
            return entManager.find(Liste.class, id);
        } finally {
//            entManager.close();
        }
    }

    public int getListesCount() {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<Liste> rt = cq.from(Liste.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            
            Query q = entManager.createNamedQuery("Listes.findAll");
            return q.getResultList().size();
                          
        } finally {
//            entManager.close();
        }
    }
    
      public List<Liste> findByUser( long userID )  /* throws DAOException */ {
        List<Liste> listes = null;
        Query requete = entManager.createNamedQuery("Liste.findByListesUser");
        requete.setParameter("lstUser",userID);
        try {
            listes =  requete.getResultList();
        } catch ( NoResultException e ) {
            log.debug("User ID [" + userID + "] not found.");
        }
//         catch ( Exception e ) {
//            e.printStackTrace();
//            throw new DAOException( e );
//        }
        return listes;
    }    
    
}
