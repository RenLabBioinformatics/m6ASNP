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
public class CompareDiscreteSplitValue implements Comparator 
{
    @Override
    public int compare(Object t, Object t1) 
    {
        DiscreteSplitValue val1 = (DiscreteSplitValue)t;
        DiscreteSplitValue val2 = (DiscreteSplitValue)t1;
        double average1 = val1.getAverage();
        double average2 = val2.getAverage();
        if(average1 < average2)
            return -1;
        else if(average1 > average2)
            return 1;
        else
            return 0;
    }
}
