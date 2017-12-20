/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic.MAF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 *Format ensembl chromosome name to UCSC chromosome name
 * @author Ben
 */
public class Chromosome 
{
    private HashMap<String, String> ensemblToUcscMap;
    private HashMap<String, String> ucscToEnsemblMap;
    
    public Chromosome()
    {
        try
        {
            ensemblToUcscMap = new HashMap();
            ucscToEnsemblMap = new HashMap(); 
            BufferedReader br = new BufferedReader(new InputStreamReader(Chromosome.class.getResourceAsStream("/chrMap.txt")));
            String strLine;
            String[] strArr;
            while(br.ready())
            {
                strLine = br.readLine();
                strArr = strLine.split("\t");
                ensemblToUcscMap.put(strArr[0], strArr[1]);
                ucscToEnsemblMap.put(strArr[1], strArr[0]);
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public String ConvertEnsemblToUCSC(String chrName)
    {
        if(ensemblToUcscMap.containsKey(chrName))
            return ensemblToUcscMap.get(chrName);
        else
            return "Unknown";
    }
    
    public String ConvertUCSCToEnsembl(String chrName)
    {
        if(ucscToEnsemblMap.containsKey(chrName))
            return ucscToEnsemblMap.get(chrName);
        else
            return "Unknown";
    }
}
