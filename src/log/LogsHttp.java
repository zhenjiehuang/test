package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LogsHttp {
    public static void main(String[] args) {
        try {
            // C:\Users\Administrator\Downloads
            BufferedReader br = new BufferedReader(
                    new FileReader(new File("Z:\\workspace\\huimei\\work\\test\\src\\log\\http.log")));

            String line = null;

            while ((line = br.readLine()) != null) {
                if (line.contains("intelligent_recommendation")) {
                    String[] ss = line.split(" ");
                    String cost = ss[ss.length - 1];

                    if (Integer.valueOf(cost) > 100 && Integer.valueOf(cost) < 2000) {
                        System.out.println(ss[ss.length - 1]);
                        // System.out.println(line + " " + ss[ss.length - 1]);
                    }
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
