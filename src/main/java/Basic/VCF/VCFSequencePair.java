package Basic.VCF;

import java.util.LinkedList;

/**
 * Created by Ben on 2016/11/8.
 */
public class VCFSequencePair
{
    private String chromosome;
    private int strand;
    private int m6AChrPosition;
    private int refM6APosition;
    private String refSequence;
    private int mutM6APosition;
    private String mutSequence;
    private LinkedList<VCFRecord> relatedMutationList;

    public LinkedList<VCFRecord> getRelatedMutationList() {
        return relatedMutationList;
    }

    public void setRelatedMutationList(LinkedList<VCFRecord> relatedMutationList) {
        this.relatedMutationList = relatedMutationList;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public int getM6AChrPosition() {
        return m6AChrPosition;
    }

    public void setM6AChrPosition(int m6AChrPosition) {
        this.m6AChrPosition = m6AChrPosition;
    }

    public int getRefM6APosition() {
        return refM6APosition;
    }

    public void setRefM6APosition(int refM6APosition) {
        this.refM6APosition = refM6APosition;
    }

    public String getRefSequence() {
        return refSequence;
    }

    public void setRefSequence(String refSequence) {
        this.refSequence = refSequence;
    }

    public int getMutM6APosition() {
        return mutM6APosition;
    }

    public void setMutM6APosition(int mutM6APosition) {
        this.mutM6APosition = mutM6APosition;
    }

    public String getMutSequence() {
        return mutSequence;
    }

    public void setMutSequence(String mutSequence) {
        this.mutSequence = mutSequence;
    }
}
