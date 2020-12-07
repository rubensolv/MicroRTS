/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;

/**
 *
 * @author rubens
 */
public interface SearchImplementation {
    
    public DetailedSearchResult run(iDSL sc_base, iDSL sc_improve, int n_steps, float winner);
    
}
