/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Basic.FDR;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author zhengyueyuan
 */
public class FDRControl {

    private LinkedList<FDRInputRecord> recordList;

    public FDRControl(LinkedList<FDRInputRecord> recordList) {
        this.recordList = recordList;
        this.process();
    }

    private void process() {
        Collections.sort(recordList, new AscendingOrder());
        int p_length = recordList.size();
        double FDR = 0;
        int rank = 0;
        LinkedList<Double> frdValueList = new LinkedList();
        for (FDRInputRecord dataRecord : recordList) {
            rank = rank + 1;
            FDR = dataRecord.getPValue() * p_length / rank;
            dataRecord.setFDR(FDR);
            frdValueList.add(FDR);
        }
        int index = 0;
        while (true) {
            if (frdValueList.size() > 0) {
                double min = getMin(frdValueList);
                int minIndex = 0;
                for (int i = 0; i < frdValueList.size(); i++) {
                    if (frdValueList.get(i) == min) {
                        minIndex = i;
                        for (int k = 0; k <= minIndex; k++) {
                            recordList.get(index + k).setFDR(min);
                            frdValueList.remove(0);
                        }
                        break;
                    }
                }
                index = index + (minIndex + 1);
            } else {
                break;
            }
        }
    }

    public static double getMin(List<Double> ins) {
        Object[] objs = ins.toArray();
        Arrays.sort(objs);
        return Double.valueOf(String.valueOf(objs[0]));
    }
}

//class SortByAge implements Comparator {
//    public int compare(Object o1, Object o2){
//        FDRInputRecord b1 = (FDRInputRecord) o1;
//        FDRInputRecord b2 = (FDRInputRecord) o2;
//        if (b1.getPValue() > b2.getPValue()) {
//            return 1;
//        } else if (b1.getPValue() < b2.getPValue()) {
//            return -1;
//        } else {
//            return 0;
//        }
//    }
//}
