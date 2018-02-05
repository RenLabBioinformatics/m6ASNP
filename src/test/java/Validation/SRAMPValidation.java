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
 * Created by Ben on 2018/1/20.
 */
public class SRAMPValidation
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

    public SRAMPValidation(File positiveDataFile, File negativeDataFile)
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
            String strLine = br.readLine();//skip title line
            String[] strArr;

            HashMap<String, Double> preResMap;
            if(type == dataType.Positive)
                preResMap = positivePreResMap;
            else if(type == dataType.Negative)
                preResMap = negativePreResMap;
            else
                preResMap = negativePreResMap;

            while(br.ready())
            {
                strLine = br.readLine();
                strArr = strLine.split("\t");
                String id = strArr[0];
                double score = Double.parseDouble(strArr[2]);
                int seqPos = Integer.parseInt(strArr[1]);

                if(preResMap.containsKey(id))
                {
                    if(seqPos == centerPos)
                    {
                        preResMap.put(id, score);
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
        SRAMPValidation srampValidation = new SRAMPValidation(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\PositiveMouse.fas"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\NegativeMouse.fas"));
        srampValidation.Validation(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\PositiveMouse_Res.txt"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\NegativeMouse_Res.txt"),
                31);
        ArrayList<Performance> perfList = srampValidation.getPerformanceList();
        for(Performance perf : perfList)
            System.out.println(perf);
    }
}
