/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VariantPredict;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Ben
 */
public class ParameterReader {
    private ParameterRecord paramRec = new ParameterRecord();
    
    public ParameterReader(String paramFile)
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(ParameterReader.class.getResourceAsStream(paramFile)));
            String strLine, name, value;
            while(br.ready())
            {
                strLine = br.readLine();
                String[] strArr = strLine.split("=");
                name = strArr[0].trim();
                value = strArr[1].trim();
                if(name.equals("Up"))
                    paramRec.setUp(Integer.parseInt(value));
                else if(name.equals("Down"))
                    paramRec.setDown(Integer.parseInt(value));
                else if(name.equals("High"))
                    paramRec.setHighCutoff(Double.parseDouble(value));
                else if(name.equals("Medium"))
                    paramRec.setMediumCutoff(Double.parseDouble(value));
                else if(name.equals("Low"))
                    paramRec.setLowCutoff(Double.parseDouble(value));
                else if(name.equals("All"))
                    paramRec.setAllCutoff(Double.parseDouble(value));
            }
            br.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public ParameterRecord getParamRec() {
        return paramRec;
    }
    
    public static void main(String[] args) {
        String str = "High = 0.56";
        String[] strArr = str.split("=");
        System.out.println(strArr[0].trim() + "\t" + strArr[1].trim());
    }
}
