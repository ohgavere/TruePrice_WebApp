/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tentManagerplate file, choose Tools | TentManagerplates
 * and open the tentManagerplate in the editor.
 */

package org.dmb.trueprice.controllers;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.entities.QttDetail;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guitch
 */
@Singleton
public class QttDetailJpaController implements Serializable {
    
    private static final Logger log 
            = InitContextListener.getLogger( EnseigneJpaController.class) ;    
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;

    public void create(QttDetail qttDetail) {
        try {
            entManager.persist(qttDetail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(QttDetail qttDetail) throws NonexistentEntityException, Exception {
        try {
            qttDetail = entManager.merge(qttDetail);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = qttDetail.getQttId();
                if (findQttDetail(id) == null) {
                    throw new NonexistentEntityException("The qttDetail with id " + id + " no longer exists.");
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
//        EntityManager entManager = null;
        try {
//            entManager = getEntityManager();
//            entManager.getTransaction().begin();
            QttDetail qttDetail;
            try {
                qttDetail = entManager.getReference(QttDetail.class, id);
                qttDetail.getQttId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The qttDetail with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(qttDetail);
//            entManager.getTransaction().commit();
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public List<QttDetail> findQttDetailEntities() {
        return findQttDetailEntities(true, -1, -1);
    }

    public List<QttDetail> findQttDetailEntities(int maxResults, int firstResult) {
        return findQttDetailEntities(false, maxResults, firstResult);
    }

    private List<QttDetail> findQttDetailEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(QttDetail.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("QttDetail.findAll");            
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            entManager.close();
        }
    }

    public QttDetail findQttDetail(Integer id) {
//        EntityManager entManager = getEntityManager();
        try {
            return entManager.find(QttDetail.class, id);
        } finally {
//            entManager.close();
        }
    }

    public int getQttDetailCount() {
//        EntityManager entManager = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<QttDetail> rt = cq.from(QttDetail.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
                        
            Query q = entManager.createNamedQuery("QttDetail.findAll");
            return q.getResultList().size();
                              
        } finally {
//            entManager.close();
        }
    }
    
}
