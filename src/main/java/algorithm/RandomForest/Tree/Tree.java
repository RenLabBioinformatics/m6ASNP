/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithm.RandomForest.Tree;

import algorithm.RandomForest.DataReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Ben
 */
public class Tree {
    private Node root;
    private int attrLen;//number of atrributes in the data set
    private int splitNum;//number of attributes that consider in each split
    private int minNodeNum;//The minimum size in a tree node
    private double minNodeError;//The minimum error in a tree node
    private ArrayList<double[]> dataList;//bootstrap data list
//    private ArrayList<double[]> testList;//test data list for out-of-bag(OOB) estimation
    private ArrayList<Integer> dataAttrTypeList;//Attributes type list, two possibilities: Continuous or Discrete
    private double oobError;//Out-of-bag error
    private int curIndex;
    
    public Tree(ArrayList<double[]> sourceDataList, ArrayList<Integer> dataAtrrTypeList, int splitNum, int minNodeNum, double minNodeError)
    {
        BootStrapping(sourceDataList);
        this.dataAttrTypeList = dataAtrrTypeList;
        attrLen = dataList.get(0).length - 1;
        this.splitNum = splitNum;
        this.minNodeNum = minNodeNum;
        this.minNodeError = minNodeError;
        //set root
        root = new Node(dataList);
        curIndex = 1;
        root.setIndex(curIndex);
        //set sort
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    }
    
    public Tree(Node root, ArrayList<Integer> dataAtrrTypeList)
    {
        this.dataAttrTypeList = dataAtrrTypeList;
        this.root = root;
    }
    
    public Tree(String treeStr, ArrayList<Integer> dataAtrrTypeList)
    {
        TreeSaverAndParser treeParser = new TreeSaverAndParser();
        this.root = treeParser.DecodeTreeFromString(treeStr);
        this.dataAttrTypeList = dataAtrrTypeList;
    }
    
    public void setRoot(Node root)
    {
        this.root = root;
    }
    
    private void BootStrapping(ArrayList<double[]> sourceDataList)
    {
        //bootstrap training data
        HashSet<Integer> selectedIndexSet = new HashSet();
        dataList = new ArrayList(sourceDataList.size());
        for(int i=0; i<sourceDataList.size(); i++)
        {
            int selectIndex = new Random().nextInt(sourceDataList.size());
            dataList.add(sourceDataList.get(selectIndex));
            selectedIndexSet.add(selectIndex);
        }
//        //test data for OOB
//        testList = new ArrayList();
//        for(int i=0; i<sourceDataList.size(); i++)
//        {
//            if(!selectedIndexSet.contains(i))
//                testList.add(sourceDataList.get(i).clone());
//        }
    }
    
    private ArrayList<Integer> getRandomSelectAttribute()
    {
        ArrayList<Integer> selectIndex = new ArrayList(splitNum);
        //Random Select Attribute
        ArrayList<Integer> allIndexList = new ArrayList(attrLen);
        for(int i=0; i<attrLen; i++)
        {
            allIndexList.add(i);
        }
        Collections.shuffle(allIndexList);
        for(int i=0; i<splitNum; i++)
        {
            selectIndex.add(allIndexList.get(i));
        }
        //return
        return selectIndex;
    }
    
    private boolean CheckIsLeaf(Node node)
    {
        double nodeError = node.getImpurity();
        int nodeSize = node.getDataList().size();
        return (nodeError<=minNodeError)||(nodeSize<=minNodeNum);
    }
    
    private void SplitDiscreteData(ArrayList<double[]> parentDataList, ArrayList<double[]> leftDataList, ArrayList<double[]> rightDataList, ArrayList<Double> splitValueList, int attrIndex)
    {
        leftDataList.clear();
        rightDataList.clear();
        double checkAttribute = 0;
        for(int i=0; i<parentDataList.size(); i++)
        {
            checkAttribute = parentDataList.get(i)[attrIndex];
            if(splitValueList.contains(checkAttribute))
                leftDataList.add(parentDataList.get(i));
            else
                rightDataList.add(parentDataList.get(i));
        }
    }
    
    private void SplitContinuousData(ArrayList<double[]> parentDataList, ArrayList<double[]> leftDataList, ArrayList<double[]> rightDataList, int splitIndex)
    {
        leftDataList.clear();
        for(int i=0; i<=splitIndex; i++)
        {
            leftDataList.add(parentDataList.get(i));
        }
        
        rightDataList.clear();
        for(int i=splitIndex+1; i<parentDataList.size(); i++)
        {
            rightDataList.add(parentDataList.get(i));
        }
    }
    
    private ArrayList<Double> getDiscreteSortedCheckList(ArrayList<double[]> nodeDataList, int sortIndex)
    {
        HashMap<Double, DiscreteSplitValue> splitValueMap = new HashMap();
        double[] nodeData;
        for(int i=0; i<nodeDataList.size(); i++)
        {
            nodeData = nodeDataList.get(i);
            int dataLen = nodeData.length;
            DiscreteSplitValue splitValue;
            if(splitValueMap.containsKey(nodeData[sortIndex]))
            {
                splitValue = splitValueMap.get(nodeData[sortIndex]);
                splitValue.addValue(nodeData[dataLen-1]);
            }
            else
            {
                splitValue = new DiscreteSplitValue();
                splitValue.addValue(nodeData[dataLen-1]);
                splitValue.setAttribute(nodeData[sortIndex]);
                splitValueMap.put(nodeData[sortIndex], splitValue);
            }
        }
        //Convert to list
        double key = 0;
        ArrayList<DiscreteSplitValue> splitValueList = new ArrayList();
        for(Iterator<Double> itr = splitValueMap.keySet().iterator(); itr.hasNext();)
        {
            key = itr.next();
            splitValueList.add(splitValueMap.get(key));
        }
        splitValueMap.clear();
        Collections.sort(splitValueList, new CompareDiscreteSplitValue());
        //Return value list
        ArrayList<Double> checkValueList = new ArrayList();
        for(int i=0; i<splitValueList.size(); i++)
            checkValueList.add(splitValueList.get(i).getAttribute());
        return checkValueList;
    }
    
    private void RecursiveSplit(Node parent)
    {
        if(!CheckIsLeaf(parent))
        {
            //
            ArrayList<double[]> nodeDataList = parent.getDataList();
            ArrayList<Integer> indexToCheck = getRandomSelectAttribute();
            Node maxLeftNode = null;
            Node maxRightNode = null;
            int maxSplitIndex = 0;
            double maxSplitValue = 0;
            double maxDecreasedImpurity = -1000;
            ArrayList<Double> minSplitValueList = null;
            //
            for(int i=0; i<indexToCheck.size(); i++)
            {
                int checkIndex = indexToCheck.get(i);
                if(dataAttrTypeList.get(checkIndex)==DataType.Continuous)
                {
                    Collections.sort(nodeDataList, new CompareAttribute(checkIndex));
                    //CheckSplitList
                    ArrayList<Integer> checkSplitList = new ArrayList();
                    for(int j=0; j<nodeDataList.size()-1; j++)
                    {
                        if(nodeDataList.get(j)[checkIndex]!=nodeDataList.get(j+1)[checkIndex])
                            checkSplitList.add(j);
                    }
                    //Calculate GINI impurity
                    for(int j=0; j<checkSplitList.size(); j++)
                    {
                        ArrayList<double[]> leftNodeData = new ArrayList();
                        ArrayList<double[]> rightNodeData = new ArrayList();
                        SplitContinuousData(nodeDataList, leftNodeData, rightNodeData, checkSplitList.get(j));
                        Node leftNode = new Node(leftNodeData);
                        Node rightNode = new Node(rightNodeData);
                        double leftImpurity = leftNode.getImpurity();
                        double rightImpurity = rightNode.getImpurity();
                        double nodeImpurity = (leftNode.getDataSize()*leftImpurity + rightNode.getDataSize()*rightImpurity)/((double)nodeDataList.size());
                        double decreasedImpurity = parent.getImpurity() - nodeImpurity;
                        if(decreasedImpurity > maxDecreasedImpurity)
                        {
                            maxLeftNode = leftNode;
                            maxRightNode = rightNode;
                            maxDecreasedImpurity = decreasedImpurity;
                            maxSplitIndex = checkIndex;
                            maxSplitValue = (nodeDataList.get(checkSplitList.get(j))[checkIndex] + nodeDataList.get(checkSplitList.get(j)+1)[checkIndex])/2;
                        }
                    }
                }
                else
                {
                    ArrayList<Double> checkValueList = getDiscreteSortedCheckList(nodeDataList, checkIndex);
                    //Calculate error
                    for (int j = 0; j < checkValueList.size() - 1; j++) 
                    {
                        ArrayList<double[]> leftNodeData = new ArrayList();
                        ArrayList<double[]> rightNodeData = new ArrayList();
                        ArrayList<Double> splitValueList = new ArrayList();
                        for (int k = 0; k <= j; k++) 
                        {
                            splitValueList.add(checkValueList.get(k));
                        }
                        SplitDiscreteData(nodeDataList, leftNodeData, rightNodeData, splitValueList, checkIndex);
                        Node leftNode = new Node(leftNodeData);
                        Node rightNode = new Node(rightNodeData);
                        double leftImpurity = leftNode.getImpurity();
                        double rightImpurity = rightNode.getImpurity();
                        double nodeImpurity = (leftNode.getDataSize()*leftImpurity + rightNode.getDataSize()*rightImpurity)/((double)nodeDataList.size());
                        double descreasedImpurity = parent.getImpurity() - nodeImpurity;
                        if (descreasedImpurity > maxDecreasedImpurity) 
                        {
                            maxLeftNode = leftNode;
                            maxRightNode = rightNode;
                            maxDecreasedImpurity = descreasedImpurity;
                            minSplitValueList = splitValueList;
                            maxSplitIndex = checkIndex;
                        }
                    }
                }
            }
            //assign the best split
            if( (maxLeftNode!=null)&&(maxRightNode!=null) )
            {
                parent.setLeftNode(maxLeftNode);
                curIndex++;
                maxLeftNode.setIndex(curIndex);
                parent.setRightNode(maxRightNode);
                curIndex++;
                maxRightNode.setIndex(curIndex);
                parent.setSplitIndex(maxSplitIndex);
                parent.setSplitValue(maxSplitValue);
                parent.setSplitValueList(minSplitValueList);
                parent.setIsLeaf(false);
                //check if continute to split
                if (!CheckIsLeaf(parent.getLeftNode())) 
                {
                    RecursiveSplit(parent.getLeftNode());
//                    parent.getLeftNode().setIsLeaf(false);
                } 
                else 
                {
                    parent.getLeftNode().setIsLeaf(true);
                }
                if (!CheckIsLeaf(parent.getRightNode())) 
                {
                    RecursiveSplit(parent.getRightNode());
//                    parent.getRightNode().setIsLeaf(false);
                } 
                else 
                {
                    parent.getRightNode().setIsLeaf(true);
                }
            }
            else
            {
                parent.setIsLeaf(true);
            }
        }
        else
        {
            parent.setIsLeaf(true);
        }
    }
    
    public void CreateTree()
    {
        RecursiveSplit(root);
//        oobError = OOBEstimate();
    }
    
    public double getOOBError()
    {
        return oobError;
    }
    
    private double RecursivePredict(double[] inputValue, Node parent)
    {
        if(parent.isIsLeaf())
        {
            return parent.getMajorityType();
        }
        else
        {
            int splitAttrM = parent.getSplitIndex();
            if (dataAttrTypeList.get(splitAttrM) == DataType.Discrete) 
            {
                ArrayList<Double> splitValueList = parent.getSplitValueList();
                if (splitValueList.contains(inputValue[splitAttrM])) 
                {
                    //Left node
                    return RecursivePredict(inputValue, parent.getLeftNode());
                } 
                else 
                {
                    //Right Node
                    return RecursivePredict(inputValue, parent.getRightNode());
                }
            } 
            else 
            {
                double splitValue = parent.getSplitValue();
                if (inputValue[splitAttrM] < splitValue) 
                {
                    //Left node
                    return RecursivePredict(inputValue, parent.getLeftNode());
                } 
                else 
                {
                    //Right Node
                    return RecursivePredict(inputValue, parent.getRightNode());
                }
            }
        }
    }
    
    public double PredictValue(double[] inputVector)
    {
        return RecursivePredict(inputVector, root);
    }
    
    private void CleanTree(Node node)
    {
        if(node.isIsLeaf())
            node.CleanNode();
        else
        {
            CleanTree(node.getLeftNode());
            CleanTree(node.getRightNode());
        }
    }
    
    public void CleanTree()
    {
        CleanTree(root);
        dataList.clear();
//        testList.clear();
    }
    
    //Pruning
    public void PrintTree(Node node)
    {
        System.out.println(node.toString());
        if(!node.isIsLeaf())
        {
            PrintTree(node.getLeftNode());
            PrintTree(node.getRightNode());
        }
    }
    
    private Node CopyTree(Node sourceNode)
    {
        Node node = sourceNode.CloneNode();
        if(!sourceNode.isIsLeaf())
        {
            node.setLeftNode(CopyTree(sourceNode.getLeftNode()));
            node.setRightNode(CopyTree(sourceNode.getRightNode()));
        }
        return node;
    }
    
    public Node CloneTree()
    {
        return CopyTree(root);
    }
    
//    private double OOBEstimate()
//    {
//        double oobErr = 0;
//        
//        for(int i=0; i<testList.size(); i++)
//        {
//            double[] testVector = testList.get(i);
//            double realValue = testVector[testVector.length-1];
//            double predictedValue = PredictValue(testVector);
//            oobErr = oobErr + Math.pow(realValue - predictedValue, 2);
//        }
//        oobErr = oobErr/((double)testList.size());
//        
//        return oobErr;
//    }
    
    public String SaveTree()
    {
        TreeSaverAndParser treeSaver = new TreeSaverAndParser(root);
        return treeSaver.getTreeString();
    }
    
    public static void main(String[] args) 
    {
        DataReader dataReader = new DataReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\DataModel\\MethyData.rf");
        ArrayList<Integer> dataAtrrTypeList = dataReader.getDataAttrTypeList();
        ArrayList<double[]> trainingList = dataReader.getDataList();
        Tree tree = new Tree(trainingList, dataAtrrTypeList, 9, 5, 10E-06);
        tree.CreateTree();
        System.out.println("OK!");
    }
}
