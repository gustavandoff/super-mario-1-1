/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package marioserver;

import java.util.Comparator;

/**
 *
 * @author gusta
 */
public class CompareScore implements Comparator<String>{

    /**
     * jämför en sträng med tider
     * @param t1 den första tiden
     * @param t2 den andra tiden
     * @return 1, -1, eller 0 beroende på om t1 är större, mindre eller likamed t2
     */
    @Override
    public int compare(String t1, String t2) {
        int sec1 = Integer.valueOf(t1.substring(0,2));
        int sec2 = Integer.valueOf(t2.substring(0,2));
        
        int mil1 = Integer.valueOf(t1.substring(3,5));
        int mil2 = Integer.valueOf(t2.substring(3,5));
        
        if (sec1 != sec2){
            return Integer.compare(sec1, sec2);
        }
        
        return Integer.compare(mil1, mil2);
    }
    
}
