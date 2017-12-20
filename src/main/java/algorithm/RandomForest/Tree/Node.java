/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithm.RandomForest.Tree;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public class Node 
{
    private ArrayList<double[]> dataList;
    Node leftNode;
    Node rightNode;
    private boolean isLeaf;
    private int splitIndex;
    private double splitValue;
    private ArrayList<Double> splitValueList;
//    private HashMap<Double, Integer> classType;
    private double majorityType;
    private double impurity;
    private int index;
    
    public Node(ArrayList<double[]> data)
    {
        this.dataList = data;
        leftNode = null;
        rightNode = null;
        isLeaf = true;
        //Calculate Gini-impurity
        HashMap<Double, Integer>classType = new HashMap();
        for(double[] dataValue : dataList)
        {
            if(!classType.containsKey(dataValue[dataValue.length - 1]))
                classType.put(dataValue[dataValue.length - 1], 1);
            else
            {
                int count = classType.get(dataValue[dataValue.length - 1]) + 1;
                classType.put(dataValue[dataValue.length - 1], count);
            }
        }
        impurity = 1;
        double count, dataSize = dataList.size();
        double maxCount = -1;
        for(double classIndex : classType.keySet())
        {
            count = classType.get(classIndex);
            impurity = impurity - Math.pow(count/dataSize, 2);
            if(count > maxCount)
            {
                majorityType = classIndex;
                maxCount = count;
            }
        }
        classType.clear();
    }
    
    public Node()
    {
        this.dataList = null;
    }

    public ArrayList<double[]> getDataList() {
        return dataList;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public int getSplitIndex() {
        return splitIndex;
    }

    public void setSplitIndex(int splitIndex) {
        this.splitIndex = splitIndex;
    }

    public double getSplitValue() {
        return splitValue;
    }

    public void setSplitValue(double splitValue) {
        this.splitValue = splitValue;
    }

    public ArrayList<Double> getSplitValueList() {
        return splitValueList;
    }

    public void setSplitValueList(ArrayList<Double> splitValueList) {
        this.splitValueList = splitValueList;
    }

//    public HashMap<Double, Integer> getClassType() {
//        return classType;
//    }
//
//    public void setClassType(HashMap<Double, Integer> classType) {
//        this.classType = classType;
//    }

    public double getImpurity() {
        return impurity;
    }
    
    public int getDataSize()
    {
        return dataList.size();
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public void setIndex(int index)
    {
        this.index = index;
    }

    public double getMajorityType() {
        return majorityType;
    }

    public void setMajorityType(double majorityType) {
        this.majorityType = majorityType;
    }
    
    public Node CloneNode()
    {
        Node node = new Node(dataList);
        node.setIndex(index);
        node.setIsLeaf(isLeaf);
        node.setLeftNode(leftNode);
        node.setRightNode(rightNode);
        node.setSplitIndex(splitIndex);
        node.setSplitValue(splitValue);
        node.setSplitValueList(splitValueList);
        return node;
    }
    
    public void CleanNode()
    {
        this.dataList.clear();
//        this.classType.clear();
        if(splitValueList != null)
            this.splitValueList.clear();
    }
    
    @Override
    public String toString()
    {
        String retStr;
        retStr = index + "\t" + splitIndex + "\t" + splitValue + "\t";
        //SplitValue
        if(splitValueList != null)
        {
            for(int i=0; i<splitValueList.size()-1; i++)
                retStr = retStr + splitValueList.get(i) + ";";
            retStr = retStr + splitValueList.get(splitValueList.size()-1);
        }
        else
            retStr = retStr + "NULL";
        //majorityClass, isLeaf
        retStr = retStr + "\t" + majorityType + "\t" + isLeaf;
        if(!isLeaf)
            retStr = retStr + "\t" + leftNode.getIndex() + "\t" + rightNode.getIndex();
        else
            retStr = retStr + "\t-1\t-1";
        //return
        return retStr;
    }
}
