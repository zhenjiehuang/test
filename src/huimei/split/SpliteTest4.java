package huimei.split;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.hm.mayson.module.customer.service.impl.ParserCustomerXuanWu;
import com.hm.mayson.module.progress.model.PatientProgress;
import com.hm.mayson.module.progress.model.ProgressRecordInfo;

public class SpliteTest4 {
    public static void main(String[] args) {
        try {
            ParserCustomerXuanWu xuanwu = new ParserCustomerXuanWu();
            BufferedReader br = new BufferedReader(new FileReader(
                    new File("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\split\\xuanwu.txt")));
            String line = null;
            List<Excel> excels = new ArrayList<Excel>();
            PatientProgress patientProgress = new PatientProgress();
            patientProgress.setProgressType(2);
            while ((line = br.readLine()) != null) {
                patientProgress.setParseMessage(line);
                xuanwu.parserProgress(null, patientProgress);
                ProgressRecordInfo recordInfo = patientProgress.getRecordInfo();
                System.out.println(recordInfo.getSymptom());
                System.out.println(recordInfo.getCurrentDiagnosis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
