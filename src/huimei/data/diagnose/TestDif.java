package huimei.data.diagnose;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.cdss.model.request.PatientProfileRequest;
import com.poi.excel.parse.ExportExcel;

import huimei.data.diagnose.excel.DifExcel;

public class TestDif {
    static PushService pushService = new PushService();
    static String url1 = "http://localhost:8080/apollo";
    static String url2 = "http://localhost:8081/apollo";
    static String service = "/v_3_0/recognize";

    static int count = 0;
    
    static ExecutorService pool = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + TestDif.class.getPackage().getName().replace('.', '\\');
        String fileName = "disease_name.txt";
        // String fileName = "disease.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));
            String line = null;
            List<DifExcel> difs = new ArrayList<DifExcel>();
            List<Future> fs = new ArrayList<Future>();
            int c = 0;
            while ((line = br.readLine()) != null && c++ < 100) {
                final String str = line;
                Future<?> f = pool.submit(new Runnable() {
                    @Override
                    public void run() {
                        dif(difs, str);
                    }
                });
                fs.add(f);
            }

            for(Future f:fs){
                f.get();
            }
            ExportExcel<DifExcel> excel = new ExportExcel<>(difs, DifExcel.class);
            excel.saveFile("D://dif3.xls");
            System.out.println(count);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void dif(List<DifExcel> difs, String line) {
        PatientProfileRequest req = new PatientProfileRequest();
        req.setSymptom(line);
        String result1 = pushService.push(url1, service, req);
        String result2 = pushService.push(url2, service, req);
        // result1.length() 334
        // result2.length() 337
        if (!result1.equals(result2)) {
            DifExcel dif = new DifExcel();
            dif.setStr1(line);
            dif.setStr2(result1);
            dif.setStr3(result2);
            if (result1.length() == 334 && result2.length() == 337) {
                dif.setStr4("node无词性");
            } else if (result1.length() == 334) {
                dif.setStr4("增加识别");
            } else if (result2.length() == 334 || result2.length() == 337) {
                dif.setStr4("减少识别");
            }
            synchronized (difs) {
                count++;
                difs.add(dif);
            }
        }
    }
}
