package Annotation.GO;

import VariantPredict.MethylationMutationRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2016/12/8.
 */
public class GOAnnotation
{
    private HashMap<String, GOAnnotationRecord> annoResMap = new HashMap<>();
    private HashMap<String, GOTerm> bpGoTermMap, mfGOTermMap, ccGOTermMap;
    private HashMap<String, LinkedList<String>> geneGOAnnoMap;
    private HashMap<String, LinkedList<MethylationMutationRecord>> resultMap;

    public GOAnnotation(HashMap<String, LinkedList<MethylationMutationRecord>> resultMap, String oboGOTermFile, String geneGOAnnoFile)
    {
        this.resultMap = resultMap;

        OboGoTermReader oboGoTermReader = new OboGoTermReader(oboGOTermFile);
        bpGoTermMap = oboGoTermReader.getBpTermMap();
        mfGOTermMap = oboGoTermReader.getMfTermMap();
        ccGOTermMap = oboGoTermReader.getCcTermMap();

        GOTermAnnotationReader goTermAnnotationReader = new GOTermAnnotationReader(geneGOAnnoFile);
        geneGOAnnoMap = goTermAnnotationReader.getAnnotationMap();
    }

    public void Annotate(int namespaceType)
    {
        HashMap<String, GOTerm> curGOMap;
        if(namespaceType == GONamespace.BiologicalProcess)
            curGOMap = bpGoTermMap;
        else if(namespaceType == GONamespace.MolecularFunction)
            curGOMap = mfGOTermMap;
        else if(namespaceType == GONamespace.CellularComponent)
            curGOMap = ccGOTermMap;
        else
        {
            System.out.println("Unknown namespace! Use Biological Process in default.");
            curGOMap = bpGoTermMap;
        }

        for(String sampleID : resultMap.keySet())
        {
            LinkedList<MethylationMutationRecord> m6aMutationList = resultMap.get(sampleID);
            for(MethylationMutationRecord methyMutRec : m6aMutationList)
            {
                String geneName = methyMutRec.getGeneSymbol();
                if(geneGOAnnoMap.containsKey(geneName))
                {
                    LinkedList<String> annotatedGOList = geneGOAnnoMap.get(geneName);
                    for(String goID : annotatedGOList)
                    {
                        if(curGOMap.containsKey(goID))
                        {
                            GOTerm goTerm = curGOMap.get(goID);
                            GOAnnotationRecord goAnnoRec;
                            if(annoResMap.containsKey(goTerm.getGoTerm()))
                                goAnnoRec = annoResMap.get(goTerm.getGoTerm());
                            else
                            {
                                goAnnoRec = new GOAnnotationRecord();
                                goAnnoRec.setNumber(0);
                                annoResMap.put(goTerm.getGoTerm(), goAnnoRec);
                            }
                            goAnnoRec.setGoTerm(goTerm.getGoTerm());
                            int number = goAnnoRec.getNumber() + 1;
                            goAnnoRec.setNumber(number);
                        }
                    }
                }
            }
        }

        double sum = 0;
        for(String goTermName : annoResMap.keySet())
        {
            sum = sum + annoResMap.get(goTermName).getNumber();
        }
        for(String goTermName : annoResMap.keySet())
        {
            double percentage = 0;
            percentage = annoResMap.get(goTermName).getNumber() / sum;
            annoResMap.get(goTermName).setPercentage(percentage);
        }
    }

    public void SaveAnnotateResult(String saveFile, int numberOfTerm)
    {
        LinkedList<GOAnnotationRecord> goAnnotatedList = new LinkedList<>();
        for(String goTermName : annoResMap.keySet())
        {
            goAnnotatedList.add(annoResMap.get(goTermName));
        }
        Collections.sort(goAnnotatedList, new CompareGOAnnotationRecord());
        try
        {
            FileWriter fw = new FileWriter(saveFile);
            int termNumber = 0;
            int leftNumberCount = 0;
            double leftPercentage = 0;
            for(GOAnnotationRecord goAnnoRec : goAnnotatedList)
            {
                termNumber++;
                if(termNumber <= numberOfTerm)
                {
                    fw.write(goAnnoRec.getGoTerm() + "\t" + goAnnoRec.getNumber() + "\t" + goAnnoRec.getPercentage() + "\n");
                }
                else
                {
                    leftNumberCount = leftNumberCount + goAnnoRec.getNumber();
                    leftPercentage = leftPercentage + goAnnoRec.getPercentage();
                }
            }
            if(leftNumberCount > 0)
                fw.write("Others\t" + leftNumberCount + "\t" + leftPercentage + "\n");
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
