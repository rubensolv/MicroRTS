/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;

/**
 *
 * @author rubens
 */
public class Fp_element {
    private int counter;
    private iDSL ast;

    public Fp_element(iDSL ast) {
        this.ast = ast;
        this.counter = 1;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public iDSL getAst() {
        return ast;
    }

    public void setAst(iDSL ast) {
        this.ast = ast;
    }
    
    public void incrementCount(){
        this.counter++;
    }
}
