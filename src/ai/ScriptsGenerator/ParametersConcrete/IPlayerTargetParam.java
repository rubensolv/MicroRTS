/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.ScriptsGenerator.ParametersConcrete;

import ai.ScriptsGenerator.IParameters.IPlayerTarget;
import ai.ScriptsGenerator.IParameters.IQuantity;

/**
 *
 * @author rubens,Julian
 */
public class IPlayerTargetParam implements IPlayerTarget{

    private int value;

    public IPlayerTargetParam() {
        this.value = 0;
    }

    public IPlayerTargetParam(int value) {
        this.value = value;
    }
    
    @Override
    public int getPlayerTarget() {
        return value;
    }

    @Override
    public void setPlayerTarget(int value) {
        this.value = value;
    }
    
}
