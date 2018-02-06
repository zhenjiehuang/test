package huimei.data.recognize;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

public class NewAndOldRecognize2 {

    public static void main1(String[] args) {
        try {
            List<Excel> exs = new ArrayList<Excel>();
            Files.lines(Paths.get("D://1\\exam.txt")).forEach(str -> {
                Excel ex = new Excel();
                ex.setData(str);
                if (hasWord(str)) {
                    ex.setHas("是");
                    if (type(str).contains(-1)) {
                        System.out.println(str);
                    } else {
                        ex.setFix("加项目词");
                        exs.add(ex);
                    }
                } else {
                    ex.setHas("否");
                    ex.setFix("不存在");
                    exs.add(ex);
                }
            });

            ExportExcel<Excel> exp = new ExportExcel<>(exs, Excel.class);
            exp.saveFile("D://1/exam.xls");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean hasWord(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);
        PushService pushService = new PushService();
        String result = pushService.push("http://127.0.0.1:8080/apollo", "/getSynonymWord/getWords", info);

        return JSONObject.parseObject(result).get("body") != null;
    }

    private static JSONArray type(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);
        PushService pushService = new PushService();
        String result = pushService.push("http://127.0.0.1:8080/apollo", "/getNodeTypes/getWords", info);

        return JSONObject.parseObject(result).getJSONArray("body");
    }

    private static String node(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);
        PushService pushService = new PushService();
        String result = pushService.push("http://127.0.0.1:8080/apollo", "/getSynonymsByWord/getWords", info);

        return JSONObject.parseObject(result).getString("body");
    }

    private static String parent(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);
        PushService pushService = new PushService();
        String result = pushService.push("http://127.0.0.1:8080/apollo", "/getAncestorWords/getWords", info);

        return JSONObject.parseObject(result).getString("body");
    }

    private static List<String> type(List<?> types) {
        NodeSynonym info = new NodeSynonym();
        List<String> ts = new ArrayList<String>();
        for (Object type : types) {
            info.setNodeId(Long.valueOf(type.toString()));
            PushService pushService = new PushService();
            String result = pushService.push("http://127.0.0.1:8080/apollo", "/getNodeWordByNodeId",
                    info);
            ts.add(result);
        }

        return ts;
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + NewAndOldRecognize2.class.getPackage().getName().replace('.', '\\');
        String fileName = "test.xls";
        // String fileName = "classification.xls";
        try {
            ImportExcel<Excel> in = new ImportExcel<Excel>(new File(path, fileName), Excel.class);
            // ImportExcel<Excel> in = new ImportExcel<>(new File(resultPath,
            // "\\检验.xls"), Excel.class);
            List<Excel> results = new ArrayList<Excel>();
            int i=0;
            for (Excel data : in.getRowDatas()) {
                String data1 = data.getData();
                if (StringUtils.isNotEmpty(data1)) {
                    // String[] ds = data1.split(",");
                    String[] ds = new String[] { data1 };
                    for (String d : ds) {
                        if (StringUtils.isBlank(d)) {
                            continue;
                        }
                        i++;
                        RecordInfo info = new RecordInfo();
                        info.setSymptom(d);
                        try {
                            PushService pushService = new PushService();
                            String str = pushService.push("http://127.0.0.1:8080/apollo",
                                    "/v_3_0/recognize", info);
                            JSONObject result = JSONObject.parseObject(str);
                            JSONArray resultss = result.getJSONObject("body")
                                    .getJSONArray("recognizeResultList");

                            boolean contain = false;
                            boolean lower = false;
                            for (int j = 0; j < resultss.size(); j++) {
                                if (resultss.getJSONObject(j).getString("word").equals(data.getData2())) {
                                    contain = true;
                                    lower = false;
                                    break;
                                }
                                if (resultss.getJSONObject(j).getString("word")
                                        .equalsIgnoreCase(data.getData2())) {
                                    lower = true;
                                }
                            }
                            if (!contain) {
                                Excel ex = new Excel();
                                ex.setData(d);
                                ex.setData2(data.getData2());
                                ex.setCallBacks(resultss.toJSONString());
                                ex.setConcepts(lower + "");
                                results.add(ex);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            System.out.println(i);

            ExportExcel<Excel> export = new ExportExcel<Excel>(results, Excel.class);

            export.saveFile(new File(resultPath, "classification_result_test_31.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}