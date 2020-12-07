/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai.synthesis.Novelty.Calculator;

/**
 *
 * @author rubens
 */
public class ScoreNoveltyStore {
    
    private Integer quantity;
    private Double sum;

    public ScoreNoveltyStore() {
        this.quantity = 0;
        this.sum = 0.0d;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public void incrQuantity() {
        this.quantity ++;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }
    
    public void addInSum(Double sum) {
        this.sum += sum;
    }
    
    public double get_total(){
        if(sum > 0.0d){
            return this.sum/(double)this.quantity;
        }
        return 0.0d;
    }
    
    
}
