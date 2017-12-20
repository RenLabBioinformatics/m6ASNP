/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic.MAF;

/**
 *The Basic.MAF record for somatic mutation
 * @author Ben
 */
public class MAFRecord {
    private String chromosome;
    private String assembly;
    private int startPosition;
    private int endPosition;
    private int strand;
    private String variantClassification;
    private String variantType;
    private String referenceCode;
    private String tumorCode;
    private String tumorBarcode;
    private String normalBarcode;
    private String geneSymbol;
    private String UCSCID;
    private String RSNumber;
    private String variationStatus;

    public String getVariationStatus() {
        return variationStatus;
    }

    public void setVariationStatus(String variationStatus) {
        this.variationStatus = variationStatus;
    }

    public String getRSNumber() {
        return RSNumber;
    }

    public void setRSNumber(String RSNumber) {
        this.RSNumber = RSNumber;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public String getVariantClassification() {
        return variantClassification;
    }

    public void setVariantClassification(String variantClassification) {
        this.variantClassification = variantClassification;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public String getTumorCode() {
        return tumorCode;
    }

    public void setTumorCode(String tumorCode) {
        this.tumorCode = tumorCode;
    }

    public String getTumorBarcode() {
        return tumorBarcode;
    }

    public void setTumorBarcode(String tumorBarcode) {
        this.tumorBarcode = tumorBarcode;
    }

    public String getNormalBarcode() {
        return normalBarcode;
    }

    public void setNormalBarcode(String normalBarcode) {
        this.normalBarcode = normalBarcode;
    }

    public String getGeneSymbol() {
        return geneSymbol;
    }

    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }

    public String getUCSCID() {
        return UCSCID;
    }

    public void setUCSCID(String UCSCID) {
        this.UCSCID = UCSCID;
    }
    
    @Override
    public String toString()
    {
         return geneSymbol + "|" + startPosition + "-" + endPosition + "|" + variantClassification + "|"  + referenceCode + "|" + tumorCode + "|" + RSNumber + "|" + variationStatus;
    }
}
