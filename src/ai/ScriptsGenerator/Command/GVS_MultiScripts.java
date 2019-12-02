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
        String pathLogsUsedCommands = System.getProperty("user.dir").concat("/commandsUsed/");
        GVS_RunBattle run = new GVS_RunBattle(pathTableScripts,pathLogsUsedCommands);
        
        String tupleAi1 = "0;";
        String tupleAi2 = "0;";
        //1128;1526;1785;1602;2037;
        //2378;1192;1995;1700;1637;1164;2578
        
        //2963;2964;2965;2966;2967;
        int iMap = 0;
        run.run(tupleAi1, tupleAi2, iMap);
    }

}
