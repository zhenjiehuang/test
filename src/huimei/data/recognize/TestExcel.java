package huimei.data.recognize;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.parse.ImportExcel;

public class TestExcel {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test.class.getPackage().getName().replace('.', '\\');
        String fileName = "检验识别优化.xlsx";
        String resultPath = "D://1";
        String charset = "GBk";
        try {
            ImportExcel<Excel> excel = new ImportExcel<>(new File(path, fileName), Excel.class);

            // BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
            // new FileOutputStream(new File(resultPath, "检验识别优化.csv")),
            // charset));
            PushService service = new PushService();

            RecordInfo info = new RecordInfo();

            List<List<?>> list = new ArrayList<List<?>>();

            for (Excel data : excel.getRowDatas()) {
                if (data.getData().length() == 0) {
                    continue;
                }

                info.setSymptom(data.getData());

                String str = service.push("http://localhost:8080/apollo", "/intelligent_recognize", info);

                JSONObject result = JSONObject.parseObject(str);

                System.out.println(data.getData());
                JSONArray sentences = result.getJSONObject("body").getJSONArray("sentences");

                list.add(Arrays.asList(data.getData()));
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
            }

            // ExportDynamicExcel export = new ExportDynamicExcel(list);
            // export.saveFile(new File(resultPath, "检验识别优化.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
