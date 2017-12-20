/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Basic.VCF.VCFRecord;

import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class MethylationMutationRecord 
{
    private String ucscID;
    private String geneSymbol;
    private String chromosome;
    private int position;//Methylated position
    private int strand;
    private String referenceSeq;
    private int refM6APosition;
    private String mutationSeq;
    private int mutM6APosition;
    private double referenceScore;
    private double mutationScore;
    private double changeSig;
    private int mutationEvent;
    private LinkedList<VCFRecord> relatedMutationList;
    private double probability;

    public int getRefM6APosition() {
        return refM6APosition;
    }

    public void setRefM6APosition(int refM6APosition) {
        this.refM6APosition = refM6APosition;
    }

    public int getMutM6APosition() {
        return mutM6APosition;
    }

    public void setMutM6APosition(int mutM6APosition) {
        this.mutM6APosition = mutM6APosition;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getUcscID() {
        return ucscID;
    }

    public void setUcscID(String ucscID) {
        this.ucscID = ucscID;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public String getReferenceSeq() {
        return referenceSeq;
    }

    public void setReferenceSeq(String referenceSeq) {
        this.referenceSeq = referenceSeq;
    }

    public String getMutationSeq() {
        return mutationSeq;
    }

    public void setMutationSeq(String mutationSeq) {
        this.mutationSeq = mutationSeq;
    }

    public double getReferenceScore() {
        return referenceScore;
    }

    public void setReferenceScore(double referenceScore) {
        this.referenceScore = referenceScore;
    }

    public double getMutationScore() {
        return mutationScore;
    }

    public void setMutationScore(double mutationScore) {
        this.mutationScore = mutationScore;
    }

    public double getChangeSig() {
        return changeSig;
    }

    public void setChangeSig(double changeSig) {
        this.changeSig = changeSig;
    }

    public LinkedList<VCFRecord> getRelatedMutationList() {
        return relatedMutationList;
    }

    public void setRelatedMutationList(LinkedList<VCFRecord> relatedMutationList) {
        this.relatedMutationList = relatedMutationList;
    }

    public int getMutationEvent() {
        return mutationEvent;
    }

    public void setMutationEvent(int mutationEvent) {
        this.mutationEvent = mutationEvent;
    }

    @Override
    public String toString() {
        String retVal = ucscID + "\t" + geneSymbol + "\t" + chromosome + "\t" + position + "\t";
        if(strand == 0)
            retVal = retVal + "+\t";
        else
            retVal = retVal + "-\t";
        retVal = retVal + referenceSeq + "\t" + mutationSeq + "\t" + referenceScore + "\t" + mutationScore + "\t";
        if(Double.isNaN(changeSig) || Double.isInfinite(changeSig))
            retVal = retVal + ".\t";
        else
            retVal = retVal + changeSig + "\t";
        switch(mutationEvent)
        {
            case MutationEvent.FunctionalGain:
                retVal = retVal + "Functional Gain\t";
                break;
            case MutationEvent.FunctionalLoss:
                retVal = retVal + "Functional Loss\t";
                break;
            case MutationEvent.MethylationNotChange:
                retVal = retVal + "Methylation not change\t";
                break;
            default:
                retVal = retVal + "Non-methylation not change\t";
        }
        for(VCFRecord vcfRec : relatedMutationList)
        {
            retVal= retVal + vcfRec.toString() + "@";
        }
        retVal = retVal +"\t" + probability;
        return retVal;
    }

    public String toCSVString()
    {
        String retVal = ucscID + "," + geneSymbol + "," + chromosome + "," + position + ",";
        if(strand == 0)
            retVal = retVal + "+,";
        else
            retVal = retVal + "-,";
        retVal = retVal + referenceSeq + "," + mutationSeq + "," + referenceScore + "," + mutationScore + ",";
        if(Double.isNaN(changeSig) || Double.isInfinite(changeSig))
            retVal = retVal + ".,";
        else
            retVal = retVal + changeSig + ",";
        switch(mutationEvent)
        {
            case MutationEvent.FunctionalGain:
                retVal = retVal + "Functional Gain,";
                break;
            case MutationEvent.FunctionalLoss:
                retVal = retVal + "Functional Loss,";
                break;
            case MutationEvent.MethylationNotChange:
                retVal = retVal + "Methylation not change,";
                break;
            default:
                retVal = retVal + "Non-methylation not change,";
        }
        for(VCFRecord vcfRec : relatedMutationList)
        {
            retVal= retVal + vcfRec.toString() + "@";
        }
        retVal = retVal +"," + probability;
        return retVal;
    }

    public String toBEDString()
    {
        String retVal = chromosome + "\t" + (position-1) + "\t" + position + "\t";
        switch(mutationEvent)
        {
            case MutationEvent.FunctionalGain:
                retVal = retVal + "Functional Gain\t";
                break;
            case MutationEvent.FunctionalLoss:
                retVal = retVal + "Functional Loss\t";
                break;
            case MutationEvent.MethylationNotChange:
                retVal = retVal + "Methylation not change\t";
                break;
            default:
                retVal = retVal + "Non-methylation not change\t";
        }
        if(Double.isNaN(changeSig) || Double.isInfinite(changeSig))
            retVal = retVal + ".\t";
        else
            retVal = retVal + changeSig + "\t";
        if(strand == 0)
            retVal = retVal + "+";
        else
            retVal = retVal + "-";
        return retVal;
    }
    
    public String getSimpleString()
    {
        String retVal = ucscID + "\t" + geneSymbol + "\t" + chromosome + "\t" + position + "\t";
        if(strand == 0)
            retVal = retVal + "+\t";
        else
            retVal = retVal + "-\t";
        retVal = retVal  + referenceScore + "\t" + mutationScore + "\t" + changeSig + "\t";
        switch(mutationEvent)
        {
            case MutationEvent.FunctionalGain:
                retVal = retVal + "Functional Gain\t";
                break;
            case MutationEvent.FunctionalLoss:
                retVal = retVal + "Functional Loss\t";
                break;
            case MutationEvent.MethylationNotChange:
                retVal = retVal + "Methylation not change\t";
                break;
            default:
                retVal = retVal + "Non-methylation not change\t";
        }
        retVal = retVal + "\t" + probability;
        return retVal;
    }
    
    public String getMethyMutString()
    {
        String retVal = chromosome + "\t" + position + "\t";
        if(strand == 0)
            retVal = retVal + "+\t";
        else
            retVal = retVal + "-\t";
        retVal = retVal + geneSymbol + "\t" + ucscID + "\t" + referenceSeq + "\t" + referenceScore + "\t" + mutationSeq + "\t" + mutationScore + "\t";
        switch(mutationEvent)
        {
            case MutationEvent.FunctionalGain:
                retVal = retVal + "Functional Gain\t";
                break;
            case MutationEvent.FunctionalLoss:
                retVal = retVal + "Functional Loss\t";
                break;
            case MutationEvent.MethylationNotChange:
                retVal = retVal + "Methylation not change\t";
                break;
            default:
                retVal = retVal + "Non-methylation not change\t";
        }
        return retVal;
    }
}
