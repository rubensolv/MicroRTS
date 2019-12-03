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
public class IfGPCompiler extends AbstractCompiler {

    protected FunctionGPCompiler functionCompiler = new FunctionGPCompiler();
    protected ConditionalGPCompiler conditionalCompiler = new ConditionalGPCompiler();

    @Override
    public List<ICommand> CompilerCode(String code, UnitTypeTable utt) {
        IfFunction ifFun = new IfFunction();

        List<ICommand> commands = new ArrayList<>();
        String[] fragments = code.split(" ");
        int pos = 0;
        //first build the if and get the conditional
        if (isIfInitialClause(fragments[pos])) {
            //remove the tags and get the conditional
            String sCond = fragments[pos];
            if (sCond.startsWith("(if(")) {
                sCond = sCond.replace("(if(", "").trim();
            } else {
                sCond = sCond.replace("if(", "").trim();
            }
            ifFun.setConditional(conditionalCompiler.getConditionalByCode(sCond));
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
            } else if (isBasicCommand(thenFragments[i])) {
                finalPos = getPositionParentClose(i, thenFragments);
                String newCode = MainGPCompiler.generateString(i, finalPos, thenFragments);
                if (newCode.startsWith("(")) {
                    newCode = newCode.substring(1, newCode.length());
                }
                if (newCode.endsWith(")")) {
                    newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                }
                ifFun.includeFullCommandsThen(functionCompiler.CompilerCode(newCode, utt));
                i = finalPos;
            } else if (thenFragments[i].contains("for")) {
                finalPos = getLastPositionForFor(i, thenFragments);
                String newCode = MainGPCompiler.generateString(i, finalPos, thenFragments);
                if (newCode.startsWith("(")) {
                    newCode = newCode.substring(1, newCode.length());
                }
                //if (newCode.endsWith(")")) {
                //    newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                //}
                ifFun.includeFullCommandsThen(ForGPCompiler.CompilerCodeStatic(newCode, utt));
                i = finalPos;
            }
        }

        pos += ++finalPos;
        //third build the else, if exists  ATTENTION
        if ((pos <= (fragments.length - 1)) && fragments[pos].startsWith("(")) {
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
                } else if (isBasicCommand(elseFragments[i])) {
                    finalPos = getPositionParentClose(i, elseFragments);
                    String newCode = MainGPCompiler.generateString(i, finalPos, elseFragments);
                    if (newCode.startsWith("(")) {
                        newCode = newCode.substring(1, newCode.length());
                    }
                    if (newCode.endsWith(")")) {
                        newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                    }
                    ifFun.includeFullCommandsElse(functionCompiler.CompilerCode(newCode, utt));
                    i = finalPos;
                } else if (elseFragments[i].contains("for")) {
                    finalPos = getLastPositionForFor(i, elseFragments);
                    String newCode = MainGPCompiler.generateString(i, finalPos, elseFragments);
                    if (newCode.startsWith("(")) {
                        newCode = newCode.substring(1, newCode.length());
                    }
                    if (newCode.endsWith(")")) {
                        newCode = newCode.substring(0, newCode.lastIndexOf(")"));
                    }
                    ifFun.includeFullCommandsElse(ForGPCompiler.CompilerCodeStatic(newCode, utt));
                    i = finalPos;
                }
            }
        }
        commands.add(ifFun);
        return commands;
    }

    public int getPositionFinalIF(int initialPosition, String[] fragments, boolean getElse) {
        //validate complex IF's
        if(getElse == false){
            //check if it is a if inside of a if
            if(fragments[initialPosition].startsWith("(if(")){
                int post_teste = getPositionParentClose(initialPosition, fragments);
                String newIF = MainGPCompiler.generateString(initialPosition, post_teste, fragments);
                newIF = newIF.substring(1, newIF.length());
                newIF = newIF.substring(0, newIF.lastIndexOf(")"));
                String[] newfragments = newIF.split(" ");
                post_teste = getPositionFinalIF(0, newfragments, true);                
                return initialPosition + post_teste;
            }
        }
        //first get the if + conditional
        if (isIfInitialClause(fragments[initialPosition])) {
            initialPosition++;
        }
        //second get the then clause
        initialPosition = getPositionParentClose(initialPosition, fragments);

        //third get the else, if exists ATTENTION
        if (getElse && ((initialPosition + 1) <= (fragments.length - 1))) {
            if (fragments[initialPosition + 1].startsWith("(")) {
                initialPosition++;
                initialPosition = getPositionParentClose(initialPosition, fragments);
            }
        }

        if (initialPosition > (fragments.length - 1)) {
            initialPosition = (fragments.length - 1);
        }
        return initialPosition;
    }

    private boolean isIfInitialClause(String fragment) {
        if (fragment.startsWith("(if") && fragment.contains("(") && fragment.contains(")")) {
            return true;
        } else if (fragment.startsWith("if") && fragment.contains("(") && fragment.contains(")")) {
            return true;
        } else {
            return false;
        }
    }

}
