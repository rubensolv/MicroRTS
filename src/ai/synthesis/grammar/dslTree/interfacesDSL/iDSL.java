/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.grammar.dslTree.interfacesDSL;

import java.io.Serializable;

/**
 *
 * @author rubens
 */
public interface iDSL extends Serializable{
    public String translate();
    public Object clone();
}
