package ai.ScriptsGenerator.TableGenerator;

import java.util.ArrayList;
import java.util.List;

public class FunctionsforGrammar {

    String nameFunction;
    String className;
    List<Parameter> parameters;

    List<String> typeUnitDiscrete;
    List<String> typeStructureDiscrete;
    List<String> behaviourDiscrete;
    List<String> playerTargetDiscrete;
    List<String> priorityPositionDiscrete;

    List<FunctionsforGrammar> basicFunctionsForGrammar;
    List<FunctionsforGrammar> conditionalsForGrammar;

    public FunctionsforGrammar(String nameFunction, List<Parameter> parameters, String className) {
        this.nameFunction = nameFunction;
        this.parameters = parameters;
        this.className = className;
    }

    public FunctionsforGrammar() {
        initializateDiscreteSpecificParameters();
        createTableBasicFunctionsGrammar();
        createTableConditionalsGrammar();
    }

    public void initializateDiscreteSpecificParameters() {
        //Parameter TypeUnit
        typeUnitDiscrete = new ArrayList<>();
        typeUnitDiscrete.add("Worker");
        typeUnitDiscrete.add("Light");
        typeUnitDiscrete.add("Ranged");
        typeUnitDiscrete.add("Heavy");
        typeUnitDiscrete.add("All");

        //Parameter TypeStructure
        typeStructureDiscrete = new ArrayList<>();
        typeStructureDiscrete.add("Base");
        typeStructureDiscrete.add("Barrack");
        typeStructureDiscrete.add("All");

        //Parameter Behaviour
        behaviourDiscrete = new ArrayList<>();
        behaviourDiscrete.add("closest");
        behaviourDiscrete.add("farthest");
        behaviourDiscrete.add("lessHealthy");
        behaviourDiscrete.add("mostHealthy");
        behaviourDiscrete.add("strongest");
        behaviourDiscrete.add("weakest");

        //Parameter PlayerTarget
        playerTargetDiscrete = new ArrayList<>();
        playerTargetDiscrete.add("Enemy");
        playerTargetDiscrete.add("Ally");

        //Parameter PriorityPosition
        priorityPositionDiscrete = new ArrayList<>();
        priorityPositionDiscrete.add("Up");
        priorityPositionDiscrete.add("Down");
        priorityPositionDiscrete.add("Right");
        priorityPositionDiscrete.add("Left");
        priorityPositionDiscrete.add("EnemyDir");

    }

    public void createTableBasicFunctionsGrammar() {
        basicFunctionsForGrammar = new ArrayList<>();

        //Function AttackBasic
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add(new Parameter("behaviour", null, null, behaviourDiscrete));
        //parameters.add(new Parameter("u", null, null, null));
        
        basicFunctionsForGrammar.add(new FunctionsforGrammar("attack", parameters, 
                "ai.ScriptsGenerator.Command.BasicAction.AttackBasic"));

        //Function BuildBasic
        parameters = new ArrayList<>();
        parameters.add(new Parameter("structureType", null, null, typeStructureDiscrete));
        parameters.add(new Parameter("Quantity", 1.0, 3.0, null));
        basicFunctionsForGrammar.add(new FunctionsforGrammar("build", parameters,
        "ai.ScriptsGenerator.Command.BasicAction.BuildBasic"));

        //Function HarvestBasic
        parameters = new ArrayList<>();
        parameters.add(new Parameter("Quantity", 1.0, 5.0, null));
        basicFunctionsForGrammar.add(new FunctionsforGrammar("harvest", parameters,
        "ai.ScriptsGenerator.Command.BasicAction.HarvestBasic"));

        //Function MoveToCoordinatesBasic
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add((new Parameter("x", 0.0, 7.0, null)));
        parameters.add((new Parameter("y", 0.0, 7.0, null)));
        //parameters.add(new Parameter("u", null, null, null));
        basicFunctionsForGrammar.add(new FunctionsforGrammar("moveToCoord", parameters,
        "ai.ScriptsGenerator.Command.BasicAction.MoveToCoordinatesBasic"));

        //Function MoveToUnitBasic
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add(new Parameter("playerTarget", null, null, playerTargetDiscrete));
        parameters.add(new Parameter("behaviour", null, null, behaviourDiscrete));
        //parameters.add(new Parameter("u", null, null, null));
        basicFunctionsForGrammar.add(new FunctionsforGrammar("moveToUnit", parameters, 
        "ai.ScriptsGenerator.Command.BasicAction.MoveToUnitBasic"));

        //Function TrainBasic
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add(new Parameter("Quantity", 1.0, 20.0, null));
        parameters.add(new Parameter("priorityPos", null, null, priorityPositionDiscrete));
        basicFunctionsForGrammar.add(new FunctionsforGrammar("train", parameters,
        "ai.ScriptsGenerator.Command.BasicAction.TrainBasic"));

    }

    public void createTableConditionalsGrammar() {
        conditionalsForGrammar = new ArrayList<>();

        //Conditional HaveEnemiesinUnitsRange
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        //parameters.add(new Parameter("u", null, null, null));
        conditionalsForGrammar.add(new FunctionsforGrammar("HaveEnemiesinUnitsRange", parameters, 
        "ai.ScriptsGenerator.BasicConditional.functions.HaveEnemiesinUnitsRange"));

        //Conditional HaveQtdEnemiesbyType
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add(new Parameter("Quantity", 1.0, 20.0, null));
        conditionalsForGrammar.add(new FunctionsforGrammar("HaveQtdEnemiesbyType", parameters,
        "ai.ScriptsGenerator.BasicConditional.functions.HaveQtdEnemiesbyType"));

        //Conditional HaveQtdEnemiesAttacking
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add(new Parameter("Quantity", 1.0, 20.0, null));
        conditionalsForGrammar.add(new FunctionsforGrammar("HaveQtdUnitsAttacking", parameters,
        "ai.ScriptsGenerator.BasicConditional.functions.HaveQtdEnemiesAttacking"));

        //Conditional HaveQtdUnitsbyType
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add(new Parameter("Quantity", 1.0, 20.0, null));
        conditionalsForGrammar.add(new FunctionsforGrammar("HaveQtdUnitsbyType", parameters,
        "ai.ScriptsGenerator.BasicConditional.functions.HaveQtdUnitsbyType"));

        //Conditional HaveQtdUnitsHarvesting
        parameters = new ArrayList<>();
        parameters.add(new Parameter("Quantity", 1.0, 20.0, null));
        conditionalsForGrammar.add(new FunctionsforGrammar("HaveQtdUnitsHarversting", parameters,
        "ai.ScriptsGenerator.BasicConditional.functions.HaveQtdUnitsHarvesting"));

        //Conditional HaveUnitsinEnemyRange
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        //parameters.add(new Parameter("u", null, null, null));
        conditionalsForGrammar.add(new FunctionsforGrammar("HaveUnitsinEnemyRange", parameters,
        "ai.ScriptsGenerator.BasicConditional.functions.HaveUnitsinEnemyRange"));

        //Conditional HaveUnitsToDistantToEnemy
        parameters = new ArrayList<>();
        parameters.add(new Parameter("unitType", null, null, typeUnitDiscrete));
        parameters.add(new Parameter("Distance", 1.0, 20.0, null));
        //parameters.add(new Parameter("u", null, null, null));
        conditionalsForGrammar.add(new FunctionsforGrammar("HaveUnitsToDistantToEnemy", parameters,
        "ai.ScriptsGenerator.BasicConditional.functions.HaveUnitsToDistantToEnemy"));

    }

    /**
     * @return the basicFunctionsForGrammar
     */
    public List<FunctionsforGrammar> getBasicFunctionsForGrammar() {
        return basicFunctionsForGrammar;
    }

    /**
     * @return the conditionalsForGrammar
     */
    public List<FunctionsforGrammar> getConditionalsForGrammar() {
        return conditionalsForGrammar;
    }

    public String getNameFunction() {
        return nameFunction;
    }

    public String getClassName() {
        return className;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
    
    

}
