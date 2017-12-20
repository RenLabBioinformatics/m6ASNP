/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Annotation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *Reading the UCSC annotation file
 * @author Ben
 */
public class UCSCAnnoReader 
{
    private HashMap<String, LinkedList<UCSCAnnoRecord>> ucscAnnoMap;
    private HashMap<String, UCSCAnnoRecord> ucscAnnoIDMap;
    
    public UCSCAnnoReader(String ucscAnnoFile)
    {
        ucscAnnoMap = new HashMap();
        ucscAnnoIDMap = new HashMap();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(ucscAnnoFile));
            String strLine;
            String[] strArr;
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("#"))
                    continue;
                strArr = strLine.split("\t");
                UCSCAnnoRecord ucscAnnoRec = new UCSCAnnoRecord();
                ucscAnnoRec.setChromosome(strArr[0]);
                ucscAnnoRec.setTranscriptStart(Integer.parseInt(strArr[1]));
                ucscAnnoRec.setTranscriptEnd(Integer.parseInt(strArr[2]));
                ucscAnnoRec.setUCSCID(strArr[3]);
                ucscAnnoRec.setGeneSymbol(strArr[4]);
                if(strArr[6].equals("+"))
                    ucscAnnoRec.setStrand(0);
                else if(strArr[6].equals("-"))
                    ucscAnnoRec.setStrand(1);
                else
                    ucscAnnoRec.setStrand(1);
                ucscAnnoRec.setCdsStart(Integer.parseInt(strArr[9]));
                ucscAnnoRec.setCdsEnd(Integer.parseInt(strArr[10]));
                String[] exonStartArr = strArr[11].split(",");
                String[] exonEndArr = strArr[12].split(",");
                for(int i=0; i<exonStartArr.length; i++)
                {
                    Exon exon = new Exon();
                    exon.setStartPos(Integer.parseInt(exonStartArr[i]));
                    exon.setEndPos(Integer.parseInt(exonEndArr[i]));
                    ucscAnnoRec.addExon(exon);
                }
                LinkedList<UCSCAnnoRecord> ucscList;
                if(ucscAnnoMap.containsKey(ucscAnnoRec.getChromosome()))
                {
                    ucscList = ucscAnnoMap.get(ucscAnnoRec.getChromosome());
                    ucscList.add(ucscAnnoRec);
                }
                else
                {
                    ucscList = new LinkedList();
                    ucscList.add(ucscAnnoRec);
                    ucscAnnoMap.put(ucscAnnoRec.getChromosome(), ucscList);
                }
                ucscAnnoIDMap.put(ucscAnnoRec.getUCSCID(), ucscAnnoRec);
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public HashMap<String, LinkedList<UCSCAnnoRecord>> getUcscAnnoMap() 
    {
        return ucscAnnoMap;
    }
    
    public HashMap<String, UCSCAnnoRecord> getUCSCAnnoIDMap()
    {
        return ucscAnnoIDMap;
    }
    
    public static void main(String[] args) {
        UCSCAnnoReader ucscReader = new UCSCAnnoReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\SNP\\mm10.kg.Canonical.txt");
        ucscReader.getUcscAnnoMap();
    }
}
