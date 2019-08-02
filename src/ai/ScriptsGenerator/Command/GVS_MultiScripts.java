/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.Command;

/**
 *
 * @author santi
 */
public class GVS_MultiScripts {

    public static void main(String args[]) throws Exception {
        String pathTableScripts = System.getProperty("user.dir").concat("/Table/");
        GVS_RunBattle run = new GVS_RunBattle(pathTableScripts);
        
        String tupleAi1 = "2;";
        String tupleAi2 = "2;";
        int iMap = 0;
        run.run(tupleAi1, tupleAi2, iMap);
    }

}
