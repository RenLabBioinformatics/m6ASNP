package Annotation.GO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2016/12/7.
 */
public class OboGoTermReader
{
    private HashMap<String, GOTerm> bpTermMap;
    private HashMap<String, GOTerm> mfTermMap;
    private HashMap<String, GOTerm> ccTermMap;

    public OboGoTermReader(String oboFile)
    {
        try
        {
            bpTermMap = new HashMap<>();
            mfTermMap = new HashMap<>();
            ccTermMap = new HashMap<>();

            BufferedReader br = new BufferedReader(new FileReader(oboFile));
            String strLine;
            boolean isTerm = false;
            String name = "", namespace = "", id = "";
            while(br.ready())
            {
                strLine = br.readLine();
                if(strLine.startsWith("[") && strLine.endsWith("]"))
                {
                    if(strLine.equals("[Term]"))
                        isTerm = true;
                    else
                        isTerm = false;
                    name = "";
                    namespace = "";
                    id = "";
                }
                if(isTerm)
                {
                    if(strLine.startsWith("id:"))
                        id = strLine.substring(strLine.indexOf(":") + 1).trim();
                    if(strLine.startsWith("name:"))
                        name = strLine.substring(strLine.indexOf(":") + 1).trim();
                    if(strLine.startsWith("namespace"))
                        namespace = strLine.substring(strLine.indexOf(":") + 1).trim();
                    //write
                    if(strLine.isEmpty())
                    {
                        GOTerm goTerm = new GOTerm();
                        goTerm.setGoID(id);
                        goTerm.setGoTerm(name);
                        if(namespace.equals("biological_process"))
                        {
                            goTerm.setNamespace(GONamespace.BiologicalProcess);
                            bpTermMap.put(goTerm.getGoID(), goTerm);
                        }
                        else if(namespace.equals("cellular_component"))
                        {
                            goTerm.setNamespace(GONamespace.CellularComponent);
                            ccTermMap.put(goTerm.getGoID(), goTerm);
                        }
                        else if(namespace.equals("molecular_function"))
                        {
                            goTerm.setNamespace(GONamespace.MolecularFunction);
                            mfTermMap.put(goTerm.getGoID(), goTerm);
                        }
                        else
                        {
                            goTerm.setNamespace(GONamespace.UnknownNamespace);
                            System.out.println("Find undefined namespace " + namespace + " for term " + id);
                        }
                    }
                }
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public HashMap<String, GOTerm> getBpTermMap() {
        return bpTermMap;
    }

    public HashMap<String, GOTerm> getMfTermMap() {
        return mfTermMap;
    }

    public HashMap<String, GOTerm> getCcTermMap() {
        return ccTermMap;
    }

    public static void main(String[] args)
    {
        OboGoTermReader oboGoTermReader = new OboGoTermReader("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\GO Annotation\\goslim_generic.obo");
        oboGoTermReader.getCcTermMap();
    }
}
