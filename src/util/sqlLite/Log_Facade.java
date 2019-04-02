/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author rubens
 */
public class Log_Facade {

    static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("MicroRTSPU2");
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
    
    public static void shrinkRewardTable(){
        List<LogUCB> sh = jpa.findLogUCBEntities();
        //id_rule, score
        HashMap<Integer,Integer> map = new HashMap<>();
        for (LogUCB logUCB : sh) {
            if(map.containsKey(logUCB.getIdRule().getIdRule())){
                //get the object and sum the score
                int score = map.get(logUCB.getIdRule().getIdRule());
                map.put(logUCB.getIdRule().getIdRule(), (score+logUCB.getReward()));
            }else{
                map.put(logUCB.getIdRule().getIdRule(), logUCB.getReward());
            }
        }
        
        clearRewardTable();
        
        for (Integer key : map.keySet()) {
            createNewReward(key, map.get(key));
        }
    }

}
