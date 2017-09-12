package huimei.data.recognize;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.hm.apollo.service.PushService;
import com.poi.excel.parse.ImportExcel;

public class Feibiaojicihui {

    static PushService service = new PushService();

    private static boolean hasNode(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getNodeSynonym/getWords", info);

        return JSONObject.parseObject(result).getJSONObject("body") != null;
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Feibiaojicihui.class.getPackage().getName().replace('.', '\\');
        String fileName = "非标记识别词汇.xlsx";
        try {

            Map<String, List<String>> map = new HashMap<String, List<String>>();

            ImportExcel<Excel> excel = new ImportExcel<>(new File(path, fileName), Excel.class);

            PushService service = new PushService();

            RecordInfo info = new RecordInfo();

            for (Excel data : excel.getRowDatas()) {
                if (data.getData().length() == 0) {
                    continue;
                }

                info.setSymptom(data.getData());

                String str = service.push("http://localhost:8080/apollo", "/intelligent_recognize", info);

                JSONObject result = JSONObject.parseObject(str);

                // System.out.println(data.getData());
                JSONArray sentences = result.getJSONObject("body").getJSONArray("sentences");

                if (sentences.size() == 1) {
                    JSONArray concepts = sentences.getJSONObject(0).getJSONArray("concepts");
                    if (concepts != null && concepts.size() == 1) {
                        String conceptName = concepts.getJSONObject(0).getString("conceptName");
                        if (!hasNode(data.getData()) && !data.getData().contains(conceptName)) {
                            System.out.println(data.getData());
                            continue;
                        }
                    }
                }
                for (int i = 0; i < sentences.size(); i++) {
                    JSONObject sentence = sentences.getJSONObject(i);

                    JSONArray words = sentence.getJSONArray("words");

                    for (int j = 0; j < words.size(); j++) {
                        if (words.getJSONObject(j).getString("types") == null) {
                            String word = words.getJSONObject(j).getString("word");
                            List<String> list = map.get(word);
                            if (list == null) {
                                map.put(word, list = new ArrayList<String>());
                            }
                            list.add(sentence.getString("sentence"));
                        }
                    }
                }
            }

            List<List<?>> list = new ArrayList<List<?>>();
            for (Entry<String, List<String>> entry : map.entrySet()) {
                list.add(Arrays.asList(entry.getKey(), entry.getValue()));
            }
            
            // ExportDynamicExcel export = new ExportDynamicExcel(list);
            // export.saveFile(new File(resultPath, "非标记识别词汇.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
