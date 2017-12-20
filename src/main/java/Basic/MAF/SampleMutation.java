/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic.MAF;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Ben
 */
public class SampleMutation {
    private HashMap<String, LinkedList<MAFRecord>> mafRecMap = new HashMap();

    public HashMap<String, LinkedList<MAFRecord>> getMafRecMap() {
        return mafRecMap;
    }

    public void setMafRecMap(HashMap<String, LinkedList<MAFRecord>> mafRecMap) {
        this.mafRecMap = mafRecMap;
    }
    
    public void addMutationToGene(String geneStr, MAFRecord mafRec)
    {
        LinkedList<MAFRecord> mafRecList;
        if(mafRecMap.containsKey(geneStr))
        {
            mafRecList = mafRecMap.get(geneStr);
            mafRecList.add(mafRec);
        }
        else
        {
            mafRecList = new LinkedList();
            mafRecList.add(mafRec);
            mafRecMap.put(geneStr, mafRecList);
        }
    }
}
