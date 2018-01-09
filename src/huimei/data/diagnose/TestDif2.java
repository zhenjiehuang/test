package huimei.data.diagnose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.alibaba.fastjson.JSON;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.cdss.model.request.PatientProfileRequest;
import com.poi.excel.parse.ExportExcel;

import huimei.data.diagnose.excel.DifExcel;
import huimei.data.diagnose.model.MaysonRecognizeResponse;

public class TestDif2 {
    static PushService pushService = new PushService();
    static String url1 = "http://localhost:8080/apollo";
    static String url2 = "http://localhost:8081/apollo";
    static String service = "/v_3_0/recognize";

    static int count = 0;
    
    static ExecutorService pool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + TestDif2.class.getPackage().getName().replace('.', '\\');
        String fileName = "disease_name.txt";
        // String fileName = "disease.txt";
        List<DifExcel> difs = new ArrayList<DifExcel>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));
            String line = null;
            List<Future> fs = new ArrayList<Future>();
            int c = 0;
            while ((line = br.readLine()) != null) {
                final String str = line;
                // Future<?> f = pool.submit(new Runnable() {
                // @Override
                // public void run() {
                // }
                // });
                // fs.add(f);
                dif(difs, str);
            }

            // for(Future f:fs){
            // f.get();
            // }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExportExcel<DifExcel> excel = new ExportExcel<>(difs, DifExcel.class);
        excel.saveFile("D://dif4.xls");
        System.out.println(count);
    }

    private static void dif(List<DifExcel> difs, String line) {
        PatientProfileRequest req = new PatientProfileRequest();
        req.setSymptom(line);
        String result1 = pushService.push(url1, service, req);
        String result2 = pushService.push(url2, service, req);
        if (!result1.equals(result2)) {

            MaysonRecognizeResponse re1 = JSON.parseObject(JSON.parseObject(result1).getString("body"),
                    MaysonRecognizeResponse.class);
            MaysonRecognizeResponse re2 = JSON.parseObject(JSON.parseObject(result2).getString("body"),
                    MaysonRecognizeResponse.class);

            if (!re1.concains(re2)) {
                DifExcel dif = new DifExcel();
                dif.setStr1(line);
                dif.setStr2(result1);
                dif.setStr3(result2);
                synchronized (difs) {
                    count++;
                    difs.add(dif);
                }
            }

        }
    }
}
