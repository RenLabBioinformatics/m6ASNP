/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Annotation.UCSCAnnoReader;
import Basic.Tabular.TabularReader;
import Basic.VCF.SampleVCFMutation;
import Basic.VCF.VCFReader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *Predict the m6A methylation mutation from TCGA somatic mutation data
 * @author Ben
 */
public class MethylationMutationPredictor 
{
    private ParameterRecord paramRec;
    private UCSCAnnoReader ucscAnnoReader;
    private HashMap<String, SampleVCFMutation> sampleMutationMap;
    private String genomeFilePath;
    private HashMap<String, LinkedList<MethylationMutationRecord>> methyMutationMap;
    
    public MethylationMutationPredictor(String ucscAnnotationFilePath, String mutFilePath, String infmt, String genomeFilePath)
    {
        ucscAnnoReader = new UCSCAnnoReader(ucscAnnotationFilePath);
        if(infmt.equals("vcf"))
        {
            VCFReader vcfReader = new VCFReader(mutFilePath, ucscAnnotationFilePath);
            sampleMutationMap = vcfReader.getVcfSampleRecMap();
        }
        else if(infmt.equals("tab"))
        {
            TabularReader tabReader = new TabularReader(mutFilePath, ucscAnnotationFilePath);
            sampleMutationMap = tabReader.getVcfSampleRecMap();
        }
        else
        {
            System.out.println("Unknown input format! Read as tabular file.");
            TabularReader tabReader = new TabularReader(mutFilePath, ucscAnnotationFilePath);
            sampleMutationMap = tabReader.getVcfSampleRecMap();
        }

        this.genomeFilePath = genomeFilePath;
        ParameterReader paramReader = new ParameterReader("/Model/ParamHuman.txt");
        paramRec = paramReader.getParamRec();
    }
    
    public void Predict(String threshod)
    {
        //Get methylation mutation
        ExtractMethyMutation extractMM = new ExtractMethyMutation(ucscAnnoReader.getUCSCAnnoIDMap(), sampleMutationMap, genomeFilePath);
        extractMM.Extract(paramRec.getUp(), paramRec.getDown());
        methyMutationMap = extractMM.getMethyMutationList();
        extractMM.CloseGenome();
        //Format mutation sequence
        RefineMutation refineMutation = new RefineMutation(methyMutationMap, genomeFilePath);
        refineMutation.RefineAll(paramRec.getUp(), paramRec.getDown());
        refineMutation.CloseGenome();
        //Predic functional change
        MutationPredictor mutPredictor = new MutationPredictor(methyMutationMap);
        mutPredictor.Predict(threshod);
    }
    
    public void SaveResult(String savePath, String outputType, boolean isOutputAll)
    {
        String[] outputTypeArr = outputType.split(",");
        HashSet<String> outputTypeSet = new HashSet<String>();
        for(int i=0; i<outputTypeArr.length; i++)
        {
            outputTypeSet.add(outputTypeArr[i].trim());
        }
        for(String outputTypeStr : outputTypeSet)
        {
            if(outputTypeStr.equals("txt"))
                MutationWriter.WriteMethylationMutation(methyMutationMap, savePath + ".txt", isOutputAll);
            else if(outputTypeStr.equals("js"))
                MutationWriter.WriteMethylationMutationInCircos(methyMutationMap, savePath + ".gain.js", savePath + ".loss.js");
            else if(outputTypeStr.equals("json"))
                MutationWriter.WriteMethylationMutationInJSON(methyMutationMap, savePath + ".json", isOutputAll);
            else if(outputTypeStr.equals("gff"))
                MutationWriter.WriteMethylationMutationInGff(methyMutationMap, savePath + ".gff", isOutputAll);
            else if(outputTypeStr.equals("vcf"))
                MutationWriter.WriteRelatedMutationInVCF(methyMutationMap, savePath + ".vcf", isOutputAll);
            else if(outputTypeStr.equals("csv"))
                MutationWriter.WriteMethylationMutationInCSV(methyMutationMap, savePath + ".csv", isOutputAll);
            else if(outputTypeStr.equals("bed"))
                MutationWriter.WriteMethylationMutationInBED(methyMutationMap, savePath + ".bed", isOutputAll);
            else if(outputTypeStr.equals("tsv"))
                MutationWriter.WriteMethylationMutationInTSV(methyMutationMap, savePath + ".tsv", isOutputAll);
            else
                System.out.println("Unknow output file type " + outputTypeStr);
        }
    }
    
    public static void main(String[] args) 
    {
        MethylationMutationPredictor methyMutPredictor = new MethylationMutationPredictor("G:\\云同步文件夹\\工作文档\\RNA-methylation\\TCGA\\knownGeneAnnotation_hg19", "G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\ClinVar\\common_and_clinical_20170905.tab", "tab", "G:\\Genome\\2bit\\hg19.2bit");
        methyMutPredictor.Predict("High");
        methyMutPredictor.SaveResult("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\ClinVar\\Result_Tab.txt", "txt", false);
    }
}
