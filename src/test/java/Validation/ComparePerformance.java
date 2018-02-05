/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validation;

import java.util.Comparator;

/**
 *
 * @author Ben
 */
public class ComparePerformance implements Comparator<Performance> {

    @Override
    public int compare(Performance o1, Performance o2) {
        if(o1.getSp() < o2.getSp())
            return 1;
        else if(o1.getSp() == o2.getSp())
            return 0;
        else
            return -1;
    }
    
}
