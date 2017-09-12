package huimei.data.recognize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.pojo.RecordInfo;

public class Test {

    public static void main(String[] args) {
        String resultPath = "D://1//1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test.class.getPackage().getName().replace('.', '\\');
        String fileName = "建德线上辅助检查.txt";
//        String fileName = "建德胸痛病例.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));
            String line = null;
            int count = 0;
            while ((line = br.readLine()) != null) {
                RecordInfo info = new RecordInfo();
                info.setSymptom(line);

                PushService pushService = new PushService();

                String str = pushService.push("http://localhost:8080/apollo", "/intelligent_recognize", info);

                JSONObject result = JSONObject.parseObject(str);

                JSONArray sentences = result.getJSONObject("body").getJSONArray("sentences");

                List<List<?>> list = new ArrayList<List<?>>();
                list.add(Arrays.asList(line));

                for (int i = 0; i < sentences.size(); i++) {
                    JSONObject sentence = sentences.getJSONObject(i);
                    list.add(Arrays.asList(sentence.getString("sentence")));

                    JSONArray words = sentence.getJSONArray("words");
                    List<String> word = new ArrayList<String>();
                    for (int j = 0; j < words.size(); j++) {
                        word.add(words.getJSONObject(j).getString("word"));
                    }
                    list.add(word);

                    List<String> types = new ArrayList<String>();
                    for (int j = 0; j < words.size(); j++) {
                        types.add(words.getJSONObject(j).getString("types"));
                    }

                    list.add(types);
                    list.add(new ArrayList<>());
                }

                // ExportDynamicExcel export = new ExportDynamicExcel(list);
                // export.saveFile(new File(resultPath, count++ + ".xls"));
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
