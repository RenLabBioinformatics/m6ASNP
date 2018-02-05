/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithm.SecondaryAlignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public class NuclMatchMatrixReader {
    private HashMap<String, Integer> matchMatrix = new HashMap();
    
    public NuclMatchMatrixReader()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(NuclMatchMatrixReader.class.getResourceAsStream("/NuclMatchMatrix.txt")));
            String strLine = br.readLine().trim();
            String[] tmpArr = strLine.split("\\s+");
            String matrixTitle = "";
            for(int i=0; i<tmpArr.length; i++)
                matrixTitle = matrixTitle + tmpArr[i];
            while(br.ready())
            {
                strLine = br.readLine().trim();
                tmpArr = strLine.split("\\s+");
                String code;
                for(int i=1; i<tmpArr.length; i++)
                {
                    code = tmpArr[0] + matrixTitle.charAt(i - 1);
                    matchMatrix.put(code, Integer.parseInt(tmpArr[i]));
                }
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public int getMatchScore(char A, char B)
    {
        String code = "" + A + B;
        if(matchMatrix.containsKey(code))
            return matchMatrix.get(code);
        else
            return 0;
    }
    
    public static void main(String[] args) {
        NuclMatchMatrixReader nuclMMReader = new NuclMatchMatrixReader();
        int retVal = nuclMMReader.getMatchScore('A', 'G');
        System.out.println(retVal);
    }
}
