package Validation;

import FASTA.Fasta;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by Ben on 2018/1/19.
 */
public class ConvertDataToFasta
{
    private LinkedList<Fasta> fastaList;

    public ConvertDataToFasta(File inputFile)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            String strLine = br.readLine();//skip title
            String[] strArr;
            fastaList = new LinkedList<>();
            int index = 0;
            while(br.ready())
            {
                strLine = br.readLine();
                strArr = strLine.split("\t");
                String seq = strArr[4];

                String fasTitle = "Site" + index;
                Fasta fas = new Fasta();
                fas.setFastaName(fasTitle);
                fas.setSequence(seq);
                fastaList.add(fas);

                index++;
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void SaveFastaFile(File fastaFile)
    {
        try
        {
            FileWriter fw = new FileWriter(fastaFile);
            for(Fasta fas : fastaList)
            {
                fw.write(">" + fas.getFastaName() + "\n");
                fw.write(fas.getSequence() + "\n");
            }
            fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        ConvertDataToFasta convertDataToFasta = new ConvertDataToFasta(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\PositiveMouse.txt"));
        convertDataToFasta.SaveFastaFile(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\PositiveMouse.fas"));
    }
}
