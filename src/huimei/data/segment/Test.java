package huimei.data.segment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月21日
 * author：huangzhenjie
 * @version 1.0
 */
public class Test {

    static {
        WordDictionary.getInstance().init(Paths.get("D:\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test.class.getPackage().getName().replace('.', '\\');
        WordDictionary.getInstance().init(Paths.get(path));
        String fileName = "识别有问题的禁忌症.txt";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(resultPath, "识别有问题的禁忌症.csv")), charset));

            JiebaSegmenter segmenter = new JiebaSegmenter();

            String line = null;

            while ((line = br.readLine()) != null) {
                List<SegToken> tokens = segmenter.process(line, JiebaSegmenter.SegMode.SEARCH);
                bw.write(line);
                bw.newLine();
                for (SegToken token : tokens) {
                    bw.write(token.word);
                    bw.write(",");
                }

                bw.newLine();
                bw.newLine();
            }

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
