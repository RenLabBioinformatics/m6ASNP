/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithm.RandomForest.Tree;

import java.util.Comparator;

/**
 *
 * @author Ben
 */
public class CompareAttribute implements Comparator{

    private int attrIndex;
    
    public CompareAttribute(int compareIndex)
    {
        attrIndex = compareIndex;
    }
    
    @Override
    public int compare(Object t, Object t1) {
        double[] data1 = (double[])t;
        double[] data2 = (double[])t1;
        if(data1[attrIndex] > data2[attrIndex])
            return 1;
        else if(data1[attrIndex] < data2[attrIndex])
            return -1;
        else
            return 0;
    }
    
}
