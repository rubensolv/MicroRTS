/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.localsearch.searchImplementation;

import ai.synthesis.DslLeague.Runner.SettingsAlphaDSL;
import ai.synthesis.Novelty.Calculator.NoveltyCommandSingleton;
import ai.synthesis.Novelty.Calculator.NoveltyFuzzyMatchSingleton;
import ai.synthesis.grammar.dslTree.builderDSLTree.BuilderDSLTreeSingleton;
import ai.synthesis.grammar.dslTree.interfacesDSL.iDSL;

/**
 *
 * @author rubens
 */
public abstract class SearchImplementationAbstract implements SearchImplementation {

    private NoveltyFuzzyMatchSingleton novFuzzy;
    private NoveltyCommandSingleton novCommand;

    public SearchImplementationAbstract() {
        this.novFuzzy = NoveltyFuzzyMatchSingleton.getInstance();
        this.novCommand = NoveltyCommandSingleton.getInstance();
    }

    protected void update_dsl_scores(iDSL dsl, float score) {
        if (SettingsAlphaDSL.get_novelty_metric().equals("symmetry")) {
            novFuzzy.update_score(dsl.translate(), score);
        } else if (SettingsAlphaDSL.get_novelty_metric().equals("base_novelty")) {
            novCommand.add_commands(dsl);
        }

    }

    protected void set_dsl_transformation(String base, String reduce) {
        if (SettingsAlphaDSL.get_novelty_metric().equals("symmetry")) {
            if (!base.equals(reduce)) {
                novFuzzy.add_transformation(base, reduce);
            }
        }
    }

    protected iDSL get_neighbour_configurated(iDSL sc_neighbour, float temp_current) {
        switch (SettingsAlphaDSL.get_novelty_metric()) {
            case "symmetry":
                return get_Neighbour_Using_Symmetry_Novelty(sc_neighbour, temp_current);
            case "base_novelty":
                return get_Neighbour_Using_base_Novelty(sc_neighbour, temp_current);
            default:
                return BuilderDSLTreeSingleton.changeNeighbourPassively(sc_neighbour);
        }

    }

    private iDSL get_Neighbour_Using_base_Novelty(iDSL sc_neighbour, float temp_current) {
        int cont = 0;
        Integer score = 7;
        iDSL neighbour = (iDSL) sc_neighbour.clone();
        while (cont <= 100) {
            neighbour = (iDSL) sc_neighbour.clone();
            BuilderDSLTreeSingleton.changeNeighbourPassively(neighbour);
            score = this.novCommand.get_novelty_factor(neighbour, temp_current);
            if (score <= 3) {
                return neighbour;
            }
            //System.out.println("Refused "+neighbour.translate());
            cont++;
        }

        return neighbour;
    }

    private iDSL get_Neighbour_Using_Symmetry_Novelty(iDSL sc_neighbour, float temp_current) {
        int cont = 0;
        double novelty = 0.0d;
        iDSL neighbour = (iDSL) sc_neighbour.clone();
        while (cont < 4000) {
            neighbour = BuilderDSLTreeSingleton.changeNeighbourPassively((iDSL) sc_neighbour.clone());
            novelty = novFuzzy.get_novelty_factor(sc_neighbour.translate());
            if (novelty > 0.90d) {
                novFuzzy.add_dsl(neighbour.translate());
                return neighbour;
            }
            //do some evaluation
            if (temp_current >= 0.75f) {
                if (novelty >= 0.40d) {
                    return neighbour;
                }
            } else if (temp_current >= 0.50f && temp_current < 0.75f) {
                if (novelty >= 0.30d) {
                    return neighbour;
                }
            } else if (temp_current >= 0.15f && temp_current < 0.50f) {
                if (novelty >= 0.15d) {
                    return neighbour;
                }
            } else {
                if (novelty <= 0.15d) {
                    return neighbour;
                }
            }
            cont++;
        }
        return neighbour;
    }

}
