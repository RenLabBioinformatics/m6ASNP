package Annotation.GO;

/**
 * Created by Ben on 2016/12/7.
 */
public class GOTerm
{
    private String goID;
    private String goTerm;
    private int namespace;

    public String getGoID() {
        return goID;
    }

    public void setGoID(String goID) {
        this.goID = goID;
    }

    public String getGoTerm() {
        return goTerm;
    }

    public void setGoTerm(String goTerm) {
        this.goTerm = goTerm;
    }

    public int getNamespace() {
        return namespace;
    }

    public void setNamespace(int namespace) {
        this.namespace = namespace;
    }
}
