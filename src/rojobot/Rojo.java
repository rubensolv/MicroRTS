/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rojobot;

import ai.RandomBiasedAI;
import ai.ScriptsGenerator.ChromosomeAI;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.GPCompiler.FunctionGPCompiler;
import ai.ScriptsGenerator.GPCompiler.ICompiler;
import ai.ScriptsGenerator.GPCompiler.MainGPCompiler;
import ai.ScriptsGenerator.ParametersConcrete.ConstructionTypeParam;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.abstraction.pathfinding.PathFinding;
import ai.asymmetric.PGS.LightPGSSCriptChoice;
import ai.asymmetric.SSS.LightSSSmRTSScriptChoice;
import ai.asymmetric.SSS.SSSmRTSScriptChoiceRandom;
import ai.competition.capivara.CmabAssymetricMCTS;
import ai.configurablescript.BasicExpandedConfigurableScript;
import ai.configurablescript.ScriptsCreator;
import ai.core.AI;
import ai.core.ParameterSpecification;
import ai.evaluation.EvaluationFunction;
import ai.evaluation.SimpleSqrtEvaluationFunction3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import rts.GameState;
import rts.PhysicalGameState;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;

/**
 *
 * @author Julian, Rubens and Levi
 */
public class Rojo extends AbstractionLayerAI {

	GameState gs_to_start_from = null;
	int playerForThisComputation;
	long start_time = 0;
	boolean started = false;
	AI baseAI = null;
	UnitTypeTable utt;
	ICompiler compiler = new MainGPCompiler();
	HashSet<String> usedCommands;
	Random rand = new Random();


	public Rojo(UnitTypeTable utt){
		super(new AStarPathFinding(),100, 200);
		this.utt = utt;
	}

	@Override
	public void reset() {
		this.started = false;
		baseAI=null;
	}

	@Override
	public PlayerAction getAction(int player, GameState gs) throws Exception {
		if (gs.canExecuteAnyAction(player)) {
			if(!started){
				initializeIA(player, gs);
				started = true;
			}
			//startNewComputation(player, gs);
			return baseAI.getAction(player, gs);
		} else {
			return new PlayerAction();
		}
	}

	@Override
	public AI clone() {
		Rojo newRojo=new Rojo(utt);
		newRojo.baseAI=this.baseAI;
		return newRojo;
	}


	private void initializeIA(int player, GameState gs) {
		
		PhysicalGameState pgs = gs.getPhysicalGameState();   
		
		if(pgs.getHeight() == 8 && pgs.getWidth() == 8){ 
			//FourBasesWorkers8x8
			if(getUnitsbyType(gs,player,"Base")==4)
			{
				baseAI=decodeScripts(utt, "for(u) (attack(Worker,closest,u)) if(HaveQtdUnitsAttacking(Worker,4)) (harvest(50)) if(HaveQtdEnemiesbyType(Worker,2)) (moveToUnit(Worker,Ally,lessHealthy)) for(u) (attack(Worker,lessHealthy,u)) for(u) (harvest(50,u)) train(Worker,50,Up) for(u) (moveToUnit(Worker,Ally,lessHealthy,u))");
			}
			//basesWorkers8x8A and others 8x8 maps
			else
			{
				baseAI=decodeScripts(utt, "for(u) (train(Worker,50,Down,u)) for(u) (if(HaveQtdUnitsHarversting(1,u)) (attack(Worker,closest,u)) (harvest(50,u))) for(u) (attack(Worker,closest,u))");
			}
		} else if(pgs.getHeight() == 16 && pgs.getWidth() == 16){
			//TwoBasesBarracks16x16.xml
			if(getUnitsbyType(gs,player,"Base")==2 && getUnitsbyType(gs,player,"Barracks")==2)
			{
				baseAI=decodeScripts(utt, "train(Light,50,EnemyDir) train(Worker,50,Right) attack(Light,closest) for(u) (harvest(50,u)) for(u) (attack(Worker,mostHealthy,u)) for(u) (moveToUnit(Worker,Ally,farthest,u)) attack(Light,strongest)");
			}
			//basesWorkers16x16A and others 16x16 maps
			else
			{
				baseAI=decodeScripts(utt, "for(u) (if(HaveUnitsToDistantToEnemy(Worker,4,u)) (attack(Worker,closest,u)) (harvest(50,u)) train(Worker,50,EnemyDir,u)) for(u) (moveToUnit(Worker,Ally,lessHealthy,u)) for(u) (harvest(50,u)) attack(Worker,farthest)");
			}       	

		} else if(pgs.getHeight() == 8 && pgs.getWidth() == 9){
			//NoWhereToRun9x8.xml and others 9x8 maps
			baseAI=decodeScripts(utt, "build(Barrack,50,Right) for(u) (if(HaveQtdUnitsbyType(Worker,4,u)) (train(Ranged,50,Up,u)) (train(Worker,50,Right))) attack(Ranged,closest) for(u) (harvest(50,u)) for(u) (if(HaveQtdUnitsbyType(Worker,4,u)) (moveToUnit(Worker,Ally,mostHealthy,u))) for(u) (train(Ranged,50,Up))");

		} else if(pgs.getHeight()==24 && pgs.getWidth()==24) {
			//DoubleGame24x24 and others 24x24 maps
			baseAI=decodeScripts(utt, "if(HaveQtdEnemiesbyType(Worker,2)) (train(Worker,50,Up)) (train(Worker,50,Up)) for(u) (if(HaveQtdUnitsHarversting(2,u)) (attack(Worker,closest,u)) harvest(50,u)) if(HaveQtdUnitsbyType(Worker,6)) (moveToUnit(Worker,Ally,mostHealthy))");

		} else if(pgs.getHeight()==32 && pgs.getWidth()==32) {
			//BWDistantResources32x32 and others 32x32 maps
			baseAI=decodeScripts(utt, "for(u) (attack(Worker,closest,u)) train(Worker,50,Right) for(u) (moveaway(Worker,u))");

		} else if(pgs.getHeight()==64 && pgs.getWidth()==64) {
			//(4)BloodBath and others 64x64 maps
			baseAI=decodeScripts(utt, "for(u) (train(Worker,50,Left,u)) for(u) (if(HaveUnitsToDistantToEnemy(Worker,18,u)) (attack(Worker,closest,u))) for(u) (harvest(50,u)) for(u) (attack(Worker,closest,u))");

		} else { // Here we choose one of the 8 scripts showed above just in case of a map with a new size (it should not happen)
			int choice = rand.nextInt(8);
			if(choice==0)
			{
				baseAI=decodeScripts(utt, "for(u) (attack(Worker,closest,u)) if(HaveQtdUnitsAttacking(Worker,4)) (harvest(50)) if(HaveQtdEnemiesbyType(Worker,2)) (moveToUnit(Worker,Ally,lessHealthy)) for(u) (attack(Worker,lessHealthy,u)) for(u) (harvest(50,u)) train(Worker,50,Up) for(u) (moveToUnit(Worker,Ally,lessHealthy,u))");
			}
			else if(choice==1)
			{
				baseAI=decodeScripts(utt, "for(u) (train(Worker,50,Down,u)) for(u) (if(HaveQtdUnitsHarversting(1,u)) (attack(Worker,closest,u)) (harvest(50,u))) for(u) (attack(Worker,closest,u))");
			}
			else if(choice==2)
			{
				baseAI=decodeScripts(utt, "train(Light,50,EnemyDir) train(Worker,50,Right) attack(Light,closest) for(u) (harvest(50,u)) for(u) (attack(Worker,mostHealthy,u)) for(u) (moveToUnit(Worker,Ally,farthest,u)) attack(Light,strongest)");
			}
			else if (choice==3)
			{
				baseAI=decodeScripts(utt, "for(u) (if(HaveUnitsToDistantToEnemy(Worker,4,u)) (attack(Worker,closest,u)) (harvest(50,u)) train(Worker,50,EnemyDir,u)) for(u) (moveToUnit(Worker,Ally,lessHealthy,u)) for(u) (harvest(50,u)) attack(Worker,farthest)");
			}
			else if(choice==4)
			{
				baseAI=decodeScripts(utt, "build(Barrack,50,Right) for(u) (if(HaveQtdUnitsbyType(Worker,4,u)) (train(Ranged,50,Up,u)) (train(Worker,50,Right))) attack(Ranged,closest) for(u) (harvest(50,u)) for(u) (if(HaveQtdUnitsbyType(Worker,4,u)) (moveToUnit(Worker,Ally,mostHealthy,u))) for(u) (train(Ranged,50,Up))");
			}
			else if(choice==5)
			{
				baseAI=decodeScripts(utt, "if(HaveQtdEnemiesbyType(Worker,2)) (train(Worker,50,Up)) (train(Worker,50,Up)) for(u) (if(HaveQtdUnitsHarversting(2,u)) (attack(Worker,closest,u)) harvest(50,u)) if(HaveQtdUnitsbyType(Worker,6)) (moveToUnit(Worker,Ally,mostHealthy))");
			}
			else if (choice==6)
			{
				baseAI=decodeScripts(utt, "for(u) (attack(Worker,closest,u)) train(Worker,50,Right) for(u) (moveaway(Worker,u))");
			}
			else
			{
				baseAI=decodeScripts(utt, "for(u) (train(Worker,50,Left,u)) for(u) (if(HaveUnitsToDistantToEnemy(Worker,18,u)) (attack(Worker,closest,u))) for(u) (harvest(50,u)) for(u) (attack(Worker,closest,u))");
			}       	
		}


	}


	public AI decodeScripts(UnitTypeTable utt, String iScripts) {
		List<AI> scriptsAI = new ArrayList<>();
		scriptsAI.add(buildCommandsIA(utt, iScripts));
		return scriptsAI.get(0);
	}

	private AI buildCommandsIA(UnitTypeTable utt, String code) {
		HashMap<Long, String> counterByFunction=new HashMap<Long, String>();
		usedCommands=new HashSet<String> ();
		FunctionGPCompiler.counterCommands=0;
		List<ICommand> commandsGP = compiler.CompilerCode(code, utt);
		AI aiscript = new ChromosomeAI(utt, commandsGP, "P1", code, usedCommands,counterByFunction);
		return aiscript;
	}

	private int getUnitsbyType(GameState game, int player, String type) {
		int quantityUnits=0;
		for (Unit un : game.getUnits()) {
			if (un.getPlayer() == player && un.getType() == utt.getUnitType(type)) {
				quantityUnits++;
			}
		}
		return quantityUnits;
	}

	@Override
	public List<ParameterSpecification> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
}