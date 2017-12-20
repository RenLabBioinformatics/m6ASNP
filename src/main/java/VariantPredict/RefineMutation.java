/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Basic.TwoBitParser;
import Basic.MAF.Chromosome;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class RefineMutation {
    private HashMap<String, LinkedList<MethylationMutationRecord>> methyMutationRecMap;
    private TwoBitParser twoBitParser;
    private Chromosome chromosome;
    
    public RefineMutation(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutationRecMap, String genomeFile)
    {
        this.methyMutationRecMap = methyMutationRecMap;
        chromosome = new Chromosome();
        try
        {
            twoBitParser = new TwoBitParser(new File(genomeFile));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void RefineAll(int upStream, int downStream)
    {
        try
        {
            for(String sampleID : methyMutationRecMap.keySet())
            {
                LinkedList<MethylationMutationRecord> mmRecList = methyMutationRecMap.get(sampleID);
                for(MethylationMutationRecord mmRec : mmRecList)
                {
                    twoBitParser.setCurrentSequence(chromosome.ConvertUCSCToEnsembl(mmRec.getChromosome()));
                    RefineMutationRecord refineMutationRec = new RefineMutationRecord(mmRec, twoBitParser);
                    refineMutationRec.Format(upStream, downStream);
                    twoBitParser.close();
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void CloseGenome()
    {
        try
        {
            twoBitParser.closeParser();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
