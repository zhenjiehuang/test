package huimei.data.recognize;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.poi.excel.parse.ImportExcel;

public class Feibiaojicihui2 {

    public static void main(String[] args) {
        String resultPath = "D://1";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Feibiaojicihui2.class.getPackage().getName().replace('.', '\\');
        String fileName = "非标记识别词汇.xlsx";
        try {

            Map<String, List<String>> map = new HashMap<String, List<String>>();

            ImportExcel<Excel> excel = new ImportExcel<>(new File(path, fileName), Excel.class);

            PushService service = new PushService();

            Map<String, Object> request = new HashMap<>();
            for (Excel data : excel.getRowDatas()) {
                if (data.getData().length() == 0) {
                    continue;
                }

                request.put("synonymWord", data.getData());

                String str = service.push("http://localhost:8080/apollo", "/getNodeSynonym/getWords",
                        request);

                JSONObject result = JSONObject.parseObject(str);

                // System.out.println(data.getData());
                JSONObject body = result.getJSONObject("body");

                if (body == null) {
                    System.out.println(data.getData());
                }
            }

            for (Entry<String, List<String>> entry : map.entrySet()) {
                // entry.getKey()
                System.out.println(entry.getValue().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
