package Basic.Tabular;

import Basic.MAF.Chromosome;
import Basic.MAF.UCSCGene;
import Basic.VCF.AnnotateVCFRecord;
import Basic.VCF.SampleVCFMutation;
import Basic.VCF.VCFReader;
import Basic.VCF.VCFRecord;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.variant.variantcontext.Allele;
import htsjdk.variant.variantcontext.Genotype;
import htsjdk.variant.variantcontext.VariantContext;
import htsjdk.variant.vcf.VCFFileReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Ben on 2017/9/20.
 */
public class VCFConvertToTab
{
    private HashMap<String, TabSampleMutation> vcfSampleRecMap;
    private Chromosome chrConvert = new Chromosome();

    public VCFConvertToTab(String vcfFile)
    {
        vcfSampleRecMap = new HashMap<>();
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
                        if(vcfSampleRecMap.containsKey(sampleName))
                        {
                            TabSampleMutation tabSampleMutation = vcfSampleRecMap.get(sampleName);
                            tabSampleMutation.AddVCFRecord(vcfRecord);
                        }
                        else
                        {
                            TabSampleMutation tabSampleMutation = new TabSampleMutation();
                            tabSampleMutation.setSampleID(sampleName);
                            tabSampleMutation.AddVCFRecord(vcfRecord);
                            vcfSampleRecMap.put(sampleName, tabSampleMutation);
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
                        if(vcfSampleRecMap.containsKey(sampleName))
                        {
                            TabSampleMutation tabSampleMutation = vcfSampleRecMap.get(sampleName);
                            tabSampleMutation.AddVCFRecord(vcfRecord);
                        }
                        else
                        {
                            TabSampleMutation tabSampleMutation = new TabSampleMutation();
                            tabSampleMutation.setSampleID(sampleName);
                            tabSampleMutation.AddVCFRecord(vcfRecord);
                            vcfSampleRecMap.put(sampleName, tabSampleMutation);
                        }
                        break;
                    }
                }
            }
        }
        vcfFileReader.close();
    }

    public void SaveTabFile(String tabFile)
    {
        try
        {
            FileWriter fw = new FileWriter(tabFile);
            fw.write("#Chromosome\tPosition\tID\tReference\tAlteration\tSample\n");
            for(String sampleName : vcfSampleRecMap.keySet())
            {
                TabSampleMutation tabSampleMutation = vcfSampleRecMap.get(sampleName);
                LinkedList<VCFRecord> vcfRecList = tabSampleMutation.getVcfRecList();
                for(VCFRecord vcfRecord : vcfRecList)
                {
                    fw.write(vcfRecord.toTabString() + "\t" + sampleName + "\n");
                }
            }
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        VCFConvertToTab vcfConvertToTab = new VCFConvertToTab("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\ClinVar\\common_and_clinical_20170905.vcf");
        vcfConvertToTab.SaveTabFile("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\ClinVar\\common_and_clinical_20170905.tab");
    }
}
