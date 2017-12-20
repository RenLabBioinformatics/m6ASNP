package Basic.Tabular;

import Basic.VCF.VCFRecord;

import java.util.LinkedList;

/**
 * Created by Ben on 2017/9/20.
 */
public class TabSampleMutation
{
    private String sampleID;
    private LinkedList<VCFRecord> vcfRecList = new LinkedList<>();

    public String getSampleID() {
        return sampleID;
    }

    public void setSampleID(String sampleID) {
        this.sampleID = sampleID;
    }

    public void AddVCFRecord(VCFRecord vcfRecord)
    {
        this.vcfRecList.add(vcfRecord);
    }

    public LinkedList<VCFRecord> getVcfRecList() {
        return vcfRecList;
    }
}
