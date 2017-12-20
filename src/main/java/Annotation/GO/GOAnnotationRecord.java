package Annotation.GO;

/**
 * Created by Ben on 2016/12/8.
 */
public class GOAnnotationRecord
{
    private String goTerm;
    private int number;
    private double percentage;

    public String getGoTerm() {
        return goTerm;
    }

    public void setGoTerm(String goTerm) {
        this.goTerm = goTerm;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
