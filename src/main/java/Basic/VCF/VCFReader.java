package Basic.VCF;

import Basic.MAF.Chromosome;
import Basic.MAF.UCSCGene;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 2016/11/7.
 */
public class VCFReader
{
    private HashMap<String, SampleVCFMutation> vcfSampleRecMap;
    private Chromosome chrConvert = new Chromosome();

    public VCFReader(String vcfFile, String annotationFile)
    {
        vcfSampleRecMap = new HashMap<String, SampleVCFMutation>();
        AnnotateVCFRecord annotateVCFRecord = new AnnotateVCFRecord(annotationFile);
        VCFFileReader vcfFileReader = new VCFFileReader(new File(vcfFile), false);
        int mutationCount = 0;
        for(CloseableIterator<VariantContext> variantItr = vcfFileReader.iterator(); variantItr.hasNext();)
        {
            mutationCount++;
            VariantContext variant = variantItr.next();
            String chrName = variant.getContig();
            if(chrName.startsWith("chr"))
                chrName = chrConvert.ConvertUCSCToEnsembl(chrName);
            int startPosition = variant.getStart();
            String refCode = variant.getReference().getBaseString();
            String id = variant.getID();
            Set<String> sampleNameSet = variant.getSampleNames();
            for(String sampleName : sampleNameSet)
            {
                SampleVCFMutation sampleVCFMutation;
                Genotype genotype = variant.getGenotype(sampleName);
                List<Allele> alleleList = genotype.getAlleles();
                for(Allele allele : alleleList)
                {
                    if(allele.isNonReference())
                    {
                        VCFRecord vcfRecord = new VCFRecord();
                        vcfRecord.setChromosome(chrName);
                        vcfRecord.setId(id);
                        vcfRecord.setPosition(startPosition);
                        vcfRecord.setRefBase(refCode);
                        vcfRecord.setMutBase(allele.getBaseString());
                        //Annotate
                        LinkedList<UCSCGene> associatedGeneList = new LinkedList();
                        annotateVCFRecord.Annotate(vcfRecord, associatedGeneList);
                        for(UCSCGene gene : associatedGeneList)
                        {
                            String ucscGeneKey = gene.getUcscId() + "|" + gene.getGeneSymbol();
                            if(vcfSampleRecMap.containsKey(sampleName))
                            {
                                sampleVCFMutation = vcfSampleRecMap.get(sampleName);
                                sampleVCFMutation.addVCFMutation(ucscGeneKey, vcfRecord);
                            }
                            else
                            {
                                sampleVCFMutation = new SampleVCFMutation();
                                sampleVCFMutation.addVCFMutation(ucscGeneKey, vcfRecord);
                                vcfSampleRecMap.put(sampleName, sampleVCFMutation);
                            }
                        }
                        break;
                    }
                }
            }
            //No sample information. Regarded as independent for each mutation. Default sample Id were added for each mutation!
            if(sampleNameSet.size() == 0)
            {
                SampleVCFMutation sampleVCFMutation;
                String sampleName = "sample" + mutationCount;
                List<Allele> alleleList = variant.getAlleles();
                for(Allele allele : alleleList)
                {
                    if(allele.isNonReference())
                    {
                        VCFRecord vcfRecord = new VCFRecord();
                        vcfRecord.setChromosome(chrName);
                        vcfRecord.setId(id);
                        vcfRecord.setPosition(startPosition);
                        vcfRecord.setRefBase(refCode);
                        vcfRecord.setMutBase(allele.getBaseString());
                        //Annotate
                        LinkedList<UCSCGene> associatedGeneList = new LinkedList();
                        annotateVCFRecord.Annotate(vcfRecord, associatedGeneList);
                        for(UCSCGene gene : associatedGeneList)
                        {
                            String ucscGeneKey = gene.getUcscId() + "|" + gene.getGeneSymbol();
                            if(vcfSampleRecMap.containsKey(sampleName))
                            {
                                sampleVCFMutation = vcfSampleRecMap.get(sampleName);
                                sampleVCFMutation.addVCFMutation(ucscGeneKey, vcfRecord);
                            }
                            else
                            {
                                sampleVCFMutation = new SampleVCFMutation();
                                sampleVCFMutation.addVCFMutation(ucscGeneKey, vcfRecord);
                                vcfSampleRecMap.put(sampleName, sampleVCFMutation);
                            }
                        }
                        break;
                    }
                }
            }
        }
        vcfFileReader.close();
    }

    public HashMap<String, SampleVCFMutation> getVcfSampleRecMap()
    {
        return vcfSampleRecMap;
    }

    public static void main(String[] args)
    {
        VCFReader vcfReader = new VCFReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\Test.vcf", "G:\\云同步文件夹\\工作文档\\RNA-methylation\\TCGA\\knownGeneAnnotation_hg19");
        vcfReader.getVcfSampleRecMap();
    }
}
