/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Validation;

import algorithm.RandomForest.DataReader;
import algorithm.RandomForest.aggregation.RandomForest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Ben
 */
public class Validation {
    private ArrayList<double[]> positiveDataList;
    private ArrayList<double[]> negativeDataList;
    private ArrayList<Integer> dataAttrTypeList;
    private ArrayList<Double> positiveScoreList = new ArrayList();
    private ArrayList<Double> negativeScoreList = new ArrayList();
    private RandomForest randomForest;
    private double maxError;
    private int splitNum;
    private int treeNum;
    private int minNodeNum;
    private ArrayList<Performance> performanceList = new ArrayList();
    
    public Validation(ArrayList<Double> positiveScoreList, ArrayList<Double> negativeScoreList)
    {
        this.positiveScoreList = positiveScoreList;
        this.negativeScoreList = negativeScoreList;
    }
    
    public Validation(ArrayList<double[]> trainingList, ArrayList<Integer> dataAttrTypeList, int treeNum, int splitNum, int minNodeNum, double maxError)
    {
        positiveDataList = new ArrayList();
        negativeDataList = new ArrayList();
        for(double[] dataValue : trainingList)
        {
            if(dataValue[dataValue.length - 1] == 1)
                positiveDataList.add(dataValue);
            else
                negativeDataList.add(dataValue);
        }
        this.dataAttrTypeList = dataAttrTypeList;
        this.maxError = maxError;
        this.splitNum = splitNum;
        this.treeNum = treeNum;
        this.minNodeNum = minNodeNum;
    }
    
    public void Validate()
    {
        FullPerformance();
    }
    
    public void Self()
    {
        positiveScoreList.clear();
        negativeScoreList.clear();
        ArrayList<double[]> trainingSet = new ArrayList();
        for(int i=0; i<positiveDataList.size(); i++)
            trainingSet.add(positiveDataList.get(i));
        for(int i=0; i<negativeDataList.size(); i++)
            trainingSet.add(negativeDataList.get(i));
        
        randomForest = new RandomForest(trainingSet, dataAttrTypeList, treeNum, splitNum, minNodeNum, maxError);
        double classIndex, score;
        for(int i=0; i<positiveDataList.size(); i++)
        {
            classIndex = randomForest.PredictValue(positiveDataList.get(i));
            if(classIndex == 1)
                score = randomForest.getScore(1);
            else
                score = 1 - randomForest.getScore(2);
            positiveScoreList.add(score);
        }
        for(int i=0; i<negativeDataList.size(); i++)
        {
            classIndex = randomForest.PredictValue(negativeDataList.get(i));
            if(classIndex == 1)
                score = randomForest.getScore(1);
            else
                score = 1 - randomForest.getScore(2);
            negativeScoreList.add(score);
        }
        randomForest.CleanForest();
        FullPerformance();
    }
    
    public void LeaveOneOut()
    {
        positiveScoreList.clear();
        negativeScoreList.clear();
        double[] testSet = null;
        ArrayList<double[]> trainingSet = new ArrayList();
        for(int i=0; i<positiveDataList.size(); i++)
        {
            trainingSet.clear();
            for(int j=0; j<positiveDataList.size(); j++)
            {
                if(i != j)
                    trainingSet.add(positiveDataList.get(j));
                else
                    testSet = positiveDataList.get(j);
            }
            for(int j=0; j<negativeDataList.size(); j++)
            {
                trainingSet.add(negativeDataList.get(j));
            }
            
            randomForest = new RandomForest(trainingSet, dataAttrTypeList, treeNum, splitNum, minNodeNum, maxError);
            double classIndex = randomForest.PredictValue(testSet);
            double score;
            if(classIndex == 1)
                score = randomForest.getScore(1);
            else
                score = 1 - randomForest.getScore(2);
            positiveScoreList.add(score);
            randomForest.CleanForest();
        }
        
        testSet = null;
        for(int i=0; i<negativeDataList.size(); i++)
        {
            trainingSet.clear();
            for(int j=0; j<positiveDataList.size(); j++)
            {
                trainingSet.add(positiveDataList.get(j));
            }
            for(int j=0; j<negativeDataList.size(); j++)
            {
                if( j != i)
                    trainingSet.add(negativeDataList.get(j));
                else
                    testSet = negativeDataList.get(j);
            }
            
            randomForest = new RandomForest(trainingSet, dataAttrTypeList, treeNum, splitNum, minNodeNum, maxError);
            double classIndex = randomForest.PredictValue(testSet);
            double score;
            if(classIndex == 1)
                score = randomForest.getScore(1);
            else
                score = 1 - randomForest.getScore(2);
            negativeScoreList.add(score);
            randomForest.CleanForest();
        }
        FullPerformance();
    }
    
    public void nFold(int n)
    {
        positiveScoreList.clear();
        negativeScoreList.clear();
        for(int i=1; i<=5; i++)
        {
//            System.out.println("Begin " + i + " iteration");
            Collections.shuffle(positiveDataList);
            Collections.shuffle(negativeDataList);
            //Positive
            ArrayList<double[]> trainingSet = new ArrayList();
            ArrayList<double[]> positiveTestSet = new ArrayList();
            int index = (int)(positiveDataList.size() * (1/(double)n));
            for(int j=0; j<=index; j++)
            {
                positiveTestSet.add(positiveDataList.get(j));
            }
            for(int j=index + 1; j<positiveDataList.size(); j++)
            {
                trainingSet.add(positiveDataList.get(j));
            }
            //Negative
            ArrayList<double[]> negativeTestSet = new ArrayList();
            index = (int)(negativeDataList.size() * (1/(double)n));
            for(int j=0; j<=index; j++)
            {
                negativeTestSet.add(negativeDataList.get(j));
            }
            for(int j=index+1; j<negativeDataList.size(); j++)
            {
                trainingSet.add(negativeDataList.get(j));
            }
            //Validation
            randomForest = new RandomForest(trainingSet, dataAttrTypeList, treeNum, splitNum, minNodeNum, maxError);
            double score, predictedClassIndex;
            for(double[] value : positiveTestSet)
            {
                predictedClassIndex = randomForest.PredictValue(value);
                if(predictedClassIndex == 1)
                    score = randomForest.getScore(1);
                else
                    score = 1 - randomForest.getScore(2);
                positiveScoreList.add(score);
            }
            for(double[] value : negativeTestSet)
            {
                predictedClassIndex = randomForest.PredictValue(value);
                if(predictedClassIndex == 1)
                    score = randomForest.getScore(1);
                else
                    score = 1 - randomForest.getScore(2);
                negativeScoreList.add(score);
            }
            randomForest.CleanForest();
//            System.out.println("Finish " + i + " iteration");
        }
        FullPerformance();
    }
    
    private void FullPerformance()
    {
        performanceList.clear();
        for(double cutoff = 0; cutoff <=1; cutoff = cutoff + 0.01)
        {
            int tp = 0, tn = 0, fp = 0, fn = 0;
            for(double score : positiveScoreList)
            {
                if(score >= cutoff)
                    tp++;
                else
                    fn++;
            }
            for(double score : negativeScoreList)
            {
                if(score >= cutoff)
                    fp++;
                else
                    tn++;
            }
            //Calculate Performance
            Performance perf = new Performance(tp, tn, fp, fn, cutoff);
            performanceList.add(perf);
        }
    }
    
    public ArrayList<Performance> getPerformanceList()
    {
        return performanceList;
    }
    
    public double CalculateAUC()
    {
        Collections.sort(performanceList, new ComparePerformance());
        double sum = 0;
        for(int i=0; i<performanceList.size()-1; i++)
        {
            Performance perfFore = performanceList.get(i);
            Performance perfBack = performanceList.get(i + 1);
            sum = sum + (perfFore.getSp() - perfBack.getSp())*((perfFore.getSn() + perfBack.getSn())/2);
        }
        return sum;
    }
    
    public static void main(String[] args) {
        DataReader dataReader = new DataReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\MethylationDataHuman_Part.rf");
        ArrayList<double[]> dataList = dataReader.getDataList();
        ArrayList<Integer> dataAtrrTypeList = dataReader.getDataAttrTypeList();
        Validation validation = new Validation(dataList, dataAtrrTypeList, 100, 0, 1000, 10E-06);
        System.out.println("Calculate N-fold validation...");
        validation.nFold(4);
        System.out.println("Finish N-fold validation...");
        ArrayList<Performance> perfList = validation.getPerformanceList();
        try
        {
            FileWriter fw = new FileWriter("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\ROC\\4Fold_Performance.txt");
            System.out.println("AUC = " + validation.CalculateAUC());
            for(Performance perf : perfList)
                fw.write(perf.getCutoff() + "\t" + perf.toString() + "\n");
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
