/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.SecondaryAlignment;

import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class NussinovAlignment {
    private String sequence;
    private StringBuilder structure;
    private int[][] scoreMatrix;
    NuclMatchMatrixReader nuclMMReader;
    private LinkedList<MatchPair> matchPairList;
    
    public NussinovAlignment()
    {
        nuclMMReader = new NuclMatchMatrixReader();
        matchPairList = new LinkedList();
        structure = new StringBuilder();
    }
    
    public void setSequence(String inputSeq)
    {
        sequence = inputSeq;
        scoreMatrix = new int[sequence.length()][sequence.length()];
    }
    
    public NussinovAlignment(String inputSeq)
    {
        sequence = inputSeq;
        scoreMatrix = new int[sequence.length()][sequence.length()];
        nuclMMReader = new NuclMatchMatrixReader();
        structure = new StringBuilder();
    }
    
    private void InitializeMatrix()
    {
        for(int i=0; i<sequence.length(); i++)
        {
            scoreMatrix[i][i] = 0;
            if(i - 1 >= 0)
                scoreMatrix[i][i - 1] = 0;
        }
    }
    
    private int FindBifurcation(int indexA, int indexB)
    {
        int maxVal = Integer.MIN_VALUE;
        for(int k = indexA + 1; k < indexB; k++)
        {
            if( (scoreMatrix[indexA][k] + scoreMatrix[k+1][indexB]) > maxVal)
                maxVal = scoreMatrix[indexA][k] + scoreMatrix[k+1][indexB];
        }
        return maxVal;
    }
    
    private void FillMatrix()
    {
        int maxScore;
        int matchScore, unmatchIScore, unmatchJScore, bifurcationScore;
        int indexA, indexB;
        for(int j=1; j<sequence.length(); j++)
        {
            for(int i=0; i<=sequence.length() - 1 - j; i++)
            {
                indexA = i;
                indexB = i + j;
                matchScore = scoreMatrix[indexA + 1][indexB - 1] + nuclMMReader.getMatchScore(sequence.charAt(indexA), sequence.charAt(indexB));
                unmatchIScore = scoreMatrix[indexA + 1][indexB];
                unmatchJScore = scoreMatrix[indexA][indexB - 1];
                bifurcationScore = FindBifurcation(indexA, indexB);
                
                maxScore = matchScore;
                if(unmatchIScore > maxScore)
                    maxScore = unmatchIScore;
                if(unmatchJScore > maxScore)
                    maxScore = unmatchJScore;
                if(bifurcationScore > maxScore)
                    maxScore = bifurcationScore;
                
                scoreMatrix[indexA][indexB] = maxScore;
            }
        }
    }
    
    private void Traceback(int i, int j)
    {
        
        if(j <= i)
            return;
        else if(scoreMatrix[i+1][j] == scoreMatrix[i][j])
            Traceback(i+1, j);
        else if(scoreMatrix[i][j-1] == scoreMatrix[i][j])
            Traceback(i, j-1);
        else if(scoreMatrix[i+1][j-1] + nuclMMReader.getMatchScore(sequence.charAt(i), sequence.charAt(j)) == scoreMatrix[i][j])
        {
            MatchPair matchPair = new MatchPair(i, j);
            matchPairList.add(matchPair);
            Traceback(i+1, j-1);
        }
        else
        {
            for(int k=i+1; k<j; k++)
            {
                if(scoreMatrix[i][k]+scoreMatrix[k+1][j] == scoreMatrix[i][j])
                {
                    Traceback(k+1, j);
                    Traceback(i, k);
                    break;
                }
            }
        }
    }
    
    private void PrintScoreMatrix()
    {
        for(int i=0; i<scoreMatrix.length; i++)
        {
            for(int j=0; j<scoreMatrix[i].length; j++)
                System.out.print("\t" + scoreMatrix[i][j]);
            System.out.println("\n");
        }
    }
    
    private void FillStructure()
    {
        structure.delete(0, structure.length());
        for(int i=0; i<sequence.length(); i++)
            structure.append(".");
        for(MatchPair matchPair : matchPairList)
        {
            structure.setCharAt(matchPair.getMatchI(), '(');
            structure.setCharAt(matchPair.getMatchJ(), ')');
        }
    }
    
    public void PredictSecondaryStructure()
    {
        InitializeMatrix();
        FillMatrix();
//        PrintScoreMatrix();
        matchPairList.clear();
        Traceback(0, sequence.length()-1);
        FillStructure();
//        System.out.println("Traceback score matrix finished!");
    }
    
    public String getStructure()
    {
        return structure.toString();
    }
    
    public static void main(String[] args) 
    {
        NussinovAlignment nussAlign = new NussinovAlignment();
        nussAlign.setSequence("GGGGAGCCTCACCACGAGCTGCCCCCAGGGAGCACTAAGCGAGGTAAGCAAGCAGGACAAGAAGCGGTGG");
        nussAlign.PredictSecondaryStructure();
        System.out.println(nussAlign.getStructure());
    }
}
