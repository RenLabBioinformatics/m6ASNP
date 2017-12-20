/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Annotation.Exon;
import Annotation.UCSCAnnoRecord;
import Basic.Tools;
import Basic.TwoBitParser;
import Basic.VCF.CompareVCFRecord;
import Basic.VCF.VCFRecord;
import Basic.VCF.VCFSequencePair;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class ExtractSequencePair 
{
    private TwoBitParser twoBitParser;
    
    public ExtractSequencePair(String genomeFilePath)
    {
        try
        {
            twoBitParser = new TwoBitParser(new File(genomeFilePath));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private LinkedList<VCFRecord> getRelatedMethMutationList(int position, int upStream, int downStream, int strand, LinkedList<VCFRecord> vcfRecList)
    {
        LinkedList<VCFRecord> retList = new LinkedList();
        for(VCFRecord vcfRec : vcfRecList)
        {
            int mutStart = vcfRec.getPosition() - 1;
            int mutEnd = mutStart + vcfRec.getRefBase().length();
            if(strand == 0)
            {
                if ( (mutStart > (position - upStream))&&(mutStart < (position + downStream))&&(mutEnd > (position - upStream))&&(mutEnd < (position + downStream)) )
                    retList.add(vcfRec);
            }
            else
            {
                if ( (mutStart > (position - downStream))&&(mutStart < (position + upStream))&&(mutEnd > (position - downStream))&&(mutEnd < (position + upStream)) )
                    retList.add(vcfRec);
            }
        }
        return retList;
    }

    private int SequenceReplace(StringBuilder mutatedSeq, int position, String refCode, String mutCode, int shiftCount)
    {
        if(mutatedSeq.substring(position, position + refCode.length()).equals(refCode))
        {
            int refCodeLen = 0, mutCodeLen = 0;
            if(Tools.isNucleotideSequence(refCode))
            {
                mutatedSeq.delete(position, position + refCode.length());
                refCodeLen = refCode.length();
            }
            if(Tools.isNucleotideSequence(mutCode))
            {
                mutatedSeq.insert(position, mutCode);
                mutCodeLen = mutCode.length();
            }
            shiftCount = shiftCount + (mutCodeLen - refCodeLen);
        }
        else
            System.out.println("Skip conflicting reference code for " + refCode);
        return shiftCount;
    }

    private void getMutatedSequence(String referenceSeq, int refChrStart, int refChrEnd, int strand, int upStream, LinkedList<VCFRecord> relatedMutationList, VCFSequencePair vcfSeqPair)
    {
        if(strand != 0)
            referenceSeq = Tools.reverseSequence(referenceSeq);
        StringBuilder mutatedSeq = new StringBuilder(referenceSeq);
        int shiftCount = 0;
        int m6APosition = upStream;
        int mutatedM6APosition = m6APosition;
        Collections.sort(relatedMutationList, new CompareVCFRecord(strand));
        for(VCFRecord vcfRec : relatedMutationList)
        {
            int mutationPosition;
            if(strand == 0)
            {
                mutationPosition = vcfRec.getPosition() - 1 - refChrStart;
                if( (mutationPosition + shiftCount >= 0 ) && (mutationPosition + shiftCount + vcfRec.getRefBase().length() < mutatedSeq.length()) )
                    shiftCount = SequenceReplace(mutatedSeq, mutationPosition + shiftCount, vcfRec.getRefBase(), vcfRec.getMutBase(), shiftCount);
                else
                    System.out.println("Coordinate conflicting! Skip mutation: " + vcfRec.getChromosome() + ":" + vcfRec.getPosition() + "  " + vcfRec.getRefBase() + " -> " + vcfRec.getMutBase());
            }
            else
            {
                mutationPosition = refChrEnd - (vcfRec.getPosition() - 1);
                if(Tools.isNucleotideSequence(vcfRec.getMutBase()))
                    mutationPosition = mutationPosition - (vcfRec.getMutBase().length() - 1);
                if( (mutationPosition + shiftCount >= 0 ) && (mutationPosition + shiftCount + vcfRec.getRefBase().length() < mutatedSeq.length()) )
                    shiftCount = SequenceReplace(mutatedSeq, mutationPosition + shiftCount, Tools.reverseSequence(vcfRec.getRefBase()), Tools.reverseSequence(vcfRec.getMutBase()), shiftCount);
                else
                    System.out.println("Coordinate conflicting! Skip mutation: " + vcfRec.getChromosome() + ":" + vcfRec.getPosition() + "  " + vcfRec.getRefBase() + " -> " + vcfRec.getMutBase());
            }
            if( mutationPosition < m6APosition )
                mutatedM6APosition = m6APosition + shiftCount;
        }
        vcfSeqPair.setRefSequence(referenceSeq);
        vcfSeqPair.setMutSequence(mutatedSeq.toString());
        vcfSeqPair.setRefM6APosition(m6APosition);
        vcfSeqPair.setMutM6APosition(mutatedM6APosition);
    }

    public LinkedList<VCFSequencePair> Extract(UCSCAnnoRecord ucscAnnoRec, LinkedList<VCFRecord> vcfRecList, int upStream, int downStream)
    {
        try
        {
            LinkedList<VCFSequencePair> seqPairList = new LinkedList<VCFSequencePair>();
            twoBitParser.setCurrentSequence(vcfRecList.getFirst().getChromosome());
            LinkedList<Exon> exonList = ucscAnnoRec.getExonList();
            for(Exon exon : exonList)
            {
                twoBitParser.reset();
                String exonSeq = twoBitParser.loadFragment(exon.getStartPos(), exon.getEndPos() - exon.getStartPos() + 1);
                if(ucscAnnoRec.getStrand() == 0)
                {
                    //sense strand
                    for(int i=0; i<exonSeq.length() - 1; i++)
                    {
                        if( (exonSeq.charAt(i) == 'A') && (exonSeq.charAt(i+1) == 'C') )
                        {
                            LinkedList<VCFRecord> relatedMutationList = getRelatedMethMutationList(exon.getStartPos() + i, upStream, downStream, ucscAnnoRec.getStrand(), vcfRecList);
                            if(relatedMutationList.size() > 0)
                            {
                                VCFSequencePair vcfSeqPair = new VCFSequencePair();
                                vcfSeqPair.setM6AChrPosition(exon.getStartPos() + i + 1);//0-base to 1-base
                                vcfSeqPair.setRelatedMutationList(relatedMutationList);
                                twoBitParser.reset();
                                String referenceSeq = twoBitParser.loadFragment(exon.getStartPos() + i - upStream, upStream + 1 + downStream);
                                getMutatedSequence(referenceSeq, exon.getStartPos() + i - upStream, exon.getStartPos() + i + downStream, ucscAnnoRec.getStrand(), upStream, relatedMutationList, vcfSeqPair);
                                seqPairList.add(vcfSeqPair);
                            }
                        }
                    }
                }
                else
                {
                    //anti-sense strand
                    for(int i=0; i<exonSeq.length() - 1; i++)
                    {
                        if( (exonSeq.charAt(i) == 'G') && (exonSeq.charAt(i+1) == 'T') )
                        {
                            LinkedList<VCFRecord> relatedMutationList = getRelatedMethMutationList(exon.getStartPos() + i + 1, downStream, upStream, ucscAnnoRec.getStrand(), vcfRecList);
                            if(relatedMutationList.size() > 0)
                            {
                                VCFSequencePair vcfSeqPair = new VCFSequencePair();
                                vcfSeqPair.setM6AChrPosition(exon.getStartPos() + i + 1 + 1);//0-base to 1-base
                                vcfSeqPair.setRelatedMutationList(relatedMutationList);
                                String referenceSeq = twoBitParser.loadFragment(exon.getStartPos() + i + 1 - downStream, upStream + 1 + downStream);
                                getMutatedSequence(referenceSeq, exon.getStartPos() + i + 1 - downStream, exon.getStartPos() + i + 1 + upStream, ucscAnnoRec.getStrand(), downStream, relatedMutationList, vcfSeqPair);
                                seqPairList.add(vcfSeqPair);
                            }
                        }
                    }
                }
            }
            twoBitParser.close();
            return seqPairList;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
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
