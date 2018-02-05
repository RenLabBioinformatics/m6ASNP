package Validation;

import FASTA.Fasta;
import FASTA.FastaReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2018/1/22.
 */
public class RNAMethylValidation
{
    private HashMap<String, Double> positivePreResMap;
    private HashMap<String, Double> negativePreResMap;
    private ArrayList<Performance> performanceList;
    private enum dataType {Positive, Negative};

    private void ReadSourceData(File inputFile, dataType type)
    {
        FastaReader fastaReader = new FastaReader();
        fastaReader.ReadFromFile(inputFile.getAbsolutePath());
        LinkedList<Fasta> fasList = fastaReader.getFastaList();
        for (Fasta fas : fasList)
        {
            if (type == dataType.Positive)
                positivePreResMap.put(fas.getFastaName(), 0D);
            else if (type == dataType.Negative)
                negativePreResMap.put(fas.getFastaName(), 0D);
            else
                System.out.println("Enum type error!");
        }
    }

    public RNAMethylValidation(File positiveDataFile, File negativeDataFile)
    {
        positivePreResMap = new HashMap<>();
        negativePreResMap = new HashMap<>();
        ReadSourceData(positiveDataFile, dataType.Positive);
        ReadSourceData(negativeDataFile, dataType.Negative);
    }

    private void CalResultFile(File resFile, int centerPos, dataType type)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(resFile));
            String strLine;
            String[] strArr;

            HashMap<String, Double> preResMap;
            if(type == dataType.Positive)
                preResMap = positivePreResMap;
            else if(type == dataType.Negative)
                preResMap = negativePreResMap;
            else
                preResMap = negativePreResMap;

            String seqName = "";
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.contains("nucleotides long and contains"))
                {
                    strArr = strLine.split("\\s+");
                    seqName = strArr[0];
                }
                else if(strLine.contains("The A"))
                {
                    strArr = strLine.split("\\s+");
                    int seqPos = Integer.parseInt(strArr[4]);
                    if(seqPos == centerPos)
                    {
                        double score = 0;
                        String scoreStr = strArr[17].substring(0, strArr[17].length() - 1);
                        if(strArr[12].equals("methylated"))
                            score = Double.parseDouble(scoreStr);
                        else if(strArr[12].equals("unmethylated"))
                            score = 1 - Double.parseDouble(scoreStr);
                        else
                            System.out.println("Unknown status: " + strArr[12]);
                        if(preResMap.containsKey(seqName))
                            preResMap.put(seqName, score);
                        else
                            System.out.println("Unknown sequence name " + seqName);
                    }
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Validation(File positiveResFile, File negativeResFile, int centerPos)
    {
        CalResultFile(positiveResFile, centerPos, dataType.Positive);
        CalResultFile(negativeResFile, centerPos, dataType.Negative);

        ArrayList<Double> positiveScoreList = new ArrayList<>();
        ArrayList<Double> negativeScoreList = new ArrayList<>();
        for(String key : positivePreResMap.keySet())
        {
            positiveScoreList.add(positivePreResMap.get(key));
        }
        for(String key : negativePreResMap.keySet())
        {
            negativeScoreList.add(negativePreResMap.get(key));
        }

        Validation validation = new Validation(positiveScoreList, negativeScoreList);
        validation.Validate();
        performanceList = validation.getPerformanceList();
        System.out.println("AUC = " + validation.CalculateAUC());
    }

    public ArrayList<Performance> getPerformanceList() {
        return performanceList;
    }

    public static void main(String[] args)
    {
        RNAMethylValidation rnaMethylValidation = new RNAMethylValidation(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\PositiveMouse.fas"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\NegativeMouse.fas"));
        rnaMethylValidation.Validation(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\PositiveMouse_m6APre_Res.txt"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\NegativeMouse_m6APre_Res.txt"),
                31);
        ArrayList<Performance> perfList = rnaMethylValidation.getPerformanceList();
        for(Performance perf : perfList)
            System.out.println(perf);
    }
}
