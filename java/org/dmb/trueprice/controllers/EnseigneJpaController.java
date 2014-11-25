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
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.apache.log4j.Logger;
import org.dmb.trueprice.controllers.exceptions.NonexistentEntityException;
import org.dmb.trueprice.entities.Enseigne;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
@Singleton
public class EnseigneJpaController implements Serializable {
    
    private static final Logger log 
            = InitContextListener.getLogger( EnseigneJpaController.class) ;    
    
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;

//    public EnseigneJpaController(EntityManagerFactory emf) {
//        this.emf = emf;
//    }
//    private EntityManagerFactory emf = null;
//
//    public EntityManager getEntityManager() {
//        return emf.createEntityManager();
//    }

    public void create(Enseigne enseigne) {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            entManager.persist(enseigne);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public void edit(Enseigne enseigne) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            em.getTransaction().begin();
            enseigne = entManager.merge(enseigne);
//            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = enseigne.getEsgnId();
                if (findEnseigne(id) == null) {
                    throw new NonexistentEntityException("The enseigne with id " + id + " no longer exists.");
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
            Enseigne enseigne;
            try {
                enseigne = entManager.getReference(Enseigne.class, id);
                enseigne.getEsgnId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The enseigne with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(enseigne);
//            em.getTransaction().commit();
        } finally {
//            if (em != null) {
//                em.close();
//            }
        }
    }

    public List<Enseigne> findEnseigneEntities() {
        return findEnseigneEntities(true, -1, -1);
    }

    public List<Enseigne> findEnseigneEntities(int maxResults, int firstResult) {
        return findEnseigneEntities(false, maxResults, firstResult);
    }

    private List<Enseigne> findEnseigneEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Enseigne.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("Enseigne.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            entManager.close();
        }
    }

    public Enseigne findEnseigne(Long id) {
//        EntityManager em = getEntityManager();
        try {
            return entManager.find(Enseigne.class, id);
        } finally {
//            em.close();
        }
    }

    public int getEnseigneCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<Enseigne> rt = cq.from(Enseigne.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            Query q = entManager.createNamedQuery("Enseigne.findAll");
            return q.getResultList().size();
                      
        } finally {
//            em.close();
        }
    }
    
    public Enseigne findByLabel ( String label )  /* throws DAOException */ {
        Enseigne esg = null;
        Query requete = entManager.createNamedQuery("Enseigne.findByEsgnLabel");
        requete.setParameter("esgnLabel",label);
        try {
            esg = (Enseigne) requete.getSingleResult();
        } catch ( NoResultException e ) {
            log.debug("Pseudo [" + label + "] not found.");
        }
//         catch ( Exception e ) {
//            e.printStackTrace();
//            throw new DAOException( e );
//        }
        return esg;
    }
    
}
