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
public class TreeSaverAndParser 
{
    private Node root;
    private HashMap<Integer, SaveNode> nodeMap = new HashMap();
    
    public TreeSaverAndParser(Node root)
    {
        this.root = root;
    }
    
    public TreeSaverAndParser()
    {
        this.root = null;
    }
    
    private void getTreeStructure(StringBuffer treeStr, Node parent)
    {
        treeStr.append(parent.toString()).append("\n");
        if(!parent.isIsLeaf())
        {
            getTreeStructure(treeStr, parent.getLeftNode());
            getTreeStructure(treeStr, parent.getRightNode());
        }
    }
    
    public String getTreeString()
    {
        StringBuffer strBuf = new StringBuffer("");
        getTreeStructure(strBuf, root);
        return strBuf.toString();
    }
    
    public Node DecodeTreeFromString(String treeStr)
    {
        nodeMap.clear();
        String[] treeStrArr = treeStr.split("\n");
        String strLine;
        String[] strArr;
        for(int i=0; i<treeStrArr.length; i++)
        {
            strLine = treeStrArr[i];
            strArr = strLine.split("\t");
            SaveNode saveNode = new SaveNode();
            saveNode.setIndex(Integer.parseInt(strArr[0]));
            saveNode.setSplitIndex(Integer.parseInt(strArr[1]));
            saveNode.setSplitValue(Double.parseDouble(strArr[2]));
            //Add SplitValueList
            if(strArr[3].equals("NULL"))
                saveNode.setSplitValueList(null);
            else
            {
                String[] splitValueArr = strArr[3].split(";");
                ArrayList<Double> splitValueList = new ArrayList();
                for(int j=0; j<splitValueArr.length; j++)
                {
                    splitValueList.add(Double.parseDouble(splitValueArr[j]));
                }
                saveNode.setSplitValueList(splitValueList);
            }
            //Add assignValue
            saveNode.setMajorityType(Double.parseDouble(strArr[4]));
            //Add leaf
            saveNode.setIsLeaf(Boolean.parseBoolean(strArr[5]));
            if(!saveNode.isIsLeaf())
            {
                saveNode.setLeft(Integer.parseInt(strArr[6]));
                saveNode.setRight(Integer.parseInt(strArr[7]));
            }
            else
            {
                saveNode.setLeft(-1);
                saveNode.setRight(-1);
            }
            //Add in map
            nodeMap.put(saveNode.getIndex(), saveNode);
        }
        //Create tree
        root = new Node();
        SaveNode rootSaveNode = nodeMap.get(1);
        root.setMajorityType(rootSaveNode.getMajorityType());
        root.setIndex(rootSaveNode.getIndex());
        root.setIsLeaf(rootSaveNode.isIsLeaf());
        root.setSplitIndex(rootSaveNode.getSplitIndex());
        root.setSplitValue(rootSaveNode.getSplitValue());
        root.setSplitValueList(rootSaveNode.getSplitValueList());
        ConstructNode(root, rootSaveNode.getLeft(), rootSaveNode.getRight());
        nodeMap.clear();
        return root;
    }
    
    private void ConstructNode(Node parent,int leftIndex,int rightIndex)
    {
        if(!parent.isIsLeaf())
        {
            Node leftNode = new Node();
//            int leftIndex = parent.getLeft().getIndex();
            SaveNode leftSaveNode = nodeMap.get(leftIndex);
            leftNode.setMajorityType(leftSaveNode.getMajorityType());
            leftNode.setIndex(leftSaveNode.getIndex());
            leftNode.setIsLeaf(leftSaveNode.isIsLeaf());
            leftNode.setSplitIndex(leftSaveNode.getSplitIndex());
            leftNode.setSplitValue(leftSaveNode.getSplitValue());
            leftNode.setSplitValueList(leftSaveNode.getSplitValueList());
            parent.setLeftNode(leftNode);
            ConstructNode(leftNode, leftSaveNode.getLeft(), leftSaveNode.getRight());
            
            Node rightNode = new Node();
//            int rightIndex = parent.getRight().getIndex();
            SaveNode rightSaveNode = nodeMap.get(rightIndex);
            rightNode.setMajorityType(rightSaveNode.getMajorityType());
            rightNode.setIndex(rightSaveNode.getIndex());
            rightNode.setIsLeaf(rightSaveNode.isIsLeaf());
            rightNode.setSplitIndex(rightSaveNode.getSplitIndex());
            rightNode.setSplitValue(rightSaveNode.getSplitValue());
            rightNode.setSplitValueList(rightSaveNode.getSplitValueList());
            parent.setRightNode(rightNode);
            ConstructNode(rightNode, rightSaveNode.getLeft(), rightSaveNode.getRight());
        }
    }
    
    public static void main(String[] args) 
    {
        
    }
}
