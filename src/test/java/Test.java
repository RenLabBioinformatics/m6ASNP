/**
 * Created by Ben on 2016/11/8.
 */
public class Test
{
    public static void main(String[] args)
    {
        StringBuilder SourceStr = new StringBuilder("TCGA");
        StringBuilder deleteStr = SourceStr.delete(0, 2);
        System.out.println(deleteStr);

        StringBuilder insertStr = SourceStr.insert(2, "TT");
        System.out.println(insertStr);
    }
}
