/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class BEDReader 
{
    private HashMap<String, LinkedList<BEDRecord>> bedRecMap = new HashMap();
    private HashMap<String, Integer> peakDistribution = new HashMap();
    
    public BEDReader(String bedFile)
    {
        peakDistribution.put("200", 0);
        peakDistribution.put("400", 0);
        peakDistribution.put("600", 0);
        peakDistribution.put("800", 0);
        peakDistribution.put("1000", 0);
        peakDistribution.put("1200", 0);
        peakDistribution.put("1400", 0);
        peakDistribution.put(">1400", 0);
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(bedFile));
            String strLine;
            String[] strArr;
            int curIndex = 0;
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("#"))
                    continue;
                strArr = strLine.split("\t");
                if(strArr.length >= 3)
                {
                    BEDRecord bedRec = new BEDRecord();
                    bedRec.setChrName(strArr[0]);
                    if(bedRec.getChrName().equals("M"))
                        bedRec.setChrName("MT");
                    bedRec.setStartPosition(Integer.parseInt(strArr[1]));
                    bedRec.setEndPosition(Integer.parseInt(strArr[2]));
                    if(strArr.length >= 4)
                        bedRec.setName(strArr[3]);
                    else
                        bedRec.setName("Unknown" + curIndex);
                    //Score
                    if(strArr.length >= 5)
                    {
                        if(strArr[4].equals(".")||strArr[4].isEmpty())
                            bedRec.setScore(0);
                        else
                            bedRec.setScore(Double.parseDouble(strArr[4]));
                    }
                    else
                        bedRec.setScore(0);
                    //Strand
                    if(strArr.length >= 6)
                    {
                        if(strArr[5].equals("+"))
                            bedRec.setStrand(0);
                        else if(strArr[5].equals("-"))
                            bedRec.setStrand(1);
                        else
                            bedRec.setStrand(-1);
                    }
                    else
                        bedRec.setStrand(-1);
                    //Put in map
                    if(bedRecMap.containsKey(bedRec.getChrName()))
                        bedRecMap.get(bedRec.getChrName()).add(bedRec);
                    else
                    {
                        LinkedList<BEDRecord> bedRecList = new LinkedList();
                        bedRecList.add(bedRec);
                        bedRecMap.put(bedRec.getChrName(), bedRecList);
                    }
                    curIndex++;
                    //Stat distribution
                    int length = bedRec.getEndPosition() - bedRec.getStartPosition() + 1;
                    int count = 0;
                    if(length <= 200)
                    {
                        count = peakDistribution.get("200") + 1;
                        peakDistribution.put("200", count);
                    }
                    else if( (length > 200)&&(length <= 400) )
                    {
                        count = peakDistribution.get("400") + 1;
                        peakDistribution.put("400", count);                    
                    }
                    else if( (length > 400)&&(length <= 600) )
                    {
                        count = peakDistribution.get("600") + 1;
                        peakDistribution.put("600", count);
                    }
                    else if( (length > 600)&&(length <= 800) )
                    {
                        count = peakDistribution.get("800") + 1;
                        peakDistribution.put("800", count);
                    }
                    else if( (length > 800)&&(length <= 1000) )
                    {
                        count = peakDistribution.get("1000") + 1;
                        peakDistribution.put("1000", count);
                    }
                    else if( (length > 1000)&&(length <= 1200) )
                    {
                        count = peakDistribution.get("1200") + 1;
                        peakDistribution.put("1200", count);
                    }
                    else if( (length > 1200)&&(length <= 1400) )
                    {
                        count = peakDistribution.get("1400") + 1;
                        peakDistribution.put("1400", count);
                    }
                    else
                    {
                        count = peakDistribution.get(">1400") + 1;
                        peakDistribution.put(">1400", count);
                    }
                }
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public HashMap<String, LinkedList<BEDRecord>> getBedRecMap() 
    {
        return bedRecMap;
    }
    
    public static void WriteBEDFile(LinkedList<BEDRecord> inBedList, String writeFile)
    {
        try
        {
            FileWriter fw = new FileWriter(writeFile);
            for (BEDRecord bedRec : inBedList) 
            {
                fw.write(bedRec.getChrName() + "\t" + bedRec.getStartPosition() + "\t" + bedRec.getEndPosition() + "\t" + bedRec.getName() + "\t" + bedRec.getScore() + "\t");
                if (bedRec.getStrand() == 0)
                    fw.write("+");
                else if (bedRec.getStrand() == 1)
                    fw.write("-");
                else
                    fw.write(".");
                fw.write("\n");
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void SavePeakLengthDistributionInPalin(String savePath)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            
            fw.write("200\t" + peakDistribution.get("200") + "\n");
            fw.write("400\t" + peakDistribution.get("400") + "\n");
            fw.write("600\t" + peakDistribution.get("600") + "\n");
            fw.write("800\t" + peakDistribution.get("800") + "\n");
            fw.write("1000\t" + peakDistribution.get("1000") + "\n");
            fw.write("1200\t" + peakDistribution.get("1200") + "\n");
            fw.write("1400\t" + peakDistribution.get("1400") + "\n");
            fw.write(">1400\t" + peakDistribution.get(">1400") + "\n");
            
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        BEDReader bedReader = new BEDReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\zuo\\IP.bed");
        bedReader.SavePeakLengthDistributionInPalin("G:\\云同步文件夹\\工作文档\\RNA-methylation\\zuo\\IP.length.txt");
    }
}
