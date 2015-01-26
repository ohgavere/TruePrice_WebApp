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
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.controllers.exceptions.PreexistingEntityException;
import org.dmb.trueprice.controllers.exceptions.RollbackFailureException;
import org.dmb.trueprice.entities.ProduitResult;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
@Singleton
public class ProduitResultJpaController implements Serializable {

//    public ProduitResultJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    = InitContextListener.getLogger( ProduitResultJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;    
    
    public void create(ProduitResult produitResult) throws PreexistingEntityException, RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();
            entManager.persist(produitResult);
//            utx.commit();
        } catch (Exception ex) {
//            try {
//                utx.rollback();
//            } catch (Exception re) {
//                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
//            }
            if (findProduitResult(produitResult.getPdtResultId()) != null) {
                throw new PreexistingEntityException("ProduitResult " + produitResult + " already exists.", ex);
            }
            throw ex;
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public void edit(ProduitResult produitResult) throws NonexistentEntityException, RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();
            produitResult = entManager.merge(produitResult);
//            utx.commit();
        } catch (Exception ex) {
//            try {
//                utx.rollback();
//            } catch (Exception re) {
//                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
//            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produitResult.getPdtResultId();
                if (findProduitResult(id) == null) {
                    throw new NonexistentEntityException("The produitResult with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();
            ProduitResult produitResult;
            try {
                produitResult = entManager.getReference(ProduitResult.class, id);
                produitResult.getPdtResultId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produitResult with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(produitResult);
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

    public List<ProduitResult> findProduitResultEntities() {
        return findProduitResultEntities(true, -1, -1);
    }

    public List<ProduitResult> findProduitResultEntities(int maxResults, int firstResult) {
        return findProduitResultEntities(false, maxResults, firstResult);
    }

    private List<ProduitResult> findProduitResultEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(ProduitResult.class));
//            Query q = entManager.createQuery(cq);
            
            Query q = entManager.createNamedQuery("ProduitResult.findAll");
            
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            entManager.close();
        }
    }

    public ProduitResult findProduitResult(Integer id) {
//        EntityManager entManager = getEntityManager();
        try {
            return entManager.find(ProduitResult.class, id);
        } finally {
//            entManager.close();
        }
    }

    public int getProduitResultCount() {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<ProduitResult> rt = cq.from(ProduitResult.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
            
            Query q = entManager.createNamedQuery("ProduitResult.findAll");            
            return (q.getResultList()).size();
            
        } finally {
//            entManager.close();
        }
    }
    
}
