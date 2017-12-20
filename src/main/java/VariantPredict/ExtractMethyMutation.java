/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Annotation.UCSCAnnoRecord;
import Basic.VCF.SampleVCFMutation;
import Basic.VCF.VCFRecord;
import Basic.VCF.VCFSequencePair;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class ExtractMethyMutation 
{
    private HashMap<String, UCSCAnnoRecord> ucscAnnoIDMap;
    private HashMap<String, SampleVCFMutation> vcfRecMap;
    private HashMap<String, LinkedList<MethylationMutationRecord>> methyMutationMap;
    private ExtractSequencePair extractSeqPair;
    
    public ExtractMethyMutation(HashMap<String, UCSCAnnoRecord> ucscAnnoIDMap, HashMap<String, SampleVCFMutation> vcfRecMap, String genomeFilePath)
    {
        this.ucscAnnoIDMap = ucscAnnoIDMap;
        this.vcfRecMap = vcfRecMap;
        this.methyMutationMap = new HashMap();
        extractSeqPair = new ExtractSequencePair(genomeFilePath);
    }
    
    public void Extract(int upStream, int downStream)
    {
        for(String sampleID : vcfRecMap.keySet())
        {
            SampleVCFMutation sampleMutation = vcfRecMap.get(sampleID);
            HashMap<String, LinkedList<VCFRecord>> geneMutationMap = sampleMutation.getVcfRecMap();
            LinkedList<MethylationMutationRecord> methyMutationList = new LinkedList();
            for(String key : geneMutationMap.keySet())
            {
                String[] strArr = key.split("\\|");
                String ucscID = strArr[0];
                String geneSymbol = strArr[1];
                UCSCAnnoRecord ucscAnnoRec = ucscAnnoIDMap.get(ucscID);
                LinkedList<VCFRecord> vcfRecList = geneMutationMap.get(key);
                LinkedList<VCFSequencePair> seqPairList = extractSeqPair.Extract(ucscAnnoRec, vcfRecList, upStream, downStream);
                for(VCFSequencePair seqPair : seqPairList)
                {
                    MethylationMutationRecord methMutationRec = new MethylationMutationRecord();
                    methMutationRec.setUcscID(ucscID);
                    methMutationRec.setGeneSymbol(geneSymbol);
                    methMutationRec.setChromosome(ucscAnnoRec.getChromosome());
                    methMutationRec.setPosition(seqPair.getM6AChrPosition());
                    methMutationRec.setReferenceSeq(seqPair.getRefSequence());
                    methMutationRec.setRefM6APosition(seqPair.getRefM6APosition());
                    methMutationRec.setMutationSeq(seqPair.getMutSequence());
                    methMutationRec.setMutM6APosition(seqPair.getMutM6APosition());
                    methMutationRec.setStrand(ucscAnnoRec.getStrand());
                    methMutationRec.setRelatedMutationList(seqPair.getRelatedMutationList());
                    methyMutationList.add(methMutationRec);
                }
            }
            methyMutationMap.put(sampleID, methyMutationList);
        }
    }

    public HashMap<String, LinkedList<MethylationMutationRecord>> getMethyMutationList() {
        return methyMutationMap;
    }
    
    public void CloseGenome()
    {
        extractSeqPair.CloseGenome();
    }
    
    public static void main(String[] args) {

    }
}
