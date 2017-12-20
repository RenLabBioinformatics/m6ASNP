package Basic.VCF;

import Annotation.UCSCAnnoReader;
import Annotation.UCSCAnnoRecord;
import Basic.MAF.Chromosome;
import Basic.MAF.UCSCGene;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2016/11/7.
 */
public class AnnotateVCFRecord
{
    private HashMap<String, LinkedList<UCSCAnnoRecord>> ucscAnnoRecMap;
    private Chromosome chrNameConvert = new Chromosome();

    public AnnotateVCFRecord(String ucscAnnoFilePath)
    {
        UCSCAnnoReader ucscReader = new UCSCAnnoReader(ucscAnnoFilePath);
        ucscAnnoRecMap = ucscReader.getUcscAnnoMap();
    }

    private boolean isMutationOverlapTranscript(VCFRecord vcfRec, UCSCAnnoRecord ucscRec)
    {
        int position = vcfRec.getPosition();
        int ucscStart = ucscRec.getTranscriptStart();
        int ucscEnd = ucscRec.getTranscriptEnd();
        if( (position >= ucscStart) && (position <= ucscEnd) )
            return true;
        else
            return false;
    }

    public void Annotate(VCFRecord vcfRec, LinkedList<UCSCGene> associatedGeneList)
    {
        String chrName = chrNameConvert.ConvertEnsemblToUCSC(vcfRec.getChromosome());
//        String chrName = mafRec.getChromosome();
        if(ucscAnnoRecMap.containsKey(chrName))
        {
            LinkedList<UCSCAnnoRecord> ucscRecList = ucscAnnoRecMap.get(chrName);
            for(UCSCAnnoRecord ucscRec : ucscRecList)
            {
                if(isMutationOverlapTranscript(vcfRec, ucscRec))
                {
                    UCSCGene gene = new UCSCGene(ucscRec.getGeneSymbol(), ucscRec.getUCSCID(), ucscRec.getTranscriptStart(), ucscRec.getTranscriptEnd());
                    associatedGeneList.add(gene);
                }
            }
        }
    }
}
