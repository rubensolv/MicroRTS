/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.sqlLite;

import ai.ScriptsGenerator.TableGenerator.TableCommandsGenerator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class TestsSqLite {

    public static void main(String args[]) {
        //getConnection();
        /*
        UCB_Facade.clearUCBTable();
        
        UCB_Facade.createNewUCBRule(1);
        UCB_Facade.createNewUCBRule(2);
        UCB_Facade.createNewUCBRule(3);
        UCB_Facade.createNewUCBRule(4);
        UCB_Facade.createNewUCBRule(5);
        
        System.out.println(UCB_Facade.findRuleById(3));

        UCB_Facade.incrementQtdUsed(3);
        UCB_Facade.incrementQtdUsed(3);
        System.out.println(UCB_Facade.findRuleById(3));
        UCB_Facade.decreaseQtdUsed(3);
        System.out.println(UCB_Facade.findRuleById(3));

        System.out.println(UCB_Facade.getNumberOfRules());

        System.out.println(UCB_Facade.getAllRules());
        UCB_Facade.clearQtdUsedFromAll();
        
        System.out.println(UCB_Facade.getAllRules());
        */
        
        //System.out.println(Log_Facade.getTotalNumberOfRewards());
        
        //UCB_Facade.incrementQtdUsed(1);
        //Log_Facade.createNewReward(1, 1);
        
        
        
        //UCB_Facade.incrementQtdUsed(1);
        //Log_Facade.createNewReward(1, 1);
        
        //System.out.println(Log_Facade.getTotalNumberOfRewards());
        
        //System.out.println("average to 1 "+ UCB_Facade.getAverageValueFromRule(1));
        
        //Log_Facade.clearRewardTable();
        //UnitTypeTable utt = new UnitTypeTable();
        //TableCommandsGenerator tbl = TableCommandsGenerator.getInstance(utt);
        //System.out.println(tbl.getNumberOfCommands());
        
        //UCB_Facade.clearUCBTable();
        
        UnitTypeTable utt = new UnitTypeTable();
        TableCommandsGenerator tbl = TableCommandsGenerator.getInstance(utt);
        for (int i = 0; i < tbl.getNumberOfCommands(); i++) {
            UCB_Facade.createNewUCBRule(i);
        }
        
        //System.out.println(UCB_Facade.findRuleById(0));
        //Log_Facade.shrinkRewardTable();
    }

    public static void update() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("MicroRTSPU");
        UcbJpaController jpa = new UcbJpaController(factory);
        System.out.println("Number of tupla " + jpa.getUcbCount());

        Ucb testUCB = jpa.findUcb(2);
        testUCB.setQtdUsed(testUCB.getQtdUsed() - 1);
        try {
            jpa.edit(testUCB);
        } catch (Exception ex) {
            Logger.getLogger(TestsSqLite.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println(testUCB);
    }

    public static void createNewEntity() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("MicroRTSPU");
        UcbJpaController jpa = new UcbJpaController(factory);
        System.out.println("Number of tupla " + jpa.getUcbCount());

        Ucb testUCB = new Ucb();
        testUCB.setIdRule(2);
        testUCB.setQtdUsed(0);

        try {
            jpa.create(testUCB);
        } catch (Exception ex) {
            Logger.getLogger(TestsSqLite.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Number of tupla " + jpa.getUcbCount());
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            //db path
            String url = "jdbc:sqlite:" + System.getProperty("user.dir").concat("/data/db/rank.db");
            System.out.println(url);
            //creat connection
            conn = DriverManager.getConnection(url);

            System.out.println("Connected!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return conn;
    }

}
