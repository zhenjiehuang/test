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
import com.poi.excel.parse.ExportDynamicExcel;
import com.poi.excel.parse.ImportExcel;

public class Jianchajieguo {

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Jianchajieguo.class.getPackage().getName().replace('.', '\\');
        String fileName = "检查结果.xls";
        // String fileName = "建德胸痛病例.txt";
        try {
            ImportExcel<Excel> in = new ImportExcel<>(new File(path, fileName), Excel.class);
            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));
            List<List<?>> list = new ArrayList<List<?>>();
            for (Excel data : in.getRowDatas()) {
                try {
                    System.out.println(data.getData());
                    RecordInfo info = new RecordInfo();
                    info.setSymptom(data.getData());

                    PushService pushService = new PushService();

                    String str = pushService.push("http://localhost:8080/apollo", "/intelligent_recognize",
                            info);

                    JSONObject result = JSONObject.parseObject(str);

                    JSONArray sentences = result.getJSONObject("body").getJSONArray("sentences");

                    list.add(Arrays.asList(data.getData()));
                    list.add(Arrays.asList(result.getString("concepts")));
                    list.add(new ArrayList<>());

                    for (int i = 0; i < sentences.size(); i++) {
                        JSONObject sentence = sentences.getJSONObject(i);
                        list.add(Arrays.asList(sentence.getString("sentence")));

                        JSONArray words = sentence.getJSONArray("words");
                        List<String> word = new ArrayList<String>();
                        for (int j = 0; j < words.size(); j++) {
                            JSONObject obj = words.getJSONObject(j);
                            if (obj.getString("types") != null && !obj.getString("types").equals("[]")) {
                                word.add(obj.getString("word"));
                            }
                        }
                        list.add(word);
                        list.add(new ArrayList<>());
                    }
                    list.add(new ArrayList<>());
                    list.add(new ArrayList<>());
                    list.add(Arrays
                            .asList("///////////////////////////////////////////////////////////////////"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ExportDynamicExcel export = new ExportDynamicExcel(list);
            export.saveFile(new File(resultPath, "检查结果-结果.xls"));
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
