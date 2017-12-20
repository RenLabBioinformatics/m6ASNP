/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Annotation;

import java.util.LinkedList;

/**
 *The canonical transcript annotation in UCSC
 * @author Ben
 */
public class UCSCAnnoRecord 
{
    private String chromosome;
    private int transcriptStart;
    private int transcriptEnd;
    private String UCSCID;
    private String geneSymbol;
    private int strand;//0 for sense strand, 1 for anti-sense strand
    private int cdsStart;
    private int cdsEnd;
    private LinkedList<Exon> exonList;

    public UCSCAnnoRecord(String chromosome, int transcriptStart, int transcriptEnd, String UCSCID, String geneSymbol, int strand, int cdsStart, int cdsEnd, LinkedList<Exon> exonList) {
        this.chromosome = chromosome;
        this.transcriptStart = transcriptStart;
        this.transcriptEnd = transcriptEnd;
        this.UCSCID = UCSCID;
        this.geneSymbol = geneSymbol;
        this.strand = strand;
        this.cdsStart = cdsStart;
        this.cdsEnd = cdsEnd;
        this.exonList = exonList;
    }
    
    public UCSCAnnoRecord()
    {
        exonList = new LinkedList();
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getTranscriptStart() {
        return transcriptStart;
    }

    public void setTranscriptStart(int transcriptStart) {
        this.transcriptStart = transcriptStart;
    }

    public int getTranscriptEnd() {
        return transcriptEnd;
    }

    public void setTranscriptEnd(int transcriptEnd) {
        this.transcriptEnd = transcriptEnd;
    }

    public String getUCSCID() {
        return UCSCID;
    }

    public void setUCSCID(String UCSCID) {
        this.UCSCID = UCSCID;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public int getCdsStart() {
        return cdsStart;
    }

    public void setCdsStart(int cdsStart) {
        this.cdsStart = cdsStart;
    }

    public int getCdsEnd() {
        return cdsEnd;
    }

    public void setCdsEnd(int cdsEnd) {
        this.cdsEnd = cdsEnd;
    }
    
    public void addExon(Exon exon)
    {
        this.exonList.add(exon);
    }

    public LinkedList<Exon> getExonList() {
        return exonList;
    }

    public void setExonList(LinkedList<Exon> exonList) {
        this.exonList = exonList;
    }
}
