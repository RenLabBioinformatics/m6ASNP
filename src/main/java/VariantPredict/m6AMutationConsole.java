/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import Annotation.GO.GOAnnotation;
import Annotation.GO.GONamespace;
import Basic.FormatVerify;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class m6AMutationConsole 
{
    public static void PrintHelp()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(m6AMutationConsole.class.getResourceAsStream("/help.txt")));
            while(br.ready())
            {
                System.out.println(br.readLine());
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) 
    {
        HashMap<String, String> paramMap = new HashMap();
        if(args[0].equals("-h") || args[0].equals("-help") || args[0].equals("--help"))
        {
            PrintHelp();
        }
        else if(args[0].equals("-predict"))
        {
            paramMap.clear();
            paramMap.put("-isAll", "false");
            paramMap.put("-t", "Medium");
            paramMap.put("-it", "vcf");
            paramMap.put("-sp", "Human");
            paramMap.put("-ot", "txt");
            int curIndex = 0;
            while(true)
            {
                paramMap.put(args[curIndex+1], args[curIndex+2]);
                curIndex = curIndex + 2;
                if(curIndex+1 >= args.length)
                    break;
            }
            System.out.println("Reading input file...");
            MethylationMutationPredictor methMutationPredictor = new MethylationMutationPredictor(paramMap.get("-a"), paramMap.get("-i"), paramMap.get("-it"), paramMap.get("-g"), paramMap.get("-sp"));
            System.out.println("Predicting m6A variation at threshold " + paramMap.get("-t") + "...");
            methMutationPredictor.Predict(paramMap.get("-t"));
            System.out.println("Saving result...");
            methMutationPredictor.SaveResult(paramMap.get("-o"), paramMap.get("-ot"), Boolean.parseBoolean(paramMap.get("-isAll")));
            System.out.println("Calculation completed!");
        }
        else if(args[0].equals("-verify"))
        {
            paramMap.clear();
            int curIndex = 0;
            while(true)
            {
                paramMap.put(args[curIndex+1], args[curIndex+2]);
                curIndex = curIndex + 2;
                if(curIndex+1 >= args.length)
                    break;
            }
            if (FormatVerify.isVCF(paramMap.get("-i")))
                System.out.println("VCF format");
            else if(FormatVerify.isTabular(paramMap.get("-i")))
                System.out.println("Tabular format");
            else
                System.out.println("Unknown format");
        }
        else if(args[0].equals("-go"))
        {
            paramMap.clear();
            paramMap.put("-ns", "bp");
            paramMap.put("-num", "9");
            int curIndex = 0;
            while(true)
            {
                paramMap.put(args[curIndex+1], args[curIndex+2]);
                curIndex = curIndex + 2;
                if(curIndex+1 >= args.length)
                    break;
            }
            MethylationMutationResultReader methyResultReader = new MethylationMutationResultReader(paramMap.get("-i"));
            HashMap<String, LinkedList<MethylationMutationRecord>> methyMutationMap = methyResultReader.getMethyMutationMap();
            GOAnnotation goAnnotation = new GOAnnotation(methyMutationMap, paramMap.get("-obo"), paramMap.get("-anno"));
            String nameSpace = paramMap.get("-ns");
            String[] strArr = nameSpace.split(",");
            for(int i=0; i<strArr.length; i++)
            {
                String ns = strArr[i].trim();
                int nameSpaceType;
                if (ns.equalsIgnoreCase("bp"))
                    nameSpaceType = GONamespace.BiologicalProcess;
                else if (ns.equalsIgnoreCase("mf"))
                    nameSpaceType = GONamespace.MolecularFunction;
                else if (ns.equalsIgnoreCase("cc"))
                    nameSpaceType = GONamespace.CellularComponent;
                else
                {
                    System.out.println("Unknown namespace for " + ns + ". Take Biological Process in default!");
                    nameSpaceType = GONamespace.BiologicalProcess;
                }
                goAnnotation.Annotate(nameSpaceType);
                goAnnotation.SaveAnnotateResult(paramMap.get("-o") + "_" + ns + ".txt", Integer.parseInt(paramMap.get("-num")));
            }
        }
        else
        {
            System.out.println("CMD Error!");
            PrintHelp();
        }
    }
}
