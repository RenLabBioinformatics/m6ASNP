import java.io.*;
import java.util.HashSet;

/**
 * Created by Ben on 2018/2/5.
 */
public class ShuffleData
{
    private HashSet<String> dataSet = new HashSet<>();

    public ShuffleData(File sourceTrainFile, File sourceTestFile)
    {
        dataSet.clear();
        readData(sourceTrainFile);
        readData(sourceTestFile);
    }

    private void readData(File sourceData)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(sourceData));
            String strLine = br.readLine();//skip title
            String[] strArr;
            while (br.ready())
            {
                strLine = br.readLine();
                strArr = strLine.split("\t");
                String setStr = strArr[0] + "|" + strArr[1] + "|" + strArr[2] + "|" + strArr[3];
                dataSet.add(setStr);
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void SaveDataSet(File trainDataFile, File testDataFile, double trainPercentage)
    {
        try
        {
            FileWriter fwTrain = new FileWriter(trainDataFile);
            fwTrain.write("Chromosome\tStart\tEnd\tStrand\n");
            FileWriter fwTest = new FileWriter(testDataFile);
            fwTest.write("Chromosome\tStart\tEnd\tStrand\n");

            int trainNum = (int) (dataSet.size() * trainPercentage);
            int testNum = dataSet.size() - trainNum;

            int index = 0;
            for (String str : dataSet)
            {
                index++;
                String[] strArr = str.split("\\|");
                if(index <= trainNum)
                    fwTrain.write(strArr[0] + "\t" + strArr[1] + "\t" + strArr[2] + "\t" + strArr[3] + "\n");
                else
                    fwTest.write(strArr[0] + "\t" + strArr[1] + "\t" + strArr[2] + "\t" + strArr[3] + "\n");
            }

            fwTrain.close();
            fwTest.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        ShuffleData shuffleData = new ShuffleData(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TrainingData_AC\\PositiveMouse.txt"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\PositiveMouse.txt"));
        shuffleData.SaveDataSet(new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\文章攥写\\GigaSicence\\m6ASNP Gigascience\\MouseTrain.txt"),
                new File("G:\\云同步文件夹\\工作文档\\RNA-methylation\\m6AVarAnno\\文章攥写\\GigaSicence\\m6ASNP Gigascience\\MouseTest.txt"),
                0.7);

    }
}
