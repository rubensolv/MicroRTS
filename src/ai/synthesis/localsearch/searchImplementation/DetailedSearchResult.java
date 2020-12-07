/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;
import java.util.ArrayList;
import java.util.List;
import util.Pair;

/**
 *
 * @author rubens
 */
public class DetailedSearchResult {
    private iDSL sWinner, sBase;
    private float winner;
    private boolean defeat_by_fp_group;
    private List<Pair<String,Float>> matchs;

    public DetailedSearchResult() {
        this.defeat_by_fp_group = false;
        this.matchs = new ArrayList<>();
    }

    public DetailedSearchResult(iDSL sWinner, float winner, boolean defeat_by_fp_group) {
        this.sWinner = sWinner;
        this.winner = winner;
        this.defeat_by_fp_group = defeat_by_fp_group;
    }
    
    public iDSL getsBase() {
        return sBase;
    }

    public void setsBase(iDSL sBase) {
        this.sBase = sBase;
    }
    
    public iDSL getsWinner() {
        return sWinner;
    }

    public void setsWinner(iDSL sWinner) {
        this.sWinner = sWinner;
    }

    public float getWinner() {
        return winner;
    }

    public void setWinner(float winner) {
        this.winner = winner;
    }

    public boolean wasDefeat_by_fp_group() {
        return defeat_by_fp_group;
    }

    public void setDefeat_by_fp_group(boolean defeat_by_fp_group) {
        this.defeat_by_fp_group = defeat_by_fp_group;
    }

    public List<Pair<String,Float>> getMatchs() {
        return matchs;
    }

    public void setMatchs(List<Pair<String,Float>> matchs) {
        this.matchs = matchs;
    }
    
    public void addMatch(Pair<String,Float> match){
        this.matchs.add(match);
    }
    
    public void print(){
        System.out.println("Detailed Search Result");
        System.out.println("Script base "+sBase.translate());
        System.out.println("Script Vencedor "+sWinner.translate());
        System.out.println("Script lost for anyother? "+defeat_by_fp_group);
        if(!this.matchs.isEmpty()){
            for (Pair<String,Float> match : matchs) {
                System.out.println("   Opponent: "+match.m_a+" Score: "+match.m_b);
            }
        }
    }
    
}
