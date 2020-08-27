/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.BasicConditional.IConditional;
import ai.ScriptsGenerator.BasicConditional.SimpleConditional;
import ai.ScriptsGenerator.BasicConditional.functions.HaveUnitsStrongest;
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
public class ConditionalGPCompiler extends AbstractCompiler {

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SimpleConditional getConditionalByCode(String code) {

        SimpleConditional conditional = buildConditional(code);

        return conditional;
    }

    private SimpleConditional buildConditional(String code) {
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
        if (code.contains("HaveUnitsStrongest")) {
            return conditionalHaveUnitsStrongest(code);
        }
        if (code.contains("HaveEnemiesStrongest")) {
            return conditionalHaveEnemiesStrongest(code);
        }
        if (code.contains("IsPlayerInPosition")) {
            return conditionalIsPlayerInPosition(code);
        }

        return null;
    }

    private SimpleConditional conditionalHaveEnemiesinUnitsRange(String code) {
        code = code.replace("HaveEnemiesinUnitsRange(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        if (params.length == 1) {
            return new SimpleConditional("HaveEnemiesinUnitsRange",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]))));
        } else {
            return new SimpleConditional("HaveEnemiesinUnitsRange",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]),
                            params[1])));
        }

    }

    private SimpleConditional conditionalHaveQtdEnemiesbyType(String code) {
        code = code.replace("HaveQtdEnemiesbyType(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        return new SimpleConditional("HaveQtdEnemiesbyType",
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[1])),
                        getTypeUnitByString(params[0]))));
    }

    private SimpleConditional conditionalHaveQtdUnitsAttacking(String code) {
        code = code.replace("HaveQtdUnitsAttacking(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        return new SimpleConditional("HaveQtdUnitsAttacking",
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[1])),
                        getTypeUnitByString(params[0]))));
    }

    private SimpleConditional conditionalHaveQtdUnitsbyType(String code) {
        code = code.replace("HaveQtdUnitsbyType(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        return new SimpleConditional("HaveQtdUnitsbyType",
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[1])),
                        getTypeUnitByString(params[0]))));
    }

    private SimpleConditional conditionalHaveQtdUnitsHarversting(String code) {
        code = code.replace("HaveQtdUnitsHarversting(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        return new SimpleConditional("HaveQtdUnitsHarversting",
                new ArrayList(Arrays.asList(new QuantityParam(Integer.decode(params[0])))));
    }

    private SimpleConditional conditionalHaveUnitsinEnemyRange(String code) {
        code = code.replace("HaveUnitsinEnemyRange(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        if (params.length == 1) {
            return new SimpleConditional("HaveUnitsinEnemyRange",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]))));
        } else {
            return new SimpleConditional("HaveUnitsinEnemyRange",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]),
                            params[1])));
        }

    }

    private SimpleConditional conditionalHaveUnitsToDistantToEnemy(String code) {
        code = code.replace("HaveUnitsToDistantToEnemy(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");

        if (params.length == 2) {
            return new SimpleConditional("HaveUnitsToDistantToEnemy",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]),
                            new DistanceParam(Integer.decode(params[1])))));
        } else {
            return new SimpleConditional("HaveUnitsToDistantToEnemy",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]),
                            new DistanceParam(Integer.decode(params[1])),
                            params[2])));
        }
    }

    private SimpleConditional conditionalHaveUnitsStrongest(String code) {
        code = code.replace("HaveUnitsStrongest(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        if (params.length == 1) {
            return new SimpleConditional("HaveUnitsStrongest",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]))));
        } else {
            return new SimpleConditional("HaveUnitsStrongest",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]),
                            params[1])));
        }
    }

    private SimpleConditional conditionalHaveEnemiesStrongest(String code) {
        code = code.replace("HaveEnemiesStrongest(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        if (params.length == 1) {
            return new SimpleConditional("HaveEnemiesStrongest",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]))));
        } else {
            return new SimpleConditional("HaveEnemiesStrongest",
                    new ArrayList(Arrays.asList(getTypeUnitByString(params[0]),
                    params[1])));
        }
    }
    
    private SimpleConditional conditionalIsPlayerInPosition(String code) {
        code = code.replace("IsPlayerInPosition(", "");
        code = code.replace(")", "").replace(",", " ");
        String[] params = code.split(" ");
        
        if (params.length == 1) {
            return new SimpleConditional("IsPlayerInPosition",
                    new ArrayList(Arrays.asList(getPriorityPositionByName(params[0]))));
        } else {
            return new SimpleConditional("IsPlayerInPosition",
                    new ArrayList(Arrays.asList(getPriorityPositionByName(params[0]),
                    params[1])));
        }
    }
    
    
            

}
