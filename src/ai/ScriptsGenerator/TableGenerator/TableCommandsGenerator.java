/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.TableGenerator;

import PVAI.util.Permutation;
import ai.ScriptsGenerator.Command.BasicAction.AttackBasic;
import ai.ScriptsGenerator.Command.BasicAction.BuildBasic;
import ai.ScriptsGenerator.Command.BasicAction.HarvestBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToCoordinatesBasic;
import ai.ScriptsGenerator.Command.BasicAction.MoveToUnitBasic;
import ai.ScriptsGenerator.Command.BasicAction.TrainBasic;
import ai.ScriptsGenerator.Command.Enumerators.EnumPlayerTarget;
import ai.ScriptsGenerator.Command.Enumerators.EnumPositionType;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.IParameters.IParameters;
import ai.ScriptsGenerator.ParametersConcrete.ClosestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.CoordinatesParam;
import ai.ScriptsGenerator.ParametersConcrete.FarthestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.LessHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.MostHealthyEnemy;
import ai.ScriptsGenerator.ParametersConcrete.PlayerTargetParam;
import ai.ScriptsGenerator.ParametersConcrete.PriorityPositionParam;
import ai.ScriptsGenerator.ParametersConcrete.QuantityParam;
import ai.ScriptsGenerator.ParametersConcrete.RandomEnemy;
import ai.ScriptsGenerator.ParametersConcrete.StrongestEnemy;
import ai.ScriptsGenerator.ParametersConcrete.TypeConcrete;
import ai.ScriptsGenerator.ParametersConcrete.WeakestEnemy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class TableCommandsGenerator {

    UnitTypeTable utt;
    ArrayList<ICommand> commands;
    HashMap<Integer, ICommand> dicCommand;

    //list of fixed param
    private static final int MAX_QTD_WORKERS_HARVERST = 10;
    private static final int MAX_QTD_UNITS_TO_BUILD = 3;
    private static final int MAX_QTD_UNITS_TO_TRAIN = 20;
    private static final int MAP_SIZE = 24;

    public TableCommandsGenerator(UnitTypeTable utt) {
        this.utt = utt;
        commands = new ArrayList<>();
        dicCommand = new HashMap<>();
        generateTable();
        generateDic();
    }

    public ICommand getCommandByID(int ID) {
        return dicCommand.get(ID);
    }

    public int getNumberOfCommands() {
        return dicCommand.keySet().size();
    }

    @Override
    public String toString() {
        /* String text = "";
        for (ICommand cmd : commands) {
            text += cmd.toString() + "\n";
        }
        return "TableCommandsGenerator " + text;
         */
        
        String text = "";
        
        for (Integer key : dicCommand.keySet()) {
            text += key+"-"+ dicCommand.get(key).toString()+ "\n";
        }
        
        return "TableCommandsGenerator " + text;
    }

    private void generateTable() {
        commands.clear();
        //harvest
        commands.addAll(getHarvestCommands());
        //build
        commands.addAll(getBuildCommands());
        //Train
        commands.addAll(getTrainCommands());
        //attack
        commands.addAll(getAttackCommands());
        //MoveToUnit
        commands.addAll(getMoveToUnit());
        //MoveToCoordenates
        commands.addAll(getMoveToCoordenates());

        //------------- booleans
    }

    private Collection<? extends ICommand> getHarvestCommands() {
        ArrayList<ICommand> tCommandHarvest = new ArrayList<>();

        for (int i = 1; i <= MAX_QTD_WORKERS_HARVERST; i++) {
            HarvestBasic harverst = new HarvestBasic();
            harverst.addParameter(TypeConcrete.getTypeWorker()); //add unit type
            harverst.addParameter(new QuantityParam(i)); //add qtd unit
            tCommandHarvest.add(harverst);
        }

        return tCommandHarvest;
    }

    private Collection<? extends ICommand> getBuildCommands() {
        ArrayList<ICommand> tCommandBuild = new ArrayList<>();

        //build barracks
        for (int i = 0; i <= MAX_QTD_UNITS_TO_BUILD; i++) {
            BuildBasic build = new BuildBasic();
            build.addParameter(TypeConcrete.getTypeBarracks()); //add unit construct type
            build.addParameter(new QuantityParam(i)); //add qtd unit
            tCommandBuild.add(build);
        }

        //build bases
        for (int i = 0; i <= MAX_QTD_UNITS_TO_BUILD; i++) {
            BuildBasic build = new BuildBasic();
            build.addParameter(TypeConcrete.getTypeBase()); //add unit construct type
            build.addParameter(new QuantityParam(i)); //add qtd unit
            tCommandBuild.add(build);
        }
        return tCommandBuild;
    }

    private Collection<? extends ICommand> getTrainCommands() {
        ArrayList<ICommand> tCommandTrain = new ArrayList<>();
        //generate position permutation
        char v[] = {'0', '1', '2', '3'};
        Permutation.clear();
        Permutation.createPermutation(v, 4);
        //Permutation.imprime();
        //train using base
        for (int i = 1; i <= MAX_QTD_UNITS_TO_TRAIN; i++) {
            for (int j = 0; j < Permutation.totalItens(); j++) {
                TrainBasic train = new TrainBasic();
                train.addParameter(TypeConcrete.getTypeBase()); //add unit construct type
                train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
                //train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
                train.addParameter(new QuantityParam(i)); //add qtd unit
                PriorityPositionParam pos = new PriorityPositionParam();
                for (Integer position : Permutation.getPermutation(j)) {
                    pos.addPosition(EnumPositionType.byCode(position));
                }

                train.addParameter(pos);
                tCommandTrain.add(train);
            }
        }

        //train using barracks
        //light
        for (int i = 1; i <= MAX_QTD_UNITS_TO_TRAIN; i++) {
            for (int j = 0; j < Permutation.totalItens(); j++) {
                TrainBasic train = new TrainBasic();
                train.addParameter(TypeConcrete.getTypeBarracks()); //add unit construct type
                train.addParameter(TypeConcrete.getTypeLight()); //add unit Type
                //train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
                train.addParameter(new QuantityParam(i)); //add qtd unit
                PriorityPositionParam pos = new PriorityPositionParam();
                for (Integer position : Permutation.getPermutation(j)) {
                    pos.addPosition(EnumPositionType.byCode(position));
                }

                train.addParameter(pos);
                tCommandTrain.add(train);
            }
        }
        //ranged
        for (int i = 1; i <= MAX_QTD_UNITS_TO_TRAIN; i++) {
            for (int j = 0; j < Permutation.totalItens(); j++) {
                TrainBasic train = new TrainBasic();
                train.addParameter(TypeConcrete.getTypeBarracks()); //add unit construct type
                train.addParameter(TypeConcrete.getTypeRanged()); //add unit Type
                //train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
                train.addParameter(new QuantityParam(i)); //add qtd unit
                PriorityPositionParam pos = new PriorityPositionParam();
                for (Integer position : Permutation.getPermutation(j)) {
                    pos.addPosition(EnumPositionType.byCode(position));
                }

                train.addParameter(pos);
                tCommandTrain.add(train);
            }
        }
        //heavy
        for (int i = 1; i <= MAX_QTD_UNITS_TO_TRAIN; i++) {
            for (int j = 0; j < Permutation.totalItens(); j++) {
                TrainBasic train = new TrainBasic();
                train.addParameter(TypeConcrete.getTypeBarracks()); //add unit construct type
                train.addParameter(TypeConcrete.getTypeRanged()); //add unit Type
                //train.addParameter(TypeConcrete.getTypeWorker()); //add unit Type
                train.addParameter(new QuantityParam(i)); //add qtd unit
                PriorityPositionParam pos = new PriorityPositionParam();
                for (Integer position : Permutation.getPermutation(j)) {
                    pos.addPosition(EnumPositionType.byCode(position));
                }

                train.addParameter(pos);
                tCommandTrain.add(train);
            }
        }

        return tCommandTrain;
    }

    private Collection<? extends ICommand> getAttackCommands() {
        ArrayList<ICommand> tCommandAttack = new ArrayList<>();
        //attack just with workers
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                AttackBasic attack = new AttackBasic();
                attack.addParameter(getTypeUnitByNumber(j)); //add unit type
                PlayerTargetParam pt = new PlayerTargetParam();
                pt.addPlayer(EnumPlayerTarget.Enemy);
                attack.addParameter(pt);
                attack.addParameter(getBehaviorByNumber(i)); //add behavior
                tCommandAttack.add(attack);
            }
        }

        return tCommandAttack;
    }

    private IParameters getBehaviorByNumber(int i) {
        switch (i) {
            case 0:
                return new ClosestEnemy();
            case 1:
                return new FarthestEnemy();
            case 2:
                return new LessHealthyEnemy();
            case 3:
                return new MostHealthyEnemy();
            case 4:
                return new StrongestEnemy();
            case 5:
                return new WeakestEnemy();
            default:
                return new RandomEnemy();

        }
    }

    private IParameters getTypeUnitByNumber(int j) {
        switch (j) {
            case 0:
                return TypeConcrete.getTypeWorker();
            case 1:
                return TypeConcrete.getTypeLight();
            case 2:
                return TypeConcrete.getTypeRanged();
            case 3:
                return TypeConcrete.getTypeHeavy();
            default:
                return TypeConcrete.getTypeUnits();

        }
    }

    private Collection<? extends ICommand> getMoveToCoordenates() {
        ArrayList<ICommand> tCommandMove = new ArrayList<>();
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {
                for (int u = 0; u < 5; u++) {
                    MoveToCoordinatesBasic moveToCoordinates = new MoveToCoordinatesBasic();
                    moveToCoordinates.addParameter(new CoordinatesParam(x, y)); //add unit type
                    moveToCoordinates.addParameter(getTypeUnitByNumber(u));
                    tCommandMove.add(moveToCoordinates);
                }
            }

        }

        return tCommandMove;
    }

    private Collection<? extends ICommand> getMoveToUnit() {
        ArrayList<ICommand> tCommandMove = new ArrayList<>();
        for (int u = 0; u < 5; u++) {
            for (int p = 0; p < 2; p++) {
                for (int i = 0; i < 7; i++) {
                    //
                    MoveToUnitBasic moveToUnit = new MoveToUnitBasic();
                    moveToUnit.addParameter(getTypeUnitByNumber(u)); //add unit type
                    PlayerTargetParam pt = new PlayerTargetParam();
                    pt.addPlayer(getPlayerTargetByNumber(p));
                    moveToUnit.addParameter(pt);
                    moveToUnit.addParameter(getBehaviorByNumber(i)); //add behavior
                    //
                    tCommandMove.add(moveToUnit);
                }
            }
        }

        return tCommandMove;
    }

    private EnumPlayerTarget getPlayerTargetByNumber(int p) {
        if (p == 0) {
            return EnumPlayerTarget.Ally;
        }
        return EnumPlayerTarget.Enemy;
    }

    private void generateDic() {
        int cont = 0;
        for (ICommand command : commands) {
            dicCommand.put(cont, command);
            cont++;
        }
        commands.clear();
    }

}
