package huimei.declass;

import java.io.InputStream;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月12日
 * author：huangzhenjie
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        try {
            Class.forName("declass.JarFile");
            String path = "D:\\huimei\\work\\test\\";
            // Process process = Runtime.getRuntime()
            // .exec("java -jar " + path
            // + "decompiler.jar
            // org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregator");
            Process process = Runtime.getRuntime()
                    .exec("java -jar " + path
                            + "decompiler.jar JarFile");
            InputStream in = process.getErrorStream();
            // InputStream in = process.getInputStream();
            // in.read(b)
            byte[] bs = new byte[1024];
            while (in.read(bs) != -1) {
                System.out.println(new String(bs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
