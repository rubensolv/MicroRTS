/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import util.sqlLite.exceptions.IllegalOrphanException;
import util.sqlLite.exceptions.NonexistentEntityException;
import util.sqlLite.exceptions.PreexistingEntityException;

/**
 *
 * @author rubens
 */
public class UcbJpaController implements Serializable {

    public UcbJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ucb ucb) throws PreexistingEntityException, Exception {
        if (ucb.getLogUCBList() == null) {
            ucb.setLogUCBList(new ArrayList<LogUCB>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<LogUCB> attachedLogUCBList = new ArrayList<LogUCB>();
            for (LogUCB logUCBListLogUCBToAttach : ucb.getLogUCBList()) {
                logUCBListLogUCBToAttach = em.getReference(logUCBListLogUCBToAttach.getClass(), logUCBListLogUCBToAttach.getId());
                attachedLogUCBList.add(logUCBListLogUCBToAttach);
            }
            ucb.setLogUCBList(attachedLogUCBList);
            em.persist(ucb);
            for (LogUCB logUCBListLogUCB : ucb.getLogUCBList()) {
                Ucb oldIdRuleOfLogUCBListLogUCB = logUCBListLogUCB.getIdRule();
                logUCBListLogUCB.setIdRule(ucb);
                logUCBListLogUCB = em.merge(logUCBListLogUCB);
                if (oldIdRuleOfLogUCBListLogUCB != null) {
                    oldIdRuleOfLogUCBListLogUCB.getLogUCBList().remove(logUCBListLogUCB);
                    oldIdRuleOfLogUCBListLogUCB = em.merge(oldIdRuleOfLogUCBListLogUCB);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUcb(ucb.getIdRule()) != null) {
                throw new PreexistingEntityException("Ucb " + ucb + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ucb ucb) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ucb persistentUcb = em.find(Ucb.class, ucb.getIdRule());
            List<LogUCB> logUCBListOld = persistentUcb.getLogUCBList();
            List<LogUCB> logUCBListNew = ucb.getLogUCBList();
            List<String> illegalOrphanMessages = null;
            for (LogUCB logUCBListOldLogUCB : logUCBListOld) {
                if (!logUCBListNew.contains(logUCBListOldLogUCB)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain LogUCB " + logUCBListOldLogUCB + " since its idRule field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<LogUCB> attachedLogUCBListNew = new ArrayList<LogUCB>();
            for (LogUCB logUCBListNewLogUCBToAttach : logUCBListNew) {
                logUCBListNewLogUCBToAttach = em.getReference(logUCBListNewLogUCBToAttach.getClass(), logUCBListNewLogUCBToAttach.getId());
                attachedLogUCBListNew.add(logUCBListNewLogUCBToAttach);
            }
            logUCBListNew = attachedLogUCBListNew;
            ucb.setLogUCBList(logUCBListNew);
            ucb = em.merge(ucb);
            for (LogUCB logUCBListNewLogUCB : logUCBListNew) {
                if (!logUCBListOld.contains(logUCBListNewLogUCB)) {
                    Ucb oldIdRuleOfLogUCBListNewLogUCB = logUCBListNewLogUCB.getIdRule();
                    logUCBListNewLogUCB.setIdRule(ucb);
                    logUCBListNewLogUCB = em.merge(logUCBListNewLogUCB);
                    if (oldIdRuleOfLogUCBListNewLogUCB != null && !oldIdRuleOfLogUCBListNewLogUCB.equals(ucb)) {
                        oldIdRuleOfLogUCBListNewLogUCB.getLogUCBList().remove(logUCBListNewLogUCB);
                        oldIdRuleOfLogUCBListNewLogUCB = em.merge(oldIdRuleOfLogUCBListNewLogUCB);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ucb.getIdRule();
                if (findUcb(id) == null) {
                    throw new NonexistentEntityException("The ucb with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ucb ucb;
            try {
                ucb = em.getReference(Ucb.class, id);
                ucb.getIdRule();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ucb with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<LogUCB> logUCBListOrphanCheck = ucb.getLogUCBList();
            for (LogUCB logUCBListOrphanCheckLogUCB : logUCBListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ucb (" + ucb + ") cannot be destroyed since the LogUCB " + logUCBListOrphanCheckLogUCB + " in its logUCBList field has a non-nullable idRule field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ucb);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ucb> findUcbEntities() {
        return findUcbEntities(true, -1, -1);
    }

    public List<Ucb> findUcbEntities(int maxResults, int firstResult) {
        return findUcbEntities(false, maxResults, firstResult);
    }

    private List<Ucb> findUcbEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ucb.class));
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

    public Ucb findUcb(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ucb.class, id);
        } finally {
            em.close();
        }
    }

    public int getUcbCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ucb> rt = cq.from(Ucb.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
