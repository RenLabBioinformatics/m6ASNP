/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package algorithm.RandomForest;

import algorithm.RandomForest.Tree.DataType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class DataReader 
{
    private ArrayList<Integer> dataAttrTypeList = new ArrayList();
    private ArrayList<double[]> dataList = new ArrayList();
    
    public DataReader(String dataFile)
    {
        try
        {
            dataAttrTypeList.clear();
            dataList.clear();
            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            String strLine = br.readLine();
            String[] strArr;
            if(strLine.startsWith("@DataType"))
            {
                //parse data type
                strArr = strLine.split("\t");
                for(int i=1; i<strArr.length; i++)
                {
                    if(strArr[i].equals("Conti"))
                        dataAttrTypeList.add(DataType.Continuous);
                    else if(strArr[i].equals("Discr"))
                        dataAttrTypeList.add(DataType.Discrete);
                    else
                    {
                        System.out.println("Find error type! Replaced by continuous.");
                        dataAttrTypeList.add(DataType.Continuous);
                    }
                }
                while(br.ready())
                {
                    strLine = br.readLine();
                    strArr = strLine.split("\t");
                    double[] dataRec = new double[strArr.length];
                    for(int i=0; i<strArr.length; i++)
                    {
                        dataRec[i] = Double.parseDouble(strArr[i]);
                    }
                    dataList.add(dataRec);
                }
            }
            else
                System.err.println("Data type not defined!");
            br.close();
        }
        catch(IOException e)
        {
            
        }
    }

    public ArrayList<Integer> getDataAttrTypeList() {
        return dataAttrTypeList;
    }

    public ArrayList<double[]> getDataList() {
        return dataList;
    }
}
