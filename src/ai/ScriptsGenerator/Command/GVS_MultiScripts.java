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
        String pathLogsUsedCommands = System.getProperty("user.dir").concat("/LogsUsedCommands/");
        GVS_RunBattle run = new GVS_RunBattle(pathTableScripts,pathLogsUsedCommands);
        
        String tupleAi1 = "0;";
        String tupleAi2 = "10554;5878;";
        int iMap = 0;
        run.run(tupleAi1, tupleAi2, iMap);
    }

}
