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
import org.dmb.trueprice.controllers.exceptions.RollbackFailureException;
import org.dmb.trueprice.entities.ProduitInfo;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
@Singleton
public class ProduitInfoJpaController implements Serializable {

//    public ProduitInfoJpaController(UserTransaction utx, EntityManagerFactory emf) {
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
    = InitContextListener.getLogger( ProduitInfoJpaController.class) ;
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;    
    
    
    public void create(ProduitInfo produitInfo) throws RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();
            entManager.persist(produitInfo);
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

    public void edit(ProduitInfo produitInfo) throws NonexistentEntityException, RollbackFailureException, Exception {
//        EntityManager entManager = null;
        try {
//            utx.begin();
//            entManager = getEntityManager();
            produitInfo = entManager.merge(produitInfo);
//            utx.commit();
        } catch (Exception ex) {
//            try {
//                utx.rollback();
//            } catch (Exception re) {
//                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
//            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = produitInfo.getPdtInfoId();
                if (findProduitInfo(id) == null) {
                    throw new NonexistentEntityException("The produitInfo with id " + id + " no longer exists.");
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
            ProduitInfo produitInfo;
            try {
                produitInfo = entManager.getReference(ProduitInfo.class, id);
                produitInfo.getPdtInfoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produitInfo with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(produitInfo);
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

    public List<ProduitInfo> findProduitInfoEntities() {
        return findProduitInfoEntities(true, -1, -1);
    }

    public List<ProduitInfo> findProduitInfoEntities(int maxResults, int firstResult) {
        return findProduitInfoEntities(false, maxResults, firstResult);
    }

    private List<ProduitInfo> findProduitInfoEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(ProduitInfo.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("ProduitInfo.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            entManager.close();
        }
    }

    public ProduitInfo findProduitInfo(Long id) {
//        EntityManager entManager = getEntityManager();
        try {
            return entManager.find(ProduitInfo.class, id);
        } finally {
//            entManager.close();
        }
    }

    public int getProduitInfoCount() {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<ProduitInfo> rt = cq.from(ProduitInfo.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
          
            Query q = entManager.createNamedQuery("ProduitInfo.findAll");
            
            return (q.getResultList()).size();
        } finally {
//            entManager.close();
        }
    }
    
}
