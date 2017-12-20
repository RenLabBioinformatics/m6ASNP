/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Basic.VCF.VCFRecord;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class MutationWriter 
{
    public static void WriteMethylationMutation(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String savePath, boolean isOutputAll)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            fw.write("SampleID\tUCSCID\tGene symbol\tChromosome\tPosition\tStrand\tReference sequence\tMutation sequence\tReference score\tMutation score\tSignificance\tMutation event\tRelated mutations\n");
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    if(!isOutputAll)
                    {
                        if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) || (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                            fw.write(sampleID + "\t" + methyMutRec.toString() + "\n");
                    }
                    else
                        fw.write(sampleID + "\t" + methyMutRec.toString() + "\n");
                }
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void WriteMethylationMutationInTSV(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String savePath, boolean isOutputAll)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            fw.write("SampleID\tUCSCID\tGene symbol\tChromosome\tPosition\tStrand\tReference sequence\tMutation sequence\tReference score\tMutation score\tSignificance\tMutation event\tRelated mutations\n");
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    if(!isOutputAll)
                    {
                        if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) || (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                            fw.write(sampleID + "\t" + methyMutRec.toString() + "\n");
                    }
                    else
                        fw.write(sampleID + "\t" + methyMutRec.toString() + "\n");
                }
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void WriteMethylationMutationInCSV(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String savePath, boolean isOutputAll)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            fw.write("SampleID,UCSCID,Gene symbol,Chromosome,Position,Strand,Reference sequence,Mutation sequence,Reference score,Mutation score,Significance,Mutation event,Related mutations\n");
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    if(!isOutputAll)
                    {
                        if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) || (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                            fw.write(sampleID + "\t" + methyMutRec.toCSVString() + "\n");
                    }
                    else
                        fw.write(sampleID + "\t" + methyMutRec.toCSVString() + "\n");
                }
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void WriteMethylationMutationInBED(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String savePath, boolean isOutputAll)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    if(!isOutputAll)
                    {
                        if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) || (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                            fw.write(methyMutRec.toBEDString() + "\n");
                    }
                    else
                        fw.write(methyMutRec.toBEDString() + "\n");
                }
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void WriteMethylationMutationInCircos(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String saveGainPath, String saveLossPath)
    {
        try
        {
            FileWriter fwGain = new FileWriter(saveGainPath);
            fwGain.write("var SCATTER01 = [ \"SCATTER01\" , {\n" +
                    "  SCATTERRadius: 250,\n" +
                    "  innerCircleColor: \"#110000\",\n" +
                    "  innerCircleSize: 1,\n" +
                    "  outerCircleSize: 5,\n" +
                    "  random_data: 30\n" +
                    "} , [\n");
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    String chrName = methyMutRec.getChromosome();
                    if(methyMutRec.getChromosome().startsWith("chr"))
                        chrName = chrName.substring(3);
                    if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) )
                    {
                        fwGain.write("  {chr: \"" + chrName + "\", start: \"" + (methyMutRec.getPosition() - 30) + "\", end: \"" + (methyMutRec.getPosition() + 30) + "\", name: \"" + methyMutRec.getGeneSymbol() + "\", des: \"red\"},\n");
                    }
//                    else if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss)
//                    {
//                        fwGain.write("  {chr: \"" + chrName + "\", start: \"" + (methyMutRec.getPosition() - 30) + "\", end: \"" + (methyMutRec.getPosition() + 30) + "\", name: \"" + methyMutRec.getGeneSymbol() + "\", des: \"green\"},\n");
//                    }
                }
            }
            fwGain.write("]];\n");
            fwGain.close();

            FileWriter fwLoss = new FileWriter(saveLossPath);
            fwLoss.write("var SCATTER02 = [ \"SCATTER02\" , {\n" +
                    "  SCATTERRadius: 180,\n" +
                    "  innerCircleSize: 1,\n" +
                    "  innerCircleColor: \"#004400\",\n" +
                    "  outerCircleSize: 5,\n" +
                    "  random_data: 30\n" +
                    "} , [\n");
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for (MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    String chrName = methyMutRec.getChromosome();
                    if (methyMutRec.getChromosome().startsWith("chr"))
                        chrName = chrName.substring(3);
                    if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                    {
                        fwLoss.write("  {chr: \"" + chrName + "\", start: \"" + (methyMutRec.getPosition() - 30) + "\", end: \"" + (methyMutRec.getPosition() + 30) + "\", name: \"" + methyMutRec.getGeneSymbol() + "\", des: \"green\"},\n");
                    }
                }
            }
            fwLoss.write("]];\n");
            fwLoss.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void WriteMethylationMutationInJSON(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String savePath, boolean isOutputAll)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            JSONStringer jsonStringer = new JSONStringer();
            jsonStringer.array();
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    if(!isOutputAll)
                    {
                        if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) || (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                        {
                            jsonStringer.object();
                            jsonStringer.key("Sample_ID");
                            jsonStringer.value(sampleID);
                            jsonStringer.key("UCSCID");
                            jsonStringer.value(methyMutRec.getUcscID());
                            jsonStringer.key("Gene_symbol");
                            jsonStringer.value(methyMutRec.getGeneSymbol());
                            jsonStringer.key("Chromosome");
                            jsonStringer.value(methyMutRec.getChromosome());
                            jsonStringer.key("Position");
                            jsonStringer.value(methyMutRec.getPosition());
                            jsonStringer.key("Strand");
                            if(methyMutRec.getStrand() == 0)
                                jsonStringer.value("+");
                            else
                                jsonStringer.value("-");
                            jsonStringer.key("Reference_sequence");
                            jsonStringer.value(methyMutRec.getReferenceSeq());
                            jsonStringer.key("Mutation_sequence");
                            jsonStringer.value(methyMutRec.getMutationSeq());
                            jsonStringer.key("Refernce_score");
                            jsonStringer.value(methyMutRec.getReferenceScore());
                            jsonStringer.key("Mutation_score");
                            jsonStringer.value(methyMutRec.getMutationScore());
                            jsonStringer.key("Significance");
                            if( Double.isNaN(methyMutRec.getChangeSig()) || Double.isInfinite(methyMutRec.getChangeSig()) )
                                jsonStringer.value(".");
                            else
                                jsonStringer.value(methyMutRec.getChangeSig());
                            jsonStringer.key("Mutation_event");
                            if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain)
                                jsonStringer.value("Functional Gain");
                            else if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss)
                                jsonStringer.value("Functional Loss");
                            else
                                jsonStringer.value("Unknown");
                            jsonStringer.key("Related_Mutations");
                            JSONArray relatedMutationJson = new JSONArray();
                            LinkedList<VCFRecord> relatedVCFList = methyMutRec.getRelatedMutationList();
                            for(VCFRecord vcfRec : relatedVCFList)
                            {
                                JSONObject relatedMutationObj = new JSONObject();
                                relatedMutationObj.put("Chromosome", vcfRec.getChromosome());
                                relatedMutationObj.put("ID", vcfRec.getId());
                                relatedMutationObj.put("Position", vcfRec.getPosition());
                                relatedMutationObj.put("Reference", vcfRec.getRefBase());
                                relatedMutationObj.put("Mutation", vcfRec.getMutBase());
                                relatedMutationJson.put(relatedMutationObj);
                            }
                            jsonStringer.value(relatedMutationJson);
                            jsonStringer.endObject();
                        }
                    }
                    else
                    {
                        jsonStringer.object();
                        jsonStringer.key("Sample_ID");
                        jsonStringer.value(sampleID);
                        jsonStringer.key("UCSCID");
                        jsonStringer.value(methyMutRec.getUcscID());
                        jsonStringer.key("Gene_symbol");
                        jsonStringer.value(methyMutRec.getGeneSymbol());
                        jsonStringer.key("Chromosome");
                        jsonStringer.value(methyMutRec.getChromosome());
                        jsonStringer.key("Position");
                        jsonStringer.value(methyMutRec.getPosition());
                        jsonStringer.key("Strand");
                        if(methyMutRec.getStrand() == 0)
                            jsonStringer.value("+");
                        else
                            jsonStringer.value("-");
                        jsonStringer.key("Reference_sequence");
                        jsonStringer.value(methyMutRec.getReferenceSeq());
                        jsonStringer.key("Mutation_sequence");
                        jsonStringer.value(methyMutRec.getMutationSeq());
                        jsonStringer.key("Refernce_score");
                        jsonStringer.value(methyMutRec.getReferenceScore());
                        jsonStringer.key("Mutation_score");
                        jsonStringer.value(methyMutRec.getMutationScore());
                        jsonStringer.key("Significance");
                        if(Double.isNaN(methyMutRec.getChangeSig()) || Double.isInfinite(methyMutRec.getChangeSig()))
                            jsonStringer.value(".");
                        else
                            jsonStringer.value(methyMutRec.getChangeSig());
                        jsonStringer.key("Mutation_event");
                        if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain)
                            jsonStringer.value("Functional Gain");
                        else if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss)
                            jsonStringer.value("Functional Loss");
                        else if(methyMutRec.getMutationEvent() == MutationEvent.MethylationNotChange)
                            jsonStringer.value("Methylation not change");
                        else if(methyMutRec.getMutationEvent() == MutationEvent.NonMethylationNotChange)
                            jsonStringer.value("Non-methylation not change");
                        else
                            jsonStringer.value("Unknown");
                        jsonStringer.key("Related_Mutations");
                        JSONArray relatedMutationJson = new JSONArray();
                        LinkedList<VCFRecord> relatedVCFList = methyMutRec.getRelatedMutationList();
                        for(VCFRecord vcfRec : relatedVCFList)
                        {
                            JSONObject relatedMutationObj = new JSONObject();
                            relatedMutationObj.put("Chromosome", vcfRec.getChromosome());
                            relatedMutationObj.put("ID", vcfRec.getId());
                            relatedMutationObj.put("Position", vcfRec.getPosition());
                            relatedMutationObj.put("Reference", vcfRec.getRefBase());
                            relatedMutationObj.put("Mutation", vcfRec.getMutBase());
                            relatedMutationJson.put(relatedMutationObj);
                        }
                        jsonStringer.value(relatedMutationJson);
                        jsonStringer.endObject();
                    }
                }
            }
            jsonStringer.endArray();
            fw.write(jsonStringer.toString());
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void WriteMethylationMutationInGff(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String savePath, boolean isOutputAll)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            int curId = 0;
            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {
                    if(!isOutputAll)
                    {
                        if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) || (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                        {
                            curId++;
                            fw.write(methyMutRec.getChromosome() + "\tm6ASNP\tmutation\t" + methyMutRec.getPosition() + "\t" + methyMutRec.getPosition() + "\t");
                            if(Double.isNaN(methyMutRec.getChangeSig()) || Double.isInfinite(methyMutRec.getChangeSig()))
                                fw.write(".\t");
                            else
                                fw.write(methyMutRec.getChangeSig() + "\t");
                            if(methyMutRec.getStrand() == 0)
                                fw.write("+\t");
                            else
                                fw.write("-\t");
                            fw.write(".\tID=m6AMutation_" + curId + "; Name=" + methyMutRec.getChromosome() + ":" + methyMutRec.getPosition() + "_" + methyMutRec.getGeneSymbol() + "_");
                            if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain)
                                fw.write("Gain\n");
                            else if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss)
                                fw.write("Lost\n");
                            else if(methyMutRec.getMutationEvent() == MutationEvent.MethylationNotChange)
                                fw.write("NotChange\n");
                            else if(methyMutRec.getMutationEvent() == MutationEvent.NonMethylationNotChange)
                                fw.write("NotChange\n");
                            else
                                fw.write("Unknown\n");
                        }
                    }
                    else
                    {
                        curId++;
                        fw.write(methyMutRec.getChromosome() + "\tm6ASNP\tmutation\t" + methyMutRec.getPosition() + "\t" + methyMutRec.getPosition() + "\t");
                        if(Double.isNaN(methyMutRec.getChangeSig()) || Double.isInfinite(methyMutRec.getChangeSig()))
                            fw.write(".\t");
                        else
                            fw.write(methyMutRec.getChangeSig() + "\t");
                        if(methyMutRec.getStrand() == 0)
                            fw.write("+\t");
                        else
                            fw.write("-\t");
                        fw.write(".\tID=m6AMutation_" + curId + "; Name=" + methyMutRec.getChromosome() + ":" + methyMutRec.getPosition() + "_" + methyMutRec.getGeneSymbol() + "_");
                        if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain)
                            fw.write("Gain\n");
                        else if(methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss)
                            fw.write("Lost\n");
                        else if(methyMutRec.getMutationEvent() == MutationEvent.MethylationNotChange)
                            fw.write("NotChange\n");
                        else if(methyMutRec.getMutationEvent() == MutationEvent.NonMethylationNotChange)
                            fw.write("NotChange\n");
                        else
                            fw.write("Unknown\n");
                    }
                }
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void WriteRelatedMutationInVCF(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String savePath, boolean isOutputAll)
    {
        try
        {
            FileWriter fw = new FileWriter(savePath);
            //Mutation set
            HashSet<String> m6AMutationSet = new HashSet<>();
            //Write vcf header title
            fw.write("##fileformat=VCFv4.2\n");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            fw.write("##fileDate=" + dateFormat.format(new Date()) + "\n");
            fw.write("##source=m6ASNP\n");
            fw.write("##FORMAT=<ID=GT,Number=1,Type=String,Description=\"Genotype\">\n");
            fw.write("#CHROM\tPOS\tID\tREF\tALT\tQUAL\tFILTER\tINFO\tFORMAT\tSample0\n");

            for(String sampleID : methyMutMap.keySet())
            {
                LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
                for(MethylationMutationRecord methyMutRec : methyMutRecList)
                {

                    if(isOutputAll)
                    {
                        LinkedList<VCFRecord> vcfRecList = methyMutRec.getRelatedMutationList();
                        for(VCFRecord vcfRec : vcfRecList)
                        {
                            String mutStr = vcfRec.getChromosome() + "|" + vcfRec.getPosition() + "|" + vcfRec.getRefBase() + "|" + vcfRec.getMutBase() + "|" + sampleID;
                            if(!m6AMutationSet.contains(mutStr))
                            {
                                fw.write(vcfRec.getChromosome() + "\t" + vcfRec.getPosition() + "\t" + vcfRec.getId() + "\t" + vcfRec.getRefBase() + "\t" + vcfRec.getMutBase() + "\t50\tPASS\t.\tGT\t1/1\n");
                                m6AMutationSet.add(mutStr);
                            }
                        }
                    }
                    else
                    {
                        if( (methyMutRec.getMutationEvent() == MutationEvent.FunctionalGain) || (methyMutRec.getMutationEvent() == MutationEvent.FunctionalLoss) )
                        {
                            LinkedList<VCFRecord> vcfRecList = methyMutRec.getRelatedMutationList();
                            for(VCFRecord vcfRec : vcfRecList)
                            {
                                String mutStr = vcfRec.getChromosome() + "|" + vcfRec.getPosition() + "|" + vcfRec.getRefBase() + "|" + vcfRec.getMutBase() + "|" + sampleID;
                                if(!m6AMutationSet.contains(mutStr))
                                {
                                    fw.write(vcfRec.getChromosome() + "\t" + vcfRec.getPosition() + "\t" + vcfRec.getId() + "\t" + vcfRec.getRefBase() + "\t" + vcfRec.getMutBase() + "\t50\tPASS\t.\tGT\t1/1\n");
                                    m6AMutationSet.add(mutStr);
                                }
                            }
                        }
                    }
                }
            }
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
