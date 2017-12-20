package Annotation.GO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2016/12/7.
 */
public class GOTermAnnotationReader
{
    private HashMap<String, LinkedList<String>> annotationMap;

    public GOTermAnnotationReader(String annotationFile)
    {
        try
        {
            annotationMap = new HashMap<>();
            BufferedReader br = new BufferedReader(new FileReader(annotationFile));
            String strLine;
            String[] strArr;
            LinkedList<String> goIDList;
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("!"))
                 continue;
                strArr = strLine.split("\t");
                if(annotationMap.containsKey(strArr[2]))
                    goIDList = annotationMap.get(strArr[2]);
                else
                {
                    goIDList = new LinkedList<>();
                    annotationMap.put(strArr[2], goIDList);
                }
                goIDList.add(strArr[4]);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public HashMap<String, LinkedList<String>> getAnnotationMap()
    {
        return annotationMap;
    }

    public static void main(String[] args)
    {
        GOTermAnnotationReader goTermAnnotationReader = new GOTermAnnotationReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\GO Annotation\\goa_human.gaf");
        goTermAnnotationReader.getAnnotationMap();
    }
}
