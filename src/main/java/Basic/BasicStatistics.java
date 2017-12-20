/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic;

import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class BasicStatistics {
    public static double getMean(LinkedList<Double> dataList)
    {
        double sum = 0;
        for(double data : dataList)
            sum = sum + data;
        return sum/((double)dataList.size());
    }
    
    public static double getVariance(LinkedList<Double> dataList, double mean)
    {
        double sum = 0;
        for(double data : dataList)
            sum = sum + Math.pow(data - mean, 2);
        return sum/(double)(dataList.size() - 1);
    }
    
    public static double getVariance(LinkedList<Double> dataList)
    {
        double mean = getMean(dataList);
        double sum = 0;
        for(double data : dataList)
            sum = sum + Math.pow(data - mean, 2);
        return sum/(double)(dataList.size() - 1);
    }
}
