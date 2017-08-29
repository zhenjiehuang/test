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

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.model.NodeSynonym;
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
public class Test2 {

    static {
        WordDictionary.getInstance().init(Paths.get("D:\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }

    static PushService service = new PushService();

    private static boolean hasNode(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getNodeSynonym/getWords", info);

        return JSONObject.parseObject(result).getJSONObject("body") != null;
    }

    private static boolean hasParent(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getTopLevelNodes/getWords", info);

        return JSONObject.parseObject(result).get("body") != null;
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test2.class.getPackage().getName().replace('.', '\\');
        WordDictionary.getInstance().init(Paths.get(path));
        String fileName = "segment.txt";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(resultPath, "segment.csv")), charset));

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
                for (SegToken token : tokens) {
                    bw.write(hasParent(token.word) + "");
                    bw.write(",");
                }

                bw.newLine();
                for (SegToken token : tokens) {
                    bw.write(hasNode(token.word) + "");
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
