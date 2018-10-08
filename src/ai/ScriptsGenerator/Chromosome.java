/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator;

import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.BasicAction.TrainBasic;
import ai.ScriptsGenerator.Command.BasicBoolean.AllyRange;
import ai.ScriptsGenerator.Command.BasicBoolean.DistanceFromEnemy;
import ai.ScriptsGenerator.Command.BasicBoolean.EnemyRange;
import ai.ScriptsGenerator.Command.BasicBoolean.NAllyUnitsAttacking;
import ai.ScriptsGenerator.Command.BasicBoolean.NAllyUnitsHarvesting;
import ai.ScriptsGenerator.Command.BasicBoolean.NAllyUnitsofType;
import ai.ScriptsGenerator.Command.BasicBoolean.NEnemyUnitsofType;
import ai.ScriptsGenerator.Command.BasicAction.HarvestBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToCoordinatesBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToUnitBasic;
import ai.ScriptsGenerator.Command.Enumerators.EnumPositionType;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.ParametersConcrete.ClosestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.CoordinatesParam;
import ai.ScriptsGenerator.ParametersConcrete.DistanceParam;
import ai.ScriptsGenerator.ParametersConcrete.FarthestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.LessHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.MostHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.IPlayerTargetParam;
import ai.ScriptsGenerator.ParametersConcrete.PriorityPositionParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.StrongestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.ParametersConcrete.WeakestEnemy;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import java.util.ArrayList;
import java.util.List;
import rts.GameState;
import rts.PlayerAction;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class Chromosome {

	List<ICommand> commands = new ArrayList<>();
	List<ICommand> commandsforBoolean;
	UnitTypeTable utt;

	public Chromosome(UnitTypeTable utt) {
		this.utt = utt;

		//BASIC ACTIONS**********************************************************************

		//train action
		TrainBasic train = new TrainBasic();
		train.addParameter(TypeConcrete.getTypeBase()); //add unit construct type
		train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
		train.addParameter(new QuantityParam(20)); //add qtd unit
		PriorityPositionParam pos = new PriorityPositionParam();
		pos.addPosition(EnumPositionType.Up);
		pos.addPosition(EnumPositionType.Left);
		//pos.addPosition(EnumPositionType.Right);
		//pos.addPosition(EnumPositionType.Down);
		train.addParameter(pos);
		commands.add(train);

		//harverst action
		HarvestBasic harverst = new HarvestBasic();
		harverst.addParameter(TypeConcrete.getTypeWorker()); //add unit type
		harverst.addParameter(new QuantityParam(1)); //add qtd unit
		commands.add(harverst);

		//attack action
//		AttackBasic attack = new AttackBasic();
//		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		attack.addParameter(new IPlayerTargetParam(0)); //add player target
//		attack.addParameter(new ClosestEnemy()); //add behavior
//		commands.add(attack);    

		//Move to unit action
//		MoveToUnitBasic moveToUnit = new MoveToUnitBasic();
//		moveToUnit.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		moveToUnit.addParameter(new IPlayerTargetParam(0)); //add player target
//		moveToUnit.addParameter(new LessHealthyEnemy()); //add behavior
//		commands.add(moveToUnit);

		//Move To coordinates
//		MoveToCoordinatesBasic moveToCoordinates = new MoveToCoordinatesBasic();
//		moveToCoordinates.addParameter(new CoordinatesParam(14,5)); //add unit type
//		moveToCoordinates.addParameter(TypeConcrete.getTypeUnits());
//		commands.add(moveToCoordinates);
		
		
		//BOOLEANS *********************************************************************************************************************************

		//BOOLEAN  If there is an ally in enemyRange 
//		AttackBasic attack = new AttackBasic();
//		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		attack.addParameter(new IPlayerTargetParam(0)); //add player target
//		attack.addParameter(new ClosestEnemy()); //add behavior   
//		commandsforBoolean = new ArrayList<>();
//		commandsforBoolean.add(attack);
//
//		EnemyRange enemyRangeBoolean = new EnemyRange(commandsforBoolean);
//		enemyRangeBoolean.addParameter(TypeConcrete.getTypeUnits());
//		commands.add(enemyRangeBoolean);


		//BOOLEAN  If there is an enemy in allyRange 

//		AttackBasic attack = new AttackBasic();
//		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		attack.addParameter(new IPlayerTargetParam(0)); //add player target
//		attack.addParameter(new ClosestEnemy()); //add behavior   
//		commandsforBoolean = new ArrayList<>();
//		commandsforBoolean.add(attack);
//
//		AllyRange allyRangeBoolean = new AllyRange(commandsforBoolean);
//		allyRangeBoolean.addParameter(TypeConcrete.getTypeUnits());
//		commands.add(allyRangeBoolean);   


		//BOOLEAN  If ally Is In Distance X From Enemy 

//		AttackBasic attack = new AttackBasic();
//		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		attack.addParameter(new IPlayerTargetParam(0)); //add player target
//		attack.addParameter(new ClosestEnemy()); //add behavior   
//		commandsforBoolean = new ArrayList<>();
//		commandsforBoolean.add(attack);
//
//		DistanceFromEnemy distanceFromEnemyBoolean = new DistanceFromEnemy(commandsforBoolean);
//		distanceFromEnemyBoolean.addParameter(TypeConcrete.getTypeUnits());
//		distanceFromEnemyBoolean.addParameter(new DistanceParam(6));
//		commands.add(distanceFromEnemyBoolean); 
		

		//BOOLEAN  If there are X ally units of type T 

//		AttackBasic attack = new AttackBasic();
//		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		attack.addParameter(new IPlayerTargetParam(0)); //add player target
//		attack.addParameter(new ClosestEnemy()); //add behavior   
//		commandsforBoolean = new ArrayList<>();
//		commandsforBoolean.add(attack);
//
//		NAllyUnitsofType nAllyOfTypeBoolean = new NAllyUnitsofType(commandsforBoolean);
//		nAllyOfTypeBoolean.addParameter(TypeConcrete.getTypeUnits());
//		nAllyOfTypeBoolean.addParameter(new QuantityParam(3));
//		commands.add(nAllyOfTypeBoolean); 
		
		
		//BOOLEAN  If there are X enemy units of type T 

		AttackBasic attack = new AttackBasic();
		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
		attack.addParameter(new IPlayerTargetParam(0)); //add player target
		attack.addParameter(new ClosestEnemy()); //add behavior   
		commandsforBoolean = new ArrayList<>();
		commandsforBoolean.add(attack);

		NEnemyUnitsofType nAllyOfTypeBoolean = new NEnemyUnitsofType(commandsforBoolean);
		nAllyOfTypeBoolean.addParameter(TypeConcrete.getTypeUnits());
		nAllyOfTypeBoolean.addParameter(new QuantityParam(3));
		commands.add(nAllyOfTypeBoolean); 

		//BOOLEAN  If there are X ally units of type T attacking

//		AttackBasic attack = new AttackBasic();
//		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		attack.addParameter(new IPlayerTargetParam(0)); //add player target
//		attack.addParameter(new ClosestEnemy()); //add behavior   
//		commandsforBoolean = new ArrayList<>();
//		commandsforBoolean.add(attack);
//
//		NAllyUnitsAttacking nAllyAttackingBoolean = new NAllyUnitsAttacking(commandsforBoolean);
//		nAllyAttackingBoolean.addParameter(TypeConcrete.getTypeUnits());
//		nAllyAttackingBoolean.addParameter(new QuantityParam(3));
//		commands.add(nAllyAttackingBoolean); 
		
//		AttackBasic attack = new AttackBasic();
//		attack.addParameter(TypeConcrete.getTypeUnits()); //add unit type
//		attack.addParameter(new IPlayerTargetParam(0)); //add player target
//		attack.addParameter(new ClosestEnemy()); //add behavior   
//		commandsforBoolean = new ArrayList<>();
//		commandsforBoolean.add(attack);
//
//		NAllyUnitsHarvesting nAllyHarvestingBoolean = new NAllyUnitsHarvesting(commandsforBoolean);
//		nAllyHarvestingBoolean.addParameter(new QuantityParam(1));
//		commands.add(nAllyHarvestingBoolean); 


	}

	public PlayerAction getAction(int player, GameState gs) {
		PlayerAction currentActions = new PlayerAction();
		PathFinding pf = new AStarPathFinding();

		//simulate one WR
		for (ICommand command : commands) {
			currentActions = command.getAction(gs, player, currentActions, pf, utt);
		}
		System.out.println("currentActions "+currentActions.toString());
		return currentActions;
	}
}
