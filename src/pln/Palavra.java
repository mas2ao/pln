/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pln;

/**
 *
 * @author shaolin
 */
public class Palavra {
    String palavra;
    int quant;

    public Palavra(String palavra, int quant) {
        this.palavra = palavra;
        this.quant = quant;
    }   
    

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }
    
    
    
}
