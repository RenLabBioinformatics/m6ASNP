/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class MethylationMutationResultReader {
    private HashMap<String, LinkedList<MethylationMutationRecord>> methyMutationMap;
    
    public MethylationMutationResultReader(String mutationResultFile)
    {
        methyMutationMap = new HashMap();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(mutationResultFile));
            String strLine = br.readLine();
            String[] strArr;
            while(br.ready())
            {
                strLine = br.readLine();
                strArr = strLine.split("\t");
                MethylationMutationRecord methyMutRec = new MethylationMutationRecord();
                methyMutRec.setUcscID(strArr[1]);
                methyMutRec.setGeneSymbol(strArr[2]);
                methyMutRec.setChromosome(strArr[3]);
                methyMutRec.setPosition(Integer.parseInt(strArr[4]));
                if(strArr[5].equals("+"))
                    methyMutRec.setStrand(0);
                else
                    methyMutRec.setStrand(1);
                methyMutRec.setReferenceSeq(strArr[6]);
                methyMutRec.setMutationSeq(strArr[7]);
                methyMutRec.setReferenceScore(Double.parseDouble(strArr[8]));
                methyMutRec.setMutationScore(Double.parseDouble(strArr[9]));
                if(!strArr[10].equals("."))
                    methyMutRec.setChangeSig(Double.parseDouble(strArr[10]));
                if(strArr[11].equals("Non-methylation not change"))
                    methyMutRec.setMutationEvent(MutationEvent.NonMethylationNotChange);
                else if(strArr[11].equals("Methylation not change"))
                    methyMutRec.setMutationEvent(MutationEvent.MethylationNotChange);
                else if(strArr[11].equals("Functional Loss"))
                    methyMutRec.setMutationEvent(MutationEvent.FunctionalLoss);
                else if(strArr[11].equals("Functional Gain"))
                    methyMutRec.setMutationEvent(MutationEvent.FunctionalGain);
                LinkedList<MethylationMutationRecord> methyRecList;
                if(methyMutationMap.containsKey(strArr[0]))
                {
                    methyRecList = methyMutationMap.get(strArr[0]);
                    methyRecList.add(methyMutRec);
                }
                else
                {
                    methyRecList = new LinkedList();
                    methyRecList.add(methyMutRec);
                    methyMutationMap.put(strArr[0], methyRecList);
                }
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public HashMap<String, LinkedList<MethylationMutationRecord>> getMethyMutationMap() {
        return methyMutationMap;
    }
}
