/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FASTA;

import FASTA.Fasta;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class FastaReader {
    LinkedList<Fasta> fastaList = new LinkedList();
    
    public void ReadFromFile(String fileName)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String strLine;
            String name = "";
            StringBuffer strBuff = null;
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith(">"))
                {
                    if(strBuff != null)
                    {
                        Fasta fasta = new Fasta();
                        fasta.setFastaName(name);
                        fasta.setSequence(strBuff.toString());
                        fastaList.add(fasta);
                    }
                    name = strLine.substring(1);
                    strBuff = new StringBuffer("");
                }
                else
                    strBuff.append(strLine);
            }
            Fasta fasta = new Fasta();
            fasta.setFastaName(name);
            fasta.setSequence(strBuff.toString());
            fastaList.add(fasta);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void ReadFromString(String fastaStr)
    {
        try
        {
            DataInputStream input = new DataInputStream(new ByteArrayInputStream(fastaStr.getBytes()));
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            String strLine;
            String name = "";
            StringBuffer strBuff = null;
            while (br.ready()) 
            {
                strLine = br.readLine();
                if (strLine.startsWith(">")) 
                {
                    if (strBuff != null) 
                    {
                        Fasta fasta = new Fasta();
                        fasta.setFastaName(name);
                        fasta.setSequence(strBuff.toString());
                        fastaList.add(fasta);
                    }
                    name = strLine.substring(1);
                    strBuff = new StringBuffer("");
                } 
                else 
                {
                    strBuff.append(strLine);
                }
            }
            Fasta fasta = new Fasta();
            fasta.setFastaName(name);
            fasta.setSequence(strBuff.toString().toUpperCase());
            fastaList.add(fasta);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void CleanFastaList()
    {
        fastaList.clear();
    }
    
    public LinkedList<Fasta> getFastaList()
    {
        return fastaList;
    }
    
    public static void main(String[] args) 
    {
        FastaReader fasReader = new FastaReader();
        fasReader.ReadFromFile("");
        LinkedList<Fasta> fastaList = fasReader.getFastaList();
        for(Iterator<Fasta> itr=fastaList.iterator(); itr.hasNext();)
        {
            Fasta fas = itr.next();
            System.out.println(">" + fas.getFastaName());
            System.out.println(fas.getSequence());
        }
    }
}
