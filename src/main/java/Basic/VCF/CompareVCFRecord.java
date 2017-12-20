package Basic.VCF;

import java.util.Comparator;

/**
 * Created by Ben on 2016/11/8.
 */
public class CompareVCFRecord implements Comparator<VCFRecord>
{
    private int strand;

    public CompareVCFRecord(int strand)
    {
        this.strand = strand;
    }

    public int compare(VCFRecord o1, VCFRecord o2)
    {
        if(o1.getPosition() < o2.getPosition())
        {
            if(strand == 0)
                return -1;
            else
                return 1;
        }
        else if(o1.getPosition() > o2.getPosition())
        {
            if(strand == 0)
                return 1;
            else
                return  -1;
        }
        else
            return 0;
    }
}
