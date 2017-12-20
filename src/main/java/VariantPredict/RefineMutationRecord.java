/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Basic.Tools;
import Basic.TwoBitParser;
import java.io.File;


/**
 *
 * @author Ben
 */
public class RefineMutationRecord
{
    private MethylationMutationRecord methyMutRec;
    private TwoBitParser twoBitParser;
    private int sourceLen;
    private String refSeq, mutationSeq;
    
    public RefineMutationRecord(MethylationMutationRecord methyMutRec, TwoBitParser twoBitParser)
    {
        this.methyMutRec = methyMutRec;
        sourceLen = methyMutRec.getMutationSeq().length();
        this.twoBitParser = twoBitParser;
    }

    
    private void ExpandUpStreamSequence(int centerPos, int upStream)
    {
        try
        {
            int complementaryLen = upStream - centerPos;
            String complementarySeq;
            if(methyMutRec.getStrand() == 0)
                complementarySeq = twoBitParser.loadFragment(methyMutRec.getPosition() - 1 - upStream - complementaryLen, complementaryLen);
            else
            {
                complementarySeq = twoBitParser.loadFragment(methyMutRec.getPosition() - 1 + upStream + complementaryLen, complementaryLen);
                complementarySeq = Tools.reverseSequence(complementarySeq);
            }
            String tumorSeq = complementarySeq + methyMutRec.getMutationSeq();
            methyMutRec.setMutationSeq(tumorSeq);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void TruncatUpStreamSequence(int centerPos, int upStream)
    {
        int truncatLen = centerPos - upStream;
        String truncatSeq = methyMutRec.getMutationSeq().substring(truncatLen, methyMutRec.getMutationSeq().length());
        methyMutRec.setMutationSeq(truncatSeq);
    }
    
    private void ExpandDownStreamSequence(int centerPos, int downStream)
    {
        try
        {
            int complementaryLen = downStream - (sourceLen - 1 - centerPos);
            String complementarySeq;
            if(methyMutRec.getStrand() == 0)
                complementarySeq = twoBitParser.loadFragment(methyMutRec.getPosition() - 1 + downStream + complementaryLen, complementaryLen);
            else
            {
                complementarySeq = twoBitParser.loadFragment(methyMutRec.getPosition() - 1 - downStream - complementaryLen, complementaryLen);
                complementarySeq = Tools.reverseSequence(complementarySeq);
            }
            String tumorSeq = methyMutRec.getMutationSeq() + complementarySeq;
            methyMutRec.setMutationSeq(tumorSeq);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void TruncatDownStreamSequence(int centerPos, int downStream)
    {
        int truncatLen = sourceLen - 1 - centerPos - downStream;
        String truncatSeq = methyMutRec.getMutationSeq().substring(0, methyMutRec.getMutationSeq().length() - truncatLen);
        methyMutRec.setMutationSeq(truncatSeq);
    }
    
    private void FormatMutationSequence(int centerPos, int upStream, int downStream)
    {
        int curUpStreamLen = centerPos;
        int curDownStreamLen = methyMutRec.getMutationSeq().length() - 1 - centerPos;
        //up stream
        if(curUpStreamLen < upStream)
            ExpandUpStreamSequence(centerPos, upStream);
        else if(curUpStreamLen > upStream)
            TruncatUpStreamSequence(centerPos, upStream);
        //Down stream
        if(curDownStreamLen < downStream)
            ExpandDownStreamSequence(centerPos, downStream);
        else if(curDownStreamLen > downStream)
            TruncatDownStreamSequence(centerPos, downStream);
        
        if(methyMutRec.getMutationSeq().length() != (upStream + 1 + downStream))
            System.out.println(methyMutRec.getChromosome() + "\t" + methyMutRec.getPosition() + "\t" + methyMutRec.getStrand() + "\t" + methyMutRec.getReferenceSeq() + "\t" + methyMutRec.getMutationSeq() + "\t" + refSeq + "\t" + mutationSeq);
    }
    
    public void Format(int upStream, int downStream)
    {
//        int centerPos = FindMethySite(upStream, downStream);
        FormatMutationSequence(methyMutRec.getMutM6APosition(), upStream, downStream);
    }
    
    public static void main(String[] args) {
        try
        {
            MethylationMutationRecord mmRec = new MethylationMutationRecord();
            mmRec.setReferenceSeq("AGACTGTTAACCAAGATGTTCATATCAACACAATAAACCTATTCCTCTGTGTGGCTTTTTT");
            mmRec.setMutationSeq("AGACTGTTAACCAAGATGTTCATATCAACGACACATAAACCTATTCCTCTGTGTGGCTTTTTT");
            mmRec.setPosition(235969746);
            mmRec.setStrand(1);
        
            TwoBitParser twoBitParser = new TwoBitParser(new File("G:\\Genome\\2bit\\hg19.2bit"));
            twoBitParser.setCurrentSequence("1");
            RefineMutationRecord refineMut = new RefineMutationRecord(mmRec, twoBitParser);
            refineMut.Format(30, 30);
            twoBitParser.close();
            twoBitParser.closeParser();
            
            System.out.println(mmRec.getReferenceSeq());
            System.out.println(mmRec.getMutationSeq());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
//        MethylationMutationRecord mmRec = new MethylationMutationRecord();
//        mmRec.setReferenceSeq("AACCATTACAAGATA");
//        mmRec.setMutationSeq("AAAAAACAAGATA");
//        RefineMutationRecord refMMRec = new RefineMutationRecord(mmRec, null);
//        int pos = refMMRec.FindMethySite(7, 7);
//        System.out.println(pos);
    }
}
