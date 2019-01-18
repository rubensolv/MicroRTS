/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author rubens
 */
public class UCB_Facade {

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("MicroRTSPU2");
    static UcbJpaController jpa = new UcbJpaController(factory);

    /**
     * Create a new entity in the DB using the idRule as primary key
     *
     * @param idRule Identification of the rule
     * @return true if the entity was persisted correctly and false if not.
     */
    public static boolean createNewUCBRule(int idRule) {
        Ucb entity = new Ucb();
        entity.setIdRule(idRule);
        entity.setQtdUsed(0);
        try {
            jpa.create(entity);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(UCB_Facade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Persist the Ucb class in the DB
     *
     * @param entity - one UCB entity with data
     * @return true if the entity was persisted correctly and false if not.
     */
    public static boolean persistNewUCBRule(Ucb entity) {
        try {
            jpa.create(entity);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(UCB_Facade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * return one Ucb entity from the DB
     *
     * @param idRule Identification of the rule
     * @return the Entity UCB from the DB
     */
    public static Ucb findRuleById(int idRule) {
        return jpa.findUcb(idRule);
    }

    /**
     * Incremente the weight of the entity in 1
     *
     * @param idRule Identification of the rule
     * @return true if the entity was persisted correctly and false if not.
     */
    public static boolean incrementQtdUsed(int idRule) {
        try {
            Ucb upUcb = findRuleById(idRule);
            upUcb.setQtdUsed(upUcb.getQtdUsed() + 1);
            try {
                jpa.edit(upUcb);
                return true;
            } catch (Exception ex) {
                Logger.getLogger(UCB_Facade.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        } catch (Exception e) {
                Logger.getLogger(UCB_Facade.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    /**
     * Decrease the weight of the entity in 1
     *
     * @param idRule Identification of the rule
     * @return true if the entity was persisted correctly and false if not.
     */
    public static boolean decreaseQtdUsed(int idRule) {
        Ucb upUcb = findRuleById(idRule);
        upUcb.setQtdUsed(upUcb.getQtdUsed() - 1);
        try {
            jpa.edit(upUcb);
            return true;
        } catch (Exception ex) {
            Logger.getLogger(UCB_Facade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    /**
     * Get the number of entities in the Table.
     *
     * @return the number of entities in the Table.
     */
    public static int getNumberOfRules() {
        return jpa.getUcbCount();
    }

    /**
     * Get all entities in the DB.
     *
     * @return return a list with all entities from the DB.
     */
    public static List<Ucb> getAllRules() {
        return jpa.findUcbEntities();
    }

    /**
     * clear the quantity of hints used to calculate the average value.
     *
     * @return quantity of rows updated.
     */
    public static int clearQtdUsedFromAll() {
        EntityManager em = jpa.getEntityManager();
        Query query = em.createQuery("UPDATE Ucb SET qtdUsed = 0");
        em.getTransaction().begin();
        int updateCount = query.executeUpdate();
        em.getTransaction().commit();
        return updateCount;
    }

    public static int clearUCBTable() {
        EntityManager em = jpa.getEntityManager();
        Query query = em.createQuery("DELETE FROM Ucb");
        em.getTransaction().begin();
        int updateCount = query.executeUpdate();
        em.getTransaction().commit();
        return updateCount;

    }

    public static double getAverageValueFromRule(int idRule) {
        Ucb ucb = findRuleById(idRule);
        double sum = 0.0;
        for (LogUCB logUCB : ucb.getLogUCBList()) {
            sum += logUCB.getReward();
        }
        double total = ucb.getQtdUsed();

        return sum / total;
    }

}
