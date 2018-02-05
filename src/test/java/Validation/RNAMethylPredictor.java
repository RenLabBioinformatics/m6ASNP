package Validation;

import FASTA.Fasta;
import FASTA.FastaReader;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Ben on 2018/1/22.
 */
public class RNAMethylPredictor
{
    private String baseURL = "http://lin-group.cn/server/iRNAMethyl/pre.php";
    private LinkedList<Fasta> fastaList;
    private LinkedList<String> resultLineList = new LinkedList();
    private String htmlMarkPattern = "<[^>]+>";

    public RNAMethylPredictor(LinkedList<Fasta> fastaList)
    {
        this.fastaList = fastaList;
    }

    private String replaceAllHTMLMarks(String strLine)
    {
        String retStr = strLine.replaceAll(htmlMarkPattern, "");
        return retStr;
    }

    private void Predict(String sequence, ArrayList<String> fastaNameList)
    {
        try
        {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(baseURL);
            ArrayList<NameValuePair> paramList = new ArrayList();
            paramList.add(new BasicNameValuePair("sequence", sequence));
            paramList.add(new BasicNameValuePair("B1", "Submit"));
            UrlEncodedFormEntity urlEntity = new UrlEncodedFormEntity(paramList, "UTF-8");
            httpPost.setEntity(urlEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String htmlStr = "";
            int nameIndex = 0;
            if(entity != null)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()));
                String strLine = br.readLine();
                while (strLine != null)
                {
                    if(strLine.contains("nucleotides long and contains") || strLine.contains("The A"))
                    {
                        htmlStr = replaceAllHTMLMarks(strLine);
                        if(strLine.contains("nucleotides long and contains"))
                        {
                            htmlStr = htmlStr.replace("Sequence-" + (nameIndex + 1), fastaNameList.get(nameIndex));
                            nameIndex++;
                        }
                        resultLineList.add(htmlStr);
                    }
//                    System.out.println(strLine);
                    strLine = br.readLine();
                }
                br.close();
            }
            else
                System.out.println("Entity error: null entity!");
            response.close();
            httpClient.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void PredictFasta(int numPerRun)
    {
        resultLineList.clear();
        int count = 0;
        String seq = "";
        int runCount = 0;
        ArrayList<String> fastaNameList = new ArrayList<>();
        for(Fasta fas : fastaList)
        {
            String s = fas.getSequence().replace('T', 'U');
            seq = seq + ">" + fas.getFastaName() + "\r\n" + s + "\r\n";
            fastaNameList.add(fas.getFastaName());
            count ++;
            if(count > numPerRun)
            {
                Predict(seq, fastaNameList);
                count = 0;
                seq = "";
                runCount++;
                System.out.println("Finish " + runCount + " run.");
                fastaNameList.clear();
            }
        }
        if(!seq.isEmpty())
        {
            Predict(seq, fastaNameList);
            runCount++;
            System.out.println("Finish " + runCount + " run.");
            fastaNameList.clear();
        }
    }

    public void SaveResult(String saveFile)
    {
        try
        {
            FileWriter fw = new FileWriter(saveFile);
            for(String strLine : resultLineList)
            {
                fw.write(strLine + "\n");
            }
            fw.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FastaReader fasReader = new FastaReader();
        fasReader.ReadFromFile("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\NegativeMouse.fas");
        RNAMethylPredictor m6aP = new RNAMethylPredictor(fasReader.getFastaList());
        m6aP.PredictFasta(2000);
        m6aP.SaveResult("G:\\云同步文件夹\\工作文档\\RNA-methylation\\NewModel\\TestData\\NegativeMouse_m6APre_Res.txt");
    }
}
