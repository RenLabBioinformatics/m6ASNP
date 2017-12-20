/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic.MAF;

/**
 *
 * @author Ben
 */
public class UCSCGene {
    private String geneSymbol;
    private String ucscId;
    private int exonStart;
    private int exonEnd;

    public int getExonStart() {
        return exonStart;
    }

    public void setExonStart(int exonStart) {
        this.exonStart = exonStart;
    }

    public int getExonEnd() {
        return exonEnd;
    }

    public void setExonEnd(int exonEnd) {
        this.exonEnd = exonEnd;
    }

    public UCSCGene(String geneSymbol, String ucscId, int exonStart, int exonEnd) {
        this.geneSymbol = geneSymbol;
        this.ucscId = ucscId;
        this.exonStart = exonStart;
        this.exonEnd = exonEnd;
    }
    
    public UCSCGene()
    {
        this.geneSymbol = null;
        this.ucscId = null;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getUcscId() {
        return ucscId;
    }

    public void setUcscId(String ucscId) {
        this.ucscId = ucscId;
    }
}
