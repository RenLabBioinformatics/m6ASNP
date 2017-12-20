package Basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Ben on 2016/12/1.
 */
public class FormatVerify
{
    public static boolean isVCF(String vcfFilePath)
    {
        try
        {
            boolean retVal = true;
            BufferedReader br = new BufferedReader(new FileReader(vcfFilePath));
            String strLine;
            String[] strArr;
            int lineNumber = 0;
            while(br.ready())
            {
                strLine = br.readLine();
                lineNumber++;
                if(lineNumber == 1)
                {
                    if(!strLine.startsWith("##fileformat=VCF"))
                    {
                        retVal = false;
                        break;
                    }
                }
                else
                {
                    if(!strLine.startsWith("#"))
                    {
                        strArr = strLine.split("\t");
                        if(strArr.length < 8)
                        {
                            retVal = false;
                            break;
                        }
                    }
                }
            }
            br.close();
            return retVal;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isTabular(String tabularFilePath)
    {
        try
        {
            boolean retVal = true;
            BufferedReader br = new BufferedReader(new FileReader(tabularFilePath));
            String strLine;
            String[] strArr;
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("#"))
                    continue;
                strArr = strLine.split("\t");
                if(strArr.length < 5)
                {
                    retVal = false;
                    break;
                }
            }
            br.close();
            return retVal;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args)
    {
        System.out.println(FormatVerify.isVCF("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\Test.vcf") + "\t" + FormatVerify.isVCF("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\SCATTER01.js"));
        System.out.println(FormatVerify.isTabular("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\Test.tab") + "\t" + FormatVerify.isTabular("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\SCATTER01.js"));
    }
}
