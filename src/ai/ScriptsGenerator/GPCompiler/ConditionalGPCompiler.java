/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.BasicConditional.IConditional;
import ai.ScriptsGenerator.BasicConditional.SimpleConditional;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.DistanceParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class ConditionalGPCompiler extends AbstractCompiler{
    
    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public IConditional getConditionalByCode(String code){
        
        IConditional conditional = buildConditional(code);
        
        return conditional;
    }

    private IConditional buildConditional(String code) {
        if (code.contains("HaveEnemiesinUnitsRange")) {
            return conditionalHaveEnemiesinUnitsRange(code);
        }
        if (code.contains("HaveQtdEnemiesbyType")) {
            return conditionalHaveQtdEnemiesbyType(code);
        }
        if (code.contains("HaveQtdUnitsAttacking")) {
            return conditionalHaveQtdUnitsAttacking(code);
        }
        if (code.contains("HaveQtdUnitsbyType")) {
            return conditionalHaveQtdUnitsbyType(code);
        }
        if (code.contains("HaveQtdUnitsHarversting")) {
            return conditionalHaveQtdUnitsHarversting(code);
        }
        if (code.contains("HaveUnitsinEnemyRange")) {
            return conditionalHaveUnitsinEnemyRange(code);
        }
        if (code.contains("HaveUnitsToDistantToEnemy")) {
            return conditionalHaveUnitsToDistantToEnemy(code);
        }
        
        return null;
    }

    private IConditional conditionalHaveEnemiesinUnitsRange(String code) {
        code = code.replace("HaveEnemiesinUnitsRange(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        return new SimpleConditional("HaveEnemiesinUnitsRange", 
                new ArrayList(Arrays.asList(getTypeUnitByString(params[0]))));
    }

    private IConditional conditionalHaveQtdEnemiesbyType(String code) {
        code = code.replace("HaveQtdEnemiesbyType(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        return new SimpleConditional("HaveQtdEnemiesbyType", 
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[1])),
                                            getTypeUnitByString(params[0]))));
    }

    private IConditional conditionalHaveQtdUnitsAttacking(String code) {
        code = code.replace("HaveQtdUnitsAttacking(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        return new SimpleConditional("HaveQtdUnitsAttacking", 
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[1])),
                                            getTypeUnitByString(params[0]))));
    }

    private IConditional conditionalHaveQtdUnitsbyType(String code) {
        code = code.replace("HaveQtdUnitsbyType(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        return new SimpleConditional("HaveQtdUnitsbyType", 
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[1])),
                        getTypeUnitByString(params[0]))));
    }

    private IConditional conditionalHaveQtdUnitsHarversting(String code) {
        code = code.replace("HaveQtdUnitsHarversting(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        return new SimpleConditional("HaveQtdUnitsHarversting", 
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[0])))));
    }

    private IConditional conditionalHaveUnitsinEnemyRange(String code) {
        code = code.replace("HaveUnitsinEnemyRange(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        return new SimpleConditional("HaveUnitsinEnemyRange", 
                new ArrayList(Arrays.asList(getTypeUnitByString(params[0]))));
    }

    private IConditional conditionalHaveUnitsToDistantToEnemy(String code) {
        code = code.replace("HaveUnitsinEnemyRange(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        return new SimpleConditional("HaveUnitsToDistantToEnemy", 
                                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]),
                                    new DistanceParam(Integer.decode(params[1])))));
    }

    
    
}
