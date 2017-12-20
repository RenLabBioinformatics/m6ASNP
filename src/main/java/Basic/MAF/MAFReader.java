/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic.MAF;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class MAFReader {
    private HashMap<String, SampleMutation> mafRecMap;
    
    public MAFReader(String mafFilePath, String annotationFilePath)
    {
        mafRecMap = new HashMap();
        AnnotateMAFRecord annotateMAF = new AnnotateMAFRecord(annotationFilePath);
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(mafFilePath));
            String strLine;
            int readCount = 0;
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("#"))
                    continue;
                if(readCount == 0)
                {
                    readCount++;
                    continue;
                }
                String[] strArr = strLine.split("\t");
                MAFRecord mafRec = new MAFRecord();
                mafRec.setGeneSymbol(strArr[0]);
                mafRec.setAssembly(strArr[3]);
                if(strArr[4].contains("chr"))
                    mafRec.setChromosome(strArr[4].substring(3));
                else
                    mafRec.setChromosome(strArr[4]);
                mafRec.setStartPosition(Integer.parseInt(strArr[5]));
                mafRec.setEndPosition(Integer.parseInt(strArr[6]));
                if(strArr[7].equals("+"))
                    mafRec.setStrand(0);
                else 
                    mafRec.setStrand(1);
                mafRec.setVariantClassification(strArr[8]);
                mafRec.setVariantType(strArr[9]);
                mafRec.setReferenceCode(strArr[10]);
                //select tumor mutation
                if(strArr[11].equals(strArr[10]))
                {
                    if(strArr[12].equals(strArr[10]))
                    {
                        System.out.println("Not detect mutation at " + mafRec.getChromosome() + ":" + mafRec.getStartPosition() + "-" + mafRec.getEndPosition());
                        continue;
                    }
                    else
                        mafRec.setTumorCode(strArr[12]);
                }
                else
                    mafRec.setTumorCode(strArr[11]);
                mafRec.setRSNumber(strArr[13]);
                mafRec.setVariationStatus(strArr[14]);
                mafRec.setTumorBarcode(strArr[15]);
                String[] barcodeArr = mafRec.getTumorBarcode().split("-");
                String sampleID = barcodeArr[0] + "-" + barcodeArr[1] + "-" + barcodeArr[2];
                mafRec.setNormalBarcode(strArr[16]);
                //Annotation
                LinkedList<UCSCGene> associatedGeneList = new LinkedList();
                annotateMAF.Annotate(mafRec, associatedGeneList);
                //store mutation
                for(UCSCGene gene : associatedGeneList)
                {
                    String ucscGeneKey = gene.getUcscId() + "|" + gene.getGeneSymbol();
                    SampleMutation sampleMutation;
                    if(mafRecMap.containsKey(sampleID))
                    {
                        sampleMutation = mafRecMap.get(sampleID);
                        sampleMutation.addMutationToGene(ucscGeneKey, mafRec);
                    }
                    else
                    {
                        sampleMutation = new SampleMutation();
                        sampleMutation.addMutationToGene(ucscGeneKey, mafRec);
                        mafRecMap.put(sampleID, sampleMutation);
                    }
                }
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public HashMap<String, SampleMutation> getMafRecMap() {
        return mafRecMap;
    }
    
    public static void main(String[] args)
    {
        MAFReader mafReader = new MAFReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\TCGA\\MAF_collection\\BRCA\\BRCA_integrated_gjj.somatic.maf", "G:\\云同步文件夹\\工作文档\\RNA-methylation\\TCGA\\knownGeneAnnotation_hg19");
        HashMap<String, SampleMutation> mafRecMap = mafReader.getMafRecMap();
    }
}
