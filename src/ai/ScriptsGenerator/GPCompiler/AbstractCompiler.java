/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.Command.Enumerators.EnumPlayerTarget;
import ai.ScriptsGenerator.Command.Enumerators.EnumPositionType;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.ClosestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.FarthestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.LessHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.MostHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.PriorityPositionParam;
import ai.ScriptsGenerator.ParametersConcrete.RandomEnemy;
import ai.ScriptsGenerator.ParametersConcrete.StrongestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.ParametersConcrete.WeakestEnemy;
import ai.ScriptsGenerator.TableGenerator.FunctionsforGrammar;
import java.util.List;

/**
 *
 * @author rubens
 */
public abstract class AbstractCompiler implements ICompiler {
    
    protected final FunctionsforGrammar fGrammar = new FunctionsforGrammar();    

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
            if (fragment.contains(basicFunction.getNameFunction())) {
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
    
    protected IParameters getTypeUnitByString(String j) {
        switch (j) {
            case "Worker":
                return TypeConcrete.getTypeWorker();
            case "Light":
                return TypeConcrete.getTypeLight();
            case "Ranged":
                return TypeConcrete.getTypeRanged();
            case "Heavy":
                return TypeConcrete.getTypeHeavy();
            default:
                return TypeConcrete.getTypeUnits();

        }
    }

    protected IParameters getBehaviorByName(String i) {
        switch (i) {
            case "closest":
                return new ClosestEnemy();
            case "farthest":
                return new FarthestEnemy();
            case "lessHealthy":
                return new LessHealthyEnemy();
            case "mostHealthy":
                return new MostHealthyEnemy();
            case "strongest":
                return new StrongestEnemy();
            case "weakest":
                return new WeakestEnemy();
            default:
                return new RandomEnemy();

        }
    }
    
    protected IParameters getPriorityPositionByName(String i) {
        PriorityPositionParam p = new PriorityPositionParam();
        switch (i) {
            case "Left":
                p.addPosition(EnumPositionType.Left);
                return p;
            case "Right":
                p.addPosition(EnumPositionType.Right);
                return p;
            case "Up":
                p.addPosition(EnumPositionType.Up);
                return p;            
            default:
                p.addPosition(EnumPositionType.Down);
                return p;

        }
    }
       

    protected EnumPlayerTarget getPlayerTargetByNumber(String p) {
        if (p.equals("Ally")) {
            return EnumPlayerTarget.Ally;
        }
        return EnumPlayerTarget.Enemy;
    }

    protected IParameters getTypeConstructByName(String param) {
        if (param.equals("Base")) {
            return TypeConcrete.getTypeBase(); //add unit construct type
        } else if (param.equals("Barrack")) {
            return TypeConcrete.getTypeBarracks(); //add unit construct type
        } else {
            return TypeConcrete.getTypeConstruction();
        }
    }
    
    protected int getPositionParentClose(int initialPosition, String[] fragments) {
        int contOpen = 0, contClosed = 0;

        for (int i = initialPosition; i < fragments.length; i++) {
            String fragment = fragments[i];
            contOpen += countCaracter(fragment, "(");
            contClosed += countCaracter(fragment, ")");
            if (contOpen == contClosed) {
                return i;
            }
        }

        return fragments.length;
    }
    
    protected int getLastPositionForFor(int initialPosition, String[] fragments) {
        //first get the name for(u)
        if (isForInitialClause(fragments[initialPosition])) {
            initialPosition++;
        }
        //second, we get the full () to complet the for. 
        return getPositionParentClose(initialPosition, fragments);
    }
    
    private boolean isForInitialClause(String fragment) {
        if (fragment.contains("for(u)")) {
            return true;
        }
        return false;
    }
}
