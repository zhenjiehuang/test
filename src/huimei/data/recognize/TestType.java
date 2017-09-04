package huimei.data.recognize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

public class TestType {

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
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + TestType.class.getPackage().getName().replace('.', '\\');
        String fileName = "识别词典.xlsx";

        try {
            ImportExcel<Excel> excel = new ImportExcel<>(new File(path, fileName), Excel.class);
            List<Excel> datas = excel.getRowDatas();
            System.out.println(datas.size());
            List<Excel> results = new ArrayList<Excel>();
            for (Excel data : datas) {
                if (hasNode(data.getData())) {
                    if (!hasParent(data.getData())) {
                        data.setData2("无词性");
                        results.add(data);
                    }
                } else {
                    data.setData2("无词");
                    results.add(data);
                }
            }

            ExportExcel<Excel> export = new ExportExcel<Excel>(results, Excel.class);
            export.saveFile(new File(resultPath, "识别词典.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
