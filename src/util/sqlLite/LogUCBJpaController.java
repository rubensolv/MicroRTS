/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import util.sqlLite.exceptions.NonexistentEntityException;

/**
 *
 * @author rubens
 */
public class LogUCBJpaController implements Serializable {

    public LogUCBJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LogUCB logUCB) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ucb idRule = logUCB.getIdRule();
            if (idRule != null) {
                idRule = em.getReference(idRule.getClass(), idRule.getIdRule());
                logUCB.setIdRule(idRule);
            }
            em.persist(logUCB);
            if (idRule != null) {
                idRule.getLogUCBList().add(logUCB);
                idRule = em.merge(idRule);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LogUCB logUCB) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogUCB persistentLogUCB = em.find(LogUCB.class, logUCB.getId());
            Ucb idRuleOld = persistentLogUCB.getIdRule();
            Ucb idRuleNew = logUCB.getIdRule();
            if (idRuleNew != null) {
                idRuleNew = em.getReference(idRuleNew.getClass(), idRuleNew.getIdRule());
                logUCB.setIdRule(idRuleNew);
            }
            logUCB = em.merge(logUCB);
            if (idRuleOld != null && !idRuleOld.equals(idRuleNew)) {
                idRuleOld.getLogUCBList().remove(logUCB);
                idRuleOld = em.merge(idRuleOld);
            }
            if (idRuleNew != null && !idRuleNew.equals(idRuleOld)) {
                idRuleNew.getLogUCBList().add(logUCB);
                idRuleNew = em.merge(idRuleNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = logUCB.getId();
                if (findLogUCB(id) == null) {
                    throw new NonexistentEntityException("The logUCB with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LogUCB logUCB;
            try {
                logUCB = em.getReference(LogUCB.class, id);
                logUCB.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The logUCB with id " + id + " no longer exists.", enfe);
            }
            Ucb idRule = logUCB.getIdRule();
            if (idRule != null) {
                idRule.getLogUCBList().remove(logUCB);
                idRule = em.merge(idRule);
            }
            em.remove(logUCB);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LogUCB> findLogUCBEntities() {
        return findLogUCBEntities(true, -1, -1);
    }

    public List<LogUCB> findLogUCBEntities(int maxResults, int firstResult) {
        return findLogUCBEntities(false, maxResults, firstResult);
    }

    private List<LogUCB> findLogUCBEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LogUCB.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public LogUCB findLogUCB(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LogUCB.class, id);
        } finally {
            em.close();
        }
    }

    public int getLogUCBCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LogUCB> rt = cq.from(LogUCB.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
