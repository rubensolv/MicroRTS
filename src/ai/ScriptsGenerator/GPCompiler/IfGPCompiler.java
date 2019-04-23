/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.GPCompiler;

import ai.ScriptsGenerator.Command.BasicBoolean.IfFunction;
import ai.ScriptsGenerator.CommandInterfaces.ICommand;
import ai.ScriptsGenerator.TableGenerator.TableConditionalGenerator;
import java.util.ArrayList;
import java.util.List;
import rts.units.UnitTypeTable;

/**
 *
 * @author rubens
 */
public class IfGPCompiler implements ICompiler {

    private FunctionGPCompiler functionCompiler = new FunctionGPCompiler();

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        IfFunction ifFun = new IfFunction();;
        TableConditionalGenerator tcg = new TableConditionalGenerator(utt);

        List<ICommand> commands = new ArrayList<>();
        String[] fragments = code.split(" ");
        int pos = 0;
        //first build the if and get the conditional
        if (isIfInitialClause(fragments[pos])) {
            //remove the tags and get the conditional
            String sCond = fragments[pos];
            sCond = sCond.replace("if(", "").replace(")", "").trim();
            ifFun.setConditional(tcg.getConditionalByID(Integer.decode(sCond)));
            pos++;
        }
        //second build the then
        //get the complete then and move for the fragments.        
        int finalPos = getPositionFinalIF(pos, fragments, false);
        String thenCode = MainGPCompiler.generateString(pos, finalPos, fragments);
        thenCode = thenCode.substring(1, thenCode.length());
        thenCode = thenCode.substring(0, thenCode.lastIndexOf(")"));
        String[] thenFragments = thenCode.split(" ");
        for (int i = 0; i < thenFragments.length; i++) {
            if (thenFragments[i].contains("if")) {
                finalPos = getPositionFinalIF(i, thenFragments, true);
                String newCode = MainGPCompiler.generateString(i, finalPos, thenFragments);
                if (newCode.startsWith("(")) {
                    newCode = newCode.substring(1, newCode.length());
                }
                if (newCode.endsWith(")") && !newCode.startsWith("if")) {
                    newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                }
                ifFun.includeFullCommandsThen(CompilerCode(newCode, utt));
                i = finalPos;
            } else if (thenFragments[i].contains("!")) {
                finalPos = getPositionParentClose(i, thenFragments);
                String newCode = MainGPCompiler.generateString(i, finalPos, thenFragments);
                if (newCode.startsWith("(")) {
                    newCode = newCode.substring(1, newCode.length());
                }
                if (newCode.endsWith(")")) {
                    newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                }
                ifFun.includeFullCommandsThen(functionCompiler.CompilerCode(newCode, utt));
            }
        }

        pos += ++finalPos;
        //third build the else, if exists  ATTENTION
        if ((pos<= (fragments.length-1) ) && fragments[pos].startsWith("(")) {
            finalPos = getPositionFinalIF(pos, fragments, false);
            String elseCode = MainGPCompiler.generateString(pos, finalPos, fragments);
            elseCode = elseCode.substring(1, elseCode.length());
            elseCode = elseCode.substring(0, elseCode.lastIndexOf(")"));
            String[] elseFragments = elseCode.split(" ");
            for (int i = 0; i < elseFragments.length; i++) {
                if (elseFragments[i].contains("if")) {
                    finalPos = getPositionFinalIF(i, elseFragments, true);
                    String newCode = MainGPCompiler.generateString(i, finalPos, elseFragments);
                    if (newCode.startsWith("(")) {
                        newCode = newCode.substring(1, newCode.length());
                    }
                    if (newCode.endsWith(")") && !newCode.startsWith("if")) {
                        newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                    }
                    ifFun.includeFullCommandsElse(CompilerCode(newCode, utt));
                    i = finalPos;
                } else if (elseFragments[i].contains("!")) {
                    finalPos = getPositionParentClose(i, elseFragments);
                    String newCode = MainGPCompiler.generateString(i, finalPos, elseFragments);
                    if (newCode.startsWith("(")) {
                        newCode = newCode.substring(1, newCode.length());
                    }
                    if (newCode.endsWith(")")) {
                        newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                    }
                    ifFun.includeFullCommandsElse(functionCompiler.CompilerCode(newCode, utt));
                }
            }
        }
        commands.add(ifFun);
        return commands;
    }

    public int getPositionFinalIF(int initialPosition, String[] fragments, boolean getElse) {

        //first get the if + conditional
        if (isIfInitialClause(fragments[initialPosition])) {
            initialPosition++;
        }
        //second get the then clause
        initialPosition = getPositionParentClose(initialPosition, fragments);

        //third get the else, if exists ATTENTION
        if(getElse && ((initialPosition+1) <= (fragments.length-1)) ){
        if ( fragments[initialPosition + 1].startsWith("(")) {
            initialPosition++;
            initialPosition = getPositionParentClose(initialPosition, fragments);
        }
        }

        if (initialPosition > (fragments.length - 1)) {
            initialPosition = (fragments.length - 1);
        }
        return initialPosition;
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

    private boolean isIfInitialClause(String fragment) {
        if (fragment.startsWith("if") && fragment.contains("(") && fragment.contains(")")) {
            return true;
        } else {
            return false;
        }
    }

    private int countCaracter(String fragment, String toFind) {
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
