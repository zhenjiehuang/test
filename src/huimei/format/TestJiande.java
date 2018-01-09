package huimei.format;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

public class TestJiande {
    public static void main(String[] args) {
        try {
            ImportExcel<Excel> im = new ImportExcel<>(new File("D://format.xls"), Excel.class);

            List<Excel> exs = im.getRowDatas();
            PushService service = new PushService();
            for (Excel ex : exs) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("symptom", ex.getData2());
                String result = service.push("http://127.0.0.1:8080/apollo", "/intelligent_recognize",
                        map);

                JSONArray array = JSON.parseObject(result).getJSONObject("body").getJSONArray("concepts");
                List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
                for (int i = 0; i < array.size(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put("conceptName", obj.getString("conceptName"));
                    resultMap.put("occur", obj.getString("occur"));
                    resultMap.put("properties", obj.getString("properties"));

                    results.add(resultMap);
                }
                ex.setData3(JSON.toJSONString(results));
            }

            ExportExcel<Excel> export = new ExportExcel<>(exs, Excel.class);
            export.saveFile("D://format_result.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
