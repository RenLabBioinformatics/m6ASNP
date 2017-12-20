/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Basic.MAF.MAFRecord;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class TCGASequencePair {
    private LinkedList<MAFRecord> relatedMutationList;
    private int methPosition;
    private String referenceSeq;
    private String tumorSeq;

    public String getReferenceSeq() {
        return referenceSeq;
    }

    public void setReferenceSeq(String referenceSeq) {
        this.referenceSeq = referenceSeq;
    }

    public String getTumorSeq() {
        return tumorSeq;
    }

    public void setTumorSeq(String tumorSeq) {
        this.tumorSeq = tumorSeq;
    }

    public int getMethPosition() {
        return methPosition;
    }

    public void setMethPosition(int methPosition) {
        this.methPosition = methPosition;
    }

    public LinkedList<MAFRecord> getRelatedMutationList() {
        return relatedMutationList;
    }

    public void setRelatedMutationList(LinkedList<MAFRecord> relatedMutationList) {
        this.relatedMutationList = relatedMutationList;
    }
}
