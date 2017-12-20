package Basic.VCF;

/**
 * Created by Ben on 2016/11/7.
 */
public class VCFRecord
{
    private String chromosome;
    private int position;
    private String id;
    private String refBase;
    private String mutBase;
    private String comments;

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefBase() {
        return refBase;
    }

    public void setRefBase(String refBase) {
        this.refBase = refBase;
    }

    public String getMutBase() {
        return mutBase;
    }

    public void setMutBase(String mutBase) {
        this.mutBase = mutBase;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String toString()
    {
        return chromosome + "|" + position + "|" + id + "|" + refBase + "|" + mutBase + "|" + comments;
    }

    public String toTabString()
    {
        String retVal = "";
        retVal = chromosome + "\t" + position;
        if(id.isEmpty())
            retVal = retVal + "\t.";
        else
            retVal = retVal + "\t" + id;
        retVal = retVal + "\t" + refBase + "\t" + mutBase;
        return retVal;
    }
}
