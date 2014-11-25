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
import org.dmb.trueprice.entities.Membre;
import org.dmb.trueprice.utils.internal.InitContextListener;

/**
 *
 * @author Guiitch
 */
@Singleton
public class MembreJpaController implements Serializable {

private static final Logger log 
= InitContextListener.getLogger( MembreJpaController.class) ;
    
    
//    public MembreJpaController(EntityManager em) {
//        this.entManager = em;
//    }
//    private EntityManagerFactory emf = null;
//    @PersistenceUnit(unitName = "TruePrice_PersistenceUnit")
    @PersistenceContext(unitName = "TruePrice_PersistenceUnit")
    private EntityManager       entManager;
    

    public void create(Membre membre) {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            entManager.getTransaction().begin();
            entManager.persist(membre);
//            entManager.getTransaction().commit();
        } finally {
//            if (entManager.getTransaction() != null) {
//                entManager.close();
//            }
        }
    }

    public void edit(Membre membre) throws NonexistentEntityException, Exception {
//        EntityManager em = null;
        try {
//            em = getEntityManager();
//            entManager.getTransaction().begin();
            membre = entManager.merge(membre);
//            entManager.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = membre.getMbId();
                if (findMembre(id) == null) {
                    throw new NonexistentEntityException("The membre with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
//            if (entManager != null) {
//                entManager.close();
//            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
//        EntityManager em = null;
//        try {
//            em = getEntityManager();
//            entManager.getTransaction().begin();
            Membre membre;
            try {
                membre = entManager.getReference(Membre.class, id);
                membre.getMbId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The membre with id " + id + " no longer exists.", enfe);
            }
            entManager.remove(membre);
//            entManager.getTransaction().commit();
       // } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    }

    public List<Membre> findMembreEntities() {
        return findMembreEntities(true, -1, -1);
    }

    public List<Membre> findMembreEntities(int maxResults, int firstResult) {
        return findMembreEntities(false, maxResults, firstResult);
    }

    private List<Membre> findMembreEntities(boolean all, int maxResults, int firstResult) {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq;
//            cq = entManager.getCriteriaBuilder().createQuery();
//            cq.select(cq.from(Membre.class));
//            Query q = entManager.createQuery(cq);
            Query q = entManager.createNamedQuery("Membre.findAll");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
//            em.close();
        }
    }

    public Membre findMembre(Long id) {
//        EntityManager em = getEntityManager();
//        try {
            return entManager.find(Membre.class, id);
//        } finally {
//            em.close();
//        }
    }

    public int getMembreCount() {
//        EntityManager em = getEntityManager();
        try {
//            CriteriaQuery cq = entManager.getCriteriaBuilder().createQuery();
//            Root<Membre> rt = cq.from(Membre.class);
//            cq.select(entManager.getCriteriaBuilder().count(rt));
//            Query q = entManager.createQuery(cq);
//            return ((Long) q.getSingleResult()).intValue();
            
            Query q = entManager.createNamedQuery("Membre.findAll");
            return q.getResultList().size();
            
        } finally {
//            em.close();
        }
    }
    
      public Membre findByMail( String email )  /* throws DAOException */ {
        Membre utilisateur = null;
        Query requete = entManager.createNamedQuery("Membre.findByMbMail");
        requete.setParameter("mbMail",email);
        try {
            utilisateur = (Membre) requete.getSingleResult();
        } catch ( NoResultException e ) {
            log.debug("Email [" + email + "] not found.");
        }
//         catch ( Exception e ) {
//            e.printStackTrace();
//            throw new DAOException( e );
//        }
        return utilisateur;
    }
    
      public Membre findByPseudo( String pseudo )  /* throws DAOException */ {
        Membre utilisateur = null;
        Query requete = entManager.createNamedQuery("Membre.findByMbPseudo");
        requete.setParameter("mbPseudo",pseudo);
        try {
            utilisateur = (Membre) requete.getSingleResult();
        } catch ( NoResultException e ) {
            log.debug("Pseudo [" + pseudo + "] not found.");
        }
//         catch ( Exception e ) {
//            e.printStackTrace();
//            throw new DAOException( e );
//        }
        return utilisateur;
    }
}
