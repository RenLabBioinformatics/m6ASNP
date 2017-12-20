/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic;

/**
 *
 * @author Ben
 */
public class Tools {
    public static String reverseSequence(String nucleotideSeq)
    {
        StringBuilder retStr = new StringBuilder("");
        for(int i=0; i<nucleotideSeq.length(); i++)
        {
            switch(nucleotideSeq.charAt(i))
            {
                case 'A':
                    retStr.append("T");
                    break;
                case 'T':
                    retStr.append("A");
                    break;
                case 'C':
                    retStr.append("G");
                    break;
                case 'G':
                    retStr.append("C");
                    break;
                default:
                    retStr.append("N");
            }
        }
        retStr = retStr.reverse();
        return retStr.toString();
    }
    
    public static char reverseNucleotide(char nucleotide)
    {
        char retVal;
        switch(nucleotide)
        {
            case 'A':
                retVal = 'T';
                break;
            case 'T':
                retVal = 'A';
                break;
            case 'C':
                retVal = 'G';
                break;
            case 'G':
                retVal = 'C';
                break;
            default:
                retVal = 'N';
                break;
        }
        return retVal;
    }

    public static boolean isNucleotideSequence(String seq)
    {
        String nucleotideStr = "ATCGN*";
        boolean retVal = true;
        for(int i=0; i<seq.length(); i++)
        {
            if(nucleotideStr.indexOf(seq.charAt(i)) < 0)
            {
                retVal = false;
                break;
            }
        }
        return retVal;
    }

    public static boolean isOverlaps(int startQuery, int endQuery, int startTarget, int endTarget)
    {
        if (startQuery < startTarget) 
        {
            if (endQuery >= startTarget) 
                return true;
            else
                return false;
        } 
        else 
        {
            if (endQuery <= endTarget) 
                return true;
            else 
            {
                if (startQuery <= endTarget)
                    return true;
                else
                    return false;
            }
        }
    }
    
    public static String ConvertUCSCToEnsembl(String ucscChromosome)
    {
        String ensemblChromosome;
        if(ucscChromosome.startsWith("chr"))
        {
            ensemblChromosome = ucscChromosome.substring(3, ucscChromosome.length());
            if(ensemblChromosome.equals("M"))
                ensemblChromosome = "MT";
        }
        else
            ensemblChromosome = ucscChromosome;
        return ensemblChromosome;
    }
}
