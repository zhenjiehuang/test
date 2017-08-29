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
public class ErrorSegments {

    static {
        WordDictionary.getInstance().init(Paths.get("D:\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test.class.getPackage().getName().replace('.', '\\');
        String[] fileNames = { "chars.dict", "disease.dict" };
        try {
            for (String fileName : fileNames) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(new File(path, fileName))));

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(new File(resultPath, "识别有问题的分词.csv"), true), charset));

                JiebaSegmenter segmenter = new JiebaSegmenter();

                String line = null;

                while ((line = br.readLine()) != null) {
                    List<SegToken> tokens = segmenter.process(line, JiebaSegmenter.SegMode.SEARCH);
                    if (tokens.size() > 1) {
                        bw.write(line);
                        bw.newLine();
                        for (SegToken token : tokens) {
                            bw.write(token.word);
                            bw.write(",");
                        }

                        bw.newLine();
                        bw.newLine();
                    }
                }

                br.close();
                bw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
