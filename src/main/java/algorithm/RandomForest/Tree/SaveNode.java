/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithm.RandomForest.Tree;

import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class SaveNode 
{
    private boolean isLeaf;
    private int left;
    private int right;
    private double splitValue;
    private int splitIndex;
    private double majorityType;
    private int index;
    private ArrayList<Double> splitValueList;

    public double getMajorityType() {
        return majorityType;
    }

    public void setMajorityType(double assignValue) {
        this.majorityType = assignValue;
    }

    public ArrayList<Double> getSplitValueList() {
        return splitValueList;
    }

    public void setSplitValueList(ArrayList<Double> splitValueList) {
        this.splitValueList = splitValueList;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public double getSplitValue() {
        return splitValue;
    }

    public void setSplitValue(double splitValue) {
        this.splitValue = splitValue;
    }

    public int getSplitIndex() {
        return splitIndex;
    }

    public void setSplitIndex(int splitIndex) {
        this.splitIndex = splitIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
