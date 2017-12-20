package Annotation.GO;

import java.util.Comparator;

/**
 * Created by Ben on 2016/12/8.
 */
public class CompareGOAnnotationRecord implements Comparator<GOAnnotationRecord>
{
    @Override
    public int compare(GOAnnotationRecord o1, GOAnnotationRecord o2)
    {
        if(o1.getPercentage() < o2.getPercentage())
            return 1;
        else if(o1.getPercentage() > o2.getPercentage())
            return -1;
        else
            return 0;
    }
}
