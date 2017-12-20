package Basic.VCF;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Ben on 2016/11/7.
 */
public class SampleVCFMutation
{
    private HashMap<String, LinkedList<VCFRecord>> vcfRecMap = new HashMap<String, LinkedList<VCFRecord>>();

    public HashMap<String, LinkedList<VCFRecord>> getVcfRecMap() {
        return vcfRecMap;
    }

    public void addVCFMutation(String geneStr, VCFRecord vcfRec)
    {
        LinkedList<VCFRecord> vcfRecList;
        if(vcfRecMap.containsKey(geneStr))
        {
            vcfRecList = vcfRecMap.get(geneStr);
            vcfRecList.add(vcfRec);
        }
        else
        {
            vcfRecList = new LinkedList<VCFRecord>();
            vcfRecList.add(vcfRec);
            vcfRecMap.put(geneStr, vcfRecList);
        }
    }
}
