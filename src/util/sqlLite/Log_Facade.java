/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import static util.sqlLite.UCB_Facade.jpa;

/**
 *
 * @author rubens
 */
public class Log_Facade {

    static EntityManagerFactory factory = Persistence.createEntityManagerFactory("MicroRTSPU2");
    static LogUCBJpaController jpa = new LogUCBJpaController(factory);

    public static void createNewReward(int idRule, int rewardValue){
        LogUCB log = new LogUCB();
        log.setIdRule(UCB_Facade.findRuleById(idRule));
        log.setReward(rewardValue);
        jpa.create(log);
    }

    public static int getTotalNumberOfRewards() {
        return jpa.getLogUCBCount();
    }
    
    
    public static void clearRewardTable(){
        EntityManager  em = jpa.getEntityManager();
        Query query = em.createQuery("DELETE FROM LogUCB");
        em.getTransaction().begin();
        int updateCount = query.executeUpdate();
        em.getTransaction().commit();
    }

}
