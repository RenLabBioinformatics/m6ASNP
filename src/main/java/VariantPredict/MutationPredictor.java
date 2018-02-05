/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import algorithm.EncodeScheme;
import algorithm.RandomForest.aggregation.RandomForest;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class MutationPredictor {
    private RandomForest randomForest;
    private EncodeScheme encode;
    private HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap;
    private ParameterRecord paramRec;
    
    public MutationPredictor(HashMap<String, LinkedList<MethylationMutationRecord>> methyMutMap, String species)
    {
        this.methyMutMap = methyMutMap;
        if(species.equalsIgnoreCase("Human"))
        {
            ParameterReader paramReader = new ParameterReader("/Model/ParamHuman.txt");
            paramRec = paramReader.getParamRec();
            randomForest = new RandomForest("/Model/MethylationRFModelHuman.txt");
        }
        else if(species.equalsIgnoreCase("Mouse"))
        {
            ParameterReader paramReader = new ParameterReader("/Model/ParamMouse.txt");
            paramRec = paramReader.getParamRec();
            randomForest = new RandomForest("/Model/MethylationRFModelMouse.txt");
        }
        else
        {
            System.out.println("Unsupported species " + species + ". Set to human model.");
            ParameterReader paramReader = new ParameterReader("/Model/ParamHuman.txt");
            paramRec = paramReader.getParamRec();
            randomForest = new RandomForest("/Model/MethylationRFModelHuman.txt");
        }
        encode = new EncodeScheme(paramRec.getUp(), paramRec.getDown());
    }
    
    public void Predict(String cutoff)
    {
        double cutoffValue = paramRec.getCutoff(cutoff);
        double classIndex;
        double score;
        for(String sampleID : methyMutMap.keySet())
        {
            LinkedList<MethylationMutationRecord> methyMutRecList = methyMutMap.get(sampleID);
            for(MethylationMutationRecord methyMutRec : methyMutRecList)
            {
                //Predict reference
                boolean isRefPositive = false;
                encode.Encode(methyMutRec.getReferenceSeq());
                double[] refVector = encode.getVector();
                classIndex = randomForest.PredictValue(refVector);
                if(classIndex == 1)
                    score = randomForest.getScore(classIndex);
                else
                    score = 1 - randomForest.getScore(classIndex);
                methyMutRec.setReferenceScore(score);
                if(score > cutoffValue)
                    isRefPositive = true;
                else
                    isRefPositive = false;
                
                //Predict tumor
                boolean isTumorPositive = false;
                if(methyMutRec.getMutationSeq().substring(paramRec.getUp(), paramRec.getUp() + 2).equals("AC"))
                {
//                    if(methyMutRec.getMutationSeq().length() != (paramRec.getUp() + paramRec.getDown() + 1))
//                        System.out.println("Got");
                    encode.Encode(methyMutRec.getMutationSeq());
                    double[] tumorVector = encode.getVector();
                    classIndex = randomForest.PredictValue(tumorVector);
                    if(classIndex == 1)
                        score = randomForest.getScore(classIndex);
                    else
                        score = 1 - randomForest.getScore(classIndex);
                    methyMutRec.setMutationScore(score);
                    if(score > cutoffValue)
                        isTumorPositive = true;
                    else
                        isTumorPositive = false;
                }
                else
                {
                    methyMutRec.setMutationScore(-1);
                    isTumorPositive = false;
                }
                
                //Compare change
                methyMutRec.setChangeSig(-1 * Math.log10(methyMutRec.getReferenceScore()/methyMutRec.getMutationScore()));
                if(isRefPositive)
                {
                    if(isTumorPositive)
                        methyMutRec.setMutationEvent(MutationEvent.MethylationNotChange);
                    else
                        methyMutRec.setMutationEvent(MutationEvent.FunctionalLoss);
                }
                else
                {
                    if(isTumorPositive)
                        methyMutRec.setMutationEvent(MutationEvent.FunctionalGain);
                    else
                        methyMutRec.setMutationEvent(MutationEvent.NonMethylationNotChange);
                }
            }
        }
    }
}
