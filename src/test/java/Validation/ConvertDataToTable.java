package Validation;

import algorithm.SecondaryAlignment.NussinovAlignment;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2018/1/29.
 */
public class ConvertDataToTable
{
    private LinkedList<double[]> dataList = new LinkedList<>();
    private enum dataType {Positive, Negative};
    private enum saveType {R, Java};
    private String structure = "().";

    public ConvertDataToTable(File posFile, File negFile, File saveFile, saveType type)
    {
        Convert(posFile, dataType.Positive, -1);
        Convert(negFile, dataType.Negative, 30074);
        if(type == saveType.R)
            SaveTableForR(saveFile);
        else if(type == saveType.Java)
            SaveTableForJava(saveFile);
    }

    private double ConvertNucleotide(char nucl)
    {
        double retVal = 0;
        switch(nucl)
        {
            case 'A':
                retVal = 1;
                break;
            case 'T':
                retVal = 2;
                break;
            case 'C':
                retVal = 3;
                break;
            case 'G':
                retVal = 4;
                break;
            default:
                retVal = 0;
        }
        return retVal;
    }

    private void Convert(File sourceData, dataType type, int maxCount)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(sourceData));
            String strLine = br.readLine();
            NussinovAlignment nussinovAlignment = new NussinovAlignment();

            HashMap<String, Double> structSeqMap = new HashMap<>();
            for(int i=0; i<structure.length(); i++)
            {
                for(int j=0; j<structure.length(); j++)
                {
                    for(int k = 0; k <structure.length(); k++)
                    {
                        String key = "" + structure.charAt(i) + structure.charAt(j) + structure.charAt(k);
                        structSeqMap.put(key, 0D);
                    }
                }
            }

            int count = 0;
            while(br.ready())
            {
                strLine = br.readLine();
                String[] strArr = strLine.split("\t");
                String seq = strArr[4];
                double[] featureRec = new double[61 + 27 + 1];
                //Encode sequence feature
                for(int i=0; i<seq.length(); i++)
                {
                    double retVal = ConvertNucleotide(seq.charAt(i));
                    featureRec[i] = retVal;
                }
                //Encode secondary structure
                nussinovAlignment.setSequence(seq);
                nussinovAlignment.PredictSecondaryStructure();
                String structureSeq = nussinovAlignment.getStructure();
                for(String key : structSeqMap.keySet())
                {
                    structSeqMap.put(key, 0D);
                }
                double sum = 0;
                for(int i=0; i<seq.length() - 3; i++)
                {
                    String key = structureSeq.substring(i, i+3);
                    if(structSeqMap.containsKey(key))
                    {
                        double val = structSeqMap.get(key) + 1;
                        structSeqMap.put(key, val);
                        sum++;
                    }
                    else
                        System.out.println("Unknown key " + key);
                }
                for(String key : structSeqMap.keySet())
                {
                    double val = structSeqMap.get(key);
                    val = val / sum;
                    structSeqMap.put(key, val);
                }
                int index = 0;
                for(int i=0; i<structure.length(); i++)
                {
                    for(int j=0; j<structure.length(); j++)
                    {
                        for(int k = 0; k <structure.length(); k++)
                        {
                            String key = "" + structure.charAt(i) + structure.charAt(j) + structure.charAt(k);
                            featureRec[61 + index] = structSeqMap.get(key);
                            index++;
                        }
                    }
                }
                //Encode data type
                if(type == dataType.Positive)
                {
                    featureRec[88] = 1;
                }
                else if(type == dataType.Negative)
                {
                    featureRec[88] = 2;
                }
                else
                    System.out.println("Unknown data type");
                dataList.add(featureRec);
                //Exit when count number greater than the pre-set threshold
                count++;
                if(maxCount > 0)
                {
                    if(count > maxCount)
                        break;
                }
            }
            br.close();
            if(type == dataType.Positive)
                System.out.println("Read " + count + " positive data.");
            else if(type == dataType.Negative)
                System.out.println("Read " + count + " negative data.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void SaveTableForJava(File saveFile)
    {
        try
        {
            FileWriter fw = new FileWriter(saveFile);
            //Write data type
            fw.write("@DataType\t");
            for(int i=1; i<=61; i++)
                fw.write("Discr\t");
            for(int i=62; i<=88; i++)
                fw.write("Conti\t");
            fw.write("Discr\n");
            //Write data
            for(double[] rec : dataList)
            {
                for(int i=0; i<rec.length - 1; i++)
                    fw.write(rec[i] + "\t");
                fw.write(rec[rec.length - 1] + "\n");
            }
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void SaveTableForR(File saveFile)
    {
        try
        {
            FileWriter fw = new FileWriter(saveFile);
            //write title
            for(int i=0; i<dataList.getFirst().length - 1; i++)
                fw.write("F" + i + "\t");
            fw.write("type\n");
            //write data
            for(double[] rec : dataList)
            {
                //sequence
                for(int i=0; i<61; i++)
                {
                    switch ((int)(rec[i]))
                    {
                        case 1:
                            fw.write("A");
                            break;
                        case 2:
                            fw.write("T");
                            break;
                        case 3:
                            fw.write("C");
                            break;
                        case 4:
                            fw.write("G");
                            break;
                        default:
                            fw.write("A");
                            System.out.println("Error nucleotide type! Set to default!");
                    }
                    fw.write("\t");
                }
                //structure
                for(int i = 61; i<61+27; i++)
                {
                    fw.write(rec[i] + "\t");
                }
                //type
                if(rec[88] == 1)
                    fw.write("P\n");
                else if(rec[88] == 2)
                    fw.write("N\n");
                else
                    System.out.println("Error data type!");
            }
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        ConvertDataToTable convertDataToTable = new ConvertDataToTable(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\PositiveMouse.txt"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\NegativeMouse.txt"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\ForR\\Mouse_Train_Java.txt"),
                saveType.Java);
    }
}
