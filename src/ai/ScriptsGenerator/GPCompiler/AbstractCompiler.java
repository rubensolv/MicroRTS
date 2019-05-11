/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.TableGenerator.FunctionsforGrammar;
import java.util.List;

/**
 *
 * @author rubens
 */
public abstract class AbstractCompiler implements ICompiler{
    
    
    
    private final FunctionsforGrammar fGrammar = new FunctionsforGrammar();
    
    
    public static String generateString(int initialPos, int finalPos, String[] fragments) {
        String fullString = "";
        if (finalPos > (fragments.length - 1)) {
            finalPos = (fragments.length - 1);
        }
        for (int i = initialPos; i <= finalPos; i++) {
            fullString += fragments[i] + " ";
        }
        return fullString.trim();
    }

    protected boolean isBasicCommand(String fragment) {
        List<FunctionsforGrammar> basicFunctions = fGrammar.getBasicFunctionsForGrammar();
        for (FunctionsforGrammar basicFunction : basicFunctions) {
            if(fragment.contains(basicFunction.getNameFunction())){
                return true;
            }
        }
        return false;
    }
    
    
    protected int countCaracter(String fragment, String toFind) {
        int total = 0;
        for (int i = 0; i < fragment.length(); i++) {
            char ch = fragment.charAt(i);
            String x1 = String.valueOf(ch);
            if (x1.equalsIgnoreCase(toFind)) {
                total = total + 1;
            }
        }
        return total;
    }
}
