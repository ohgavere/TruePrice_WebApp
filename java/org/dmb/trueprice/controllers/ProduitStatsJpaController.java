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
import javax.transaction.UserTransaction;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.controllers.exceptions.RollbackFailureException;
import org.dmb.trueprice.entities.ProduitStats;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
@Singleton
public class ProduitStatsJpaController implements Serializable {

//    public ProduitStatsJpaController(UserTransaction utx, EntityManagerFactory emf) {
//        this.utx = utx;
//        this.emf = emf;
//    }
//    private UserTransaction utx = null;
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }
    
    private static final Logger log 
    = InitContextListener.getLogger( ProduitStatsJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;    
    

    public void create(ProduitStats produitStats) throws RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();
            entManager.persist(produitStats);
//            utx.commit();
//        } catch (Exception ex) {
//            try {
////                utx.rollback();
//            } catch (Exception re) {
//                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
//            }
//            throw ex;
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public void edit(ProduitStats produitStats) throws NonexistentEntityException, RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();
            produitStats = entManager.merge(produitStats);
//            utx.commit();
        } catch (Exception ex) {
//            try {
//                utx.rollback();
//            } catch (Exception re) {
//                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
//            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = produitStats.getPdtStatsId();
                if (findProduitStats(id) == null) {
                    throw new NonexistentEntityException("The produitStats with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException, RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();        
            ProduitStats produitStats;
            try {
                produitStats = entManager.getReference(ProduitStats.class, id);
                produitStats.getPdtStatsId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produitStats with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(produitStats);
//            utx.commit();
        } catch (Exception ex) {
//            try {
//                utx.rollback();
//            } catch (Exception re) {
//                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
//            }
            throw ex;
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public List<ProduitStats> findProduitStatsEntities() {
        return findProduitStatsEntities(true, -1, -1);
    }

    public List<ProduitStats> findProduitStatsEntities(int maxResults, int firstResult) {
        return findProduitStatsEntities(false, maxResults, firstResult);
    }

    private List<ProduitStats> findProduitStatsEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(ProduitStats.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("ProduitStats.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            entManager.close();
        }
    }

    public ProduitStats findProduitStats(Long id) {
//        EntityManager entManager = getEntityManager();
        try {
            return entManager.find(ProduitStats.class, id);
        } finally {
//            entManager.close();
        }
    }

    public int getProduitStatsCount() {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<ProduitStats> rt = cq.from(ProduitStats.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
            
            Query q = entManager.createNamedQuery("QttDetail.findAll");
            
            return (q.getResultList()).size();
            
        } finally {
            entManager.close();
        }
    }
    
}
