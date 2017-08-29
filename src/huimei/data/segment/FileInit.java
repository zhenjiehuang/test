package huimei.data.segment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月21日
 * author：huangzhenjie
 * @version 1.0
 */
public class FileInit {
    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "utf-8";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test.class.getPackage().getName().replace('.', '\\');
        String fileName = "disease.dict";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(resultPath, "disease1.dict")), charset));

            String line = null;
            while ((line = br.readLine()) != null) {
                bw.write(line.split(" ")[0].trim());
                bw.newLine();
            }

            bw.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
