package Basic.Tabular;

import Basic.MAF.Chromosome;
import Basic.MAF.UCSCGene;
import Basic.VCF.AnnotateVCFRecord;
import Basic.VCF.SampleVCFMutation;
import Basic.VCF.VCFRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2016/12/1.
 */
public class TabularReader
{
    private HashMap<String, SampleVCFMutation> vcfSampleRecMap;
    private Chromosome chrConvert = new Chromosome();

    public TabularReader(String tabFile, String annotationFile)
    {
        vcfSampleRecMap = new HashMap<String, SampleVCFMutation>();
        AnnotateVCFRecord annotateVCFRecord = new AnnotateVCFRecord(annotationFile);
        //Read tabular file
        //Chromosome  Position  id  refCode  mutCode  SampleName Comments
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(tabFile));
            String strLine;
            String[] strArr;
            SampleVCFMutation sampleVCFMutation;
            int mutationCount = 0;
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("#"))
                    continue;
                mutationCount++;
                strArr = strLine.split("\t");
                String chrName = strArr[0];
                if(chrName.startsWith("chr"))
                    chrName = chrConvert.ConvertUCSCToEnsembl(chrName);
                int startPosition = Integer.parseInt(strArr[1]);
                String id = strArr[2];
                String refCode = strArr[3];
                String mutCode = strArr[4];
                String sampleName;
                if(strArr.length > 5)
                    sampleName = strArr[5];
                else
                    sampleName = "sample" + mutationCount;
                VCFRecord vcfRec = new VCFRecord();
                vcfRec.setChromosome(chrName);
                vcfRec.setPosition(startPosition);
                vcfRec.setId(id);
                vcfRec.setRefBase(refCode);
                vcfRec.setMutBase(mutCode);

                LinkedList<UCSCGene> associatedGeneList = new LinkedList();
                annotateVCFRecord.Annotate(vcfRec, associatedGeneList);
                for(UCSCGene gene : associatedGeneList)
                {
                    String ucscGeneKey = gene.getUcscId() + "|" + gene.getGeneSymbol();
                    if(vcfSampleRecMap.containsKey(sampleName))
                    {
                        sampleVCFMutation = vcfSampleRecMap.get(sampleName);
                        sampleVCFMutation.addVCFMutation(ucscGeneKey, vcfRec);
                    }
                    else
                    {
                        sampleVCFMutation = new SampleVCFMutation();
                        sampleVCFMutation.addVCFMutation(ucscGeneKey, vcfRec);
                        vcfSampleRecMap.put(sampleName, sampleVCFMutation);
                    }
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public HashMap<String, SampleVCFMutation> getVcfSampleRecMap() {
        return vcfSampleRecMap;
    }
}
