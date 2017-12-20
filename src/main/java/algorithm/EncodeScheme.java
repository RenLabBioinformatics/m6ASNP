/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm;

import algorithm.SecondaryAlignment.NussinovAlignment;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public class EncodeScheme {
    private HashMap<String, Double> nucleotideMap = new HashMap();
    int up, down;
    
    private int kSpace;
    private HashMap<String, Integer> encodeMap = new HashMap();
    private final String nucleotide = "TCGA";
    private final String secondaryNotation = "().";
    private double[] vector;
    private NussinovAlignment nussAlign;
    
    public EncodeScheme(int up, int down)
    {
        nucleotideMap.clear();
        encodeMap.clear();
        nucleotideMap.put("A", 1D);
        nucleotideMap.put("T", 2D);
        nucleotideMap.put("U", 2D);
        nucleotideMap.put("C", 3D);
        nucleotideMap.put("G", 4D);
        
//        int index = up + down;
//        for(int i=0; i<secondaryNotation.length(); i++)
//        {
//            for(int j=0; j<secondaryNotation.length(); j++)
//            {
//                for(int k=0; k<secondaryNotation.length(); k++)
//                {
//                    encodeMap.put("" + secondaryNotation.charAt(i) + secondaryNotation.charAt(j) + secondaryNotation.charAt(k), index);
//                    index++;
//                }
//            }
//        }
        
        vector = new double[up + down];
        this.up = up;
        this.down = down;
//        nussAlign = new NussinovAlignment();
    }
    
//    public EncodeScheme(int kSpace)
//    {
//        this.kSpace = kSpace;
//        int index = 0;
//        //single nucleotide
//        for(int i=0; i<nucleotide.length(); i++)
//        {
//            encodeMap.put("" + nucleotide.charAt(i), index);
//            index++;
//        }
//        //1-space nucleotide
//        for(int k=0; k<=kSpace; k++)
//        {
//            for(int i=0; i<nucleotide.length(); i++)
//            {
//                for(int j=0; j<nucleotide.length(); j++)
//                {
//                    encodeMap.put("" + k + nucleotide.charAt(i) + nucleotide.charAt(j), index);
//                    index++;
//                }
//            }
//        }
//        //Secondary structure
//        for(int i=0; i<secondaryNotation.length(); i++)
//        {
//            for(int j=0; j<secondaryNotation.length(); j++)
//            {
//                for(int k=0; k<secondaryNotation.length(); k++)
//                {
//                    encodeMap.put("" + secondaryNotation.charAt(i) + secondaryNotation.charAt(j) + secondaryNotation.charAt(k), index);
//                    index++;
//                }
//            }
//        }
//        
//        vector = new double[4 + (this.kSpace + 1)*16 + 3*3*3];
//        nussAlign = new NussinovAlignment();
//    }
    
    private void EncodeFlanking(String nucleotideSeq)
    {
        //upstream
        String nucleotide;
        for(int i=0; i<up; i++)
        {
            nucleotide = "" + nucleotideSeq.charAt(i);
            if(nucleotideMap.containsKey(nucleotide))
                vector[i] = nucleotideMap.get(nucleotide);
            else
                vector[i] = -1;
        }
        //downstream
        for(int i=up+1; i<=up+down; i++)
        {
            nucleotide = "" + nucleotideSeq.charAt(i);
            if(nucleotideMap.containsKey(nucleotide))
                vector[i - 1] = nucleotideMap.get(nucleotide);
            else
                vector[i - 1] = -1;
        }
    }
    
//    private void EncodeKPair(String nucleotideSeq)
//    {
//        //Single nucleotide
//        int index;
//        String singleStr;
//        for(int i=0; i<nucleotideSeq.length(); i++)
//        {
//            singleStr = "" + nucleotideSeq.charAt(i);
//            if(encodeMap.containsKey(singleStr))
//            {
//                index = encodeMap.get(singleStr);
//                vector[index]++;
//            }
////            else
////                System.out.println("Unclear nucleotide " + singleStr);
//        }
//        //k-space nucleotide
//        String kSpaceStr;
//        for(int k=0; k<=kSpace; k++)
//        {
//            for(int i=0; i<= (nucleotideSeq.length()-(k+2)); i++)
//            {
//                kSpaceStr = "" + k + nucleotideSeq.charAt(i) + nucleotideSeq.charAt(i + k + 1);
//                if(encodeMap.containsKey(kSpaceStr))
//                {
//                    index = encodeMap.get(kSpaceStr);
//                    vector[index]++;
//                }
////                else
////                    System.out.println("Unclear nucleotide " + kSpaceStr);
//            }
//        }
//    }
    
    private void EncodeSecondaryStructure(String secondarySeq)
    {
        for(int i=0; i<=secondarySeq.length()-3; i++)
        {
            String secStrutSeg = secondarySeq.substring(i, i+3);
            if(encodeMap.containsKey(secStrutSeg))
            {
                int index = encodeMap.get(secStrutSeg);
                vector[index]++;
            }
//            else
//                System.out.println("Unknown secondary structure " + secStrutSeg);
        }
    }
    
    private void ClearVector()
    {
        for(int i=0; i<vector.length; i++)
            vector[i] = 0;
    }
    
    public void Encode(String nucleotideSeq)
    {
        ClearVector();
        EncodeFlanking(nucleotideSeq);
//        EncodeKPair(nucleotideSeq);
//        nussAlign.setSequence(nucleotideSeq);
//        nussAlign.PredictSecondaryStructure();
//        String secondarySeq = nussAlign.getStructure();
//        EncodeSecondaryStructure(secondarySeq);
    }
    
    public double[] getVector()
    {
        return vector;
    }
}
