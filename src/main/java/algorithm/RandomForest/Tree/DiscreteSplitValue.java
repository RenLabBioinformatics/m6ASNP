/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithm.RandomForest.Tree;

/**
 *
 * @author Ben
 */
public class DiscreteSplitValue {
    private double value = 0;
    private double attribute = 0;
    private int count = 0;
    private double average = 0;

    public double getValue() {
        return value;
    }

    public void addValue(double value) {
        this.value = this.value + value;
        this.count = this.count + 1;
    }

    public double getAttribute() {
        return attribute;
    }

    public void setAttribute(double attribute) {
        this.attribute = attribute;
    }

    public double getAverage() {
        return value / ((double) count);
    }
}
