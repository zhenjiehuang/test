package huimei.data.node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

public class TestNode {

    static PushService service = new PushService();

    private static String getNode(String str) {
        NodeSynonym node = new NodeSynonym();
        node.setSynonymWord(str);
        String result = service.push("http://10.27.213.55:8080/apollo", "/getNodeSynonym/getWords", node);
        JSONObject obj = JSONObject.parseObject(result).getJSONObject("body");
        if (obj == null) {
            return null;
        } else {
            return obj.getString("synonymWord");
        }
    }

    private static String getAncestors(String str) {
        NodeSynonym node = new NodeSynonym();
        node.setSynonymWord(str);
        String result = service.push("http://10.27.213.55:8080/apollo", "/getAncestorWords/getWords", node);

        return JSONObject.parseObject(result).getString("body");
    }


    public static void main(String[] args) {
        try {
            ImportExcel<Excel3> im = new ImportExcel<Excel3>(
                    new File("C:\\Users\\Administrator\\Downloads", "不基于接口的检验数据.xlsx"), Excel3.class);
            List<Excel3> datas = im.getRowDatas();

            List<Excel3> results = new ArrayList<>();
            for (Excel3 data : datas) {
                String str = data.getStr();
                String node = getNode(str);
                if (node != null) {
                    String ancestors = getAncestors(str);
                    if (ancestors.length() == 2) {
                        // [] 有节点没有词性
                        data.setR("有节点没有词性");
                    } else {
                        // 有节点，有词性
                        data.setR("有节点，有词性");
                    }
                } else {
                    // 没有节点
                    data.setR("没有节点");
                }
            }

            ExportExcel<Excel3> out = new ExportExcel<>(datas, Excel3.class);
            out.saveFile(new File("D://exam.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
