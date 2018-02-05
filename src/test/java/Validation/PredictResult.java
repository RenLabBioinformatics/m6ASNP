package Validation;

/**
 * Created by Ben on 2018/1/20.
 */
public class PredictResult
{
    private String chrName;
    private int position;
    private int strand;
    private double Score;

    public PredictResult(String chrName, int position, int strand, double score) {
        this.chrName = chrName;
        this.position = position;
        this.strand = strand;
        Score = score;
    }

    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }
}
