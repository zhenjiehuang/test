package log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Logs {
    public static void main(String[] args) {
        try {
            // C:\Users\Administrator\Downloads
            BufferedReader br = new BufferedReader(
                    new FileReader(new File("C:\\Users\\Administrator\\Downloads\\catalina.out")));

            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ForkJoinPool.commonPool-worker")
                        || line.startsWith("http-nio-10.46.65.250-8099-exec")) {
                    if (
                    //
                    line.contains("redis:")
                    //
                    ) {
                        System.out.println(line);
                    }
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
