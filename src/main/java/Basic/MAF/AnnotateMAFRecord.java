/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic.MAF;

import Annotation.UCSCAnnoReader;
import Annotation.UCSCAnnoRecord;
import Basic.Tools;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class AnnotateMAFRecord {
    private HashMap<String, LinkedList<UCSCAnnoRecord>> ucscAnnoRecMap;
    private Chromosome chrNameConvert = new Chromosome();
    
    public AnnotateMAFRecord(String ucscAnnoFilePath)
    {
        UCSCAnnoReader ucscReader = new UCSCAnnoReader(ucscAnnoFilePath);
        ucscAnnoRecMap = ucscReader.getUcscAnnoMap();
    }
    
    private boolean isMutationOverlapTranscript(MAFRecord mafRec, UCSCAnnoRecord ucscRec)
    {
        int mafStart = mafRec.getStartPosition();
        int mafEnd = mafRec.getEndPosition();
        int ucscStart = ucscRec.getTranscriptStart();
        int ucscEnd = ucscRec.getTranscriptEnd();
        return Tools.isOverlaps(mafStart, mafEnd, ucscStart, ucscEnd);
    }
    
    public void Annotate(MAFRecord mafRec, LinkedList<UCSCGene> associatedGeneList)
    {
        String chrName = chrNameConvert.ConvertEnsemblToUCSC(mafRec.getChromosome());
//        String chrName = mafRec.getChromosome();
        if(ucscAnnoRecMap.containsKey(chrName))
        {
            LinkedList<UCSCAnnoRecord> ucscRecList = ucscAnnoRecMap.get(chrName);
            for(UCSCAnnoRecord ucscRec : ucscRecList)
            {
                if(isMutationOverlapTranscript(mafRec, ucscRec))
                {
                    UCSCGene gene = new UCSCGene(ucscRec.getGeneSymbol(), ucscRec.getUCSCID(), ucscRec.getTranscriptStart(), ucscRec.getTranscriptEnd());
                    associatedGeneList.add(gene);
                }
            }
        }
    }
}
