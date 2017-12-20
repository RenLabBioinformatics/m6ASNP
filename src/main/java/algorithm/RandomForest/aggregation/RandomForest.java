/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithm.RandomForest.aggregation;

import algorithm.RandomForest.DataReader;
import algorithm.RandomForest.Tree.Tree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public class RandomForest {
    private ArrayList<Tree> treeList = new ArrayList();
    private ArrayList<Integer> dataAttrTypeList;
    private HashMap<Double, Integer> classType = new HashMap();
    
    public RandomForest(ArrayList<double[]> trainingDataList, ArrayList<Integer> dataAttrTypeList, int treeNumber, int splitNum, int minNodeNum, double minNodeError)
    {
        int inSplitNum;
        if(splitNum < 1)
            inSplitNum = (int)Math.sqrt(trainingDataList.get(0).length - 1) + 1;
        else
            inSplitNum = splitNum;
        for(int i=1; i<=treeNumber; i++)
        {
            Tree tree = new Tree(trainingDataList, dataAttrTypeList, inSplitNum, minNodeNum, minNodeError);
            tree.CreateTree();
            tree.CleanTree();
            treeList.add(tree);
//            System.out.println("Constructed tree " + i);
        }
        this.dataAttrTypeList = dataAttrTypeList;
    }
    
    public RandomForest(File forestModelFile)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(forestModelFile));
            String strLine;
            String[] strArr;
            String treeStr = "";
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("@DataType"))
                {
                    dataAttrTypeList = new ArrayList();
                    strArr = strLine.split("\t");
                    for(int i=1; i<strArr.length; i++)
                        dataAttrTypeList.add(Integer.parseInt(strArr[i]));
                }
                else if(strLine.startsWith("@Tree") || strLine.startsWith("@End"))
                {
                    if(!treeStr.isEmpty())
                    {
                        Tree tree = new Tree(treeStr, dataAttrTypeList);
                        treeList.add(tree);
                        treeStr = "";
                    }
                }
                else
                {
                    treeStr = treeStr + strLine + "\n";
                }
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }        
    }
    
    public RandomForest(String forestModelPath)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(RandomForest.class.getResourceAsStream(forestModelPath)));
            String strLine;
            String[] strArr;
            String treeStr = "";
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("@DataType"))
                {
                    dataAttrTypeList = new ArrayList();
                    strArr = strLine.split("\t");
                    for(int i=1; i<strArr.length; i++)
                        dataAttrTypeList.add(Integer.parseInt(strArr[i]));
                }
                else if(strLine.startsWith("@Tree") || strLine.startsWith("@End"))
                {
                    if(!treeStr.isEmpty())
                    {
                        Tree tree = new Tree(treeStr, dataAttrTypeList);
                        treeList.add(tree);
                        treeStr = "";
                    }
                }
                else
                {
                    treeStr = treeStr + strLine + "\n";
                }
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public double PredictValue(double[] inputVector)
    {
        classType.clear();
        double maxClassIndex = 0;
        int maxCount = 0, curCount = 0;
        double predictClassIndex;
        for(int i=0; i<treeList.size(); i++)
        {
            predictClassIndex = treeList.get(i).PredictValue(inputVector);
            if(!classType.containsKey(predictClassIndex))
                classType.put(predictClassIndex, 1);
            else
            {
                curCount = classType.get(predictClassIndex) + 1;
                classType.put(predictClassIndex, curCount);
            }
        }
        //get majority class
        maxCount = 0;
        for(double classIndex : classType.keySet())
        {
            if(classType.get(classIndex) > maxCount)
            {
                maxCount = classType.get(classIndex);
                maxClassIndex = classIndex;
            }
        }
        return maxClassIndex;
    }
    
    public double getScore(double classIndex)
    {
        int count = classType.get(classIndex);
        return (double)count/(double)treeList.size();
    }
    
    public void SaveForest(String savePath)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            String treeStr;
            
            fw.write("@DataType\t");
            for(int i=0; i<dataAttrTypeList.size()-1; i++)
            {
                fw.write(dataAttrTypeList.get(i) + "\t");
            }
            fw.write(dataAttrTypeList.get(dataAttrTypeList.size()-1) + "\n");
            
            
            for(int i=0; i<treeList.size(); i++)
            {
                Tree tree = treeList.get(i);
                treeStr = tree.SaveTree();
                fw.write("@Tree" + i + "\n");
                fw.write(treeStr + "\n");
            }
            
            fw.write("@End\n");
            
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void CleanForest()
    {
        for(int i=0; i<treeList.size(); i++)
            treeList.get(i).CleanTree();
    }
    
    public static void main(String[] args) 
    {
        //Training
        DataReader dataReader = new DataReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\MethylationDataMouse_Part.rf");
        ArrayList<Integer> dataAtrrTypeList = dataReader.getDataAttrTypeList();
        ArrayList<double[]> trainingList = dataReader.getDataList();
        RandomForest rf = new RandomForest(trainingList, dataAtrrTypeList, 100, 0, 1000, 10E-06);
        rf.SaveForest("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\MethylationRFModelMouse.txt");
        System.out.println("OK!");
    }
}
