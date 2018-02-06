package huimei.data.recognize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.framework.utils.BCConvertUtils;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

public class NewAndOldRecognize {

    private static boolean hasWord(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);
        PushService pushService = new PushService();
        String result = pushService.push("http://127.0.0.1:8080/apollo", "/getSynonymWord/getWords", info);

        return JSONObject.parseObject(result).get("body") != null;
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
        path = path + "\\src\\" + NewAndOldRecognize.class.getPackage().getName().replace('.', '\\');
        String fileName = "classification4.xls";
        // String fileName = "test.xls";
        try {
            ImportExcel<Excel> in = new ImportExcel<Excel>(new File(path, fileName), Excel.class);
            // ImportExcel<Excel> in = new ImportExcel<>(new File(resultPath,
            // "\\规则.xls"), Excel.class);
            System.out.println(JSON.toJSONString(in.getErrors()));
            List<Excel> results = new ArrayList<Excel>();
            int i=0;
            for (Excel data : in.getRowDatas()) {
                String data1 = data.getData();
                // data.setData2(BCConvertUtils.quanjiao2banjiao(data.getData2().toLowerCase()));
                if (StringUtils.isNotEmpty(data1)) {
                    String[] ds = data1.split(",");
                    // String[] ds = new String[] { data1 };
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
                                    "/intelligent_recognize", info);
                            JSONObject result = JSONObject.parseObject(str);
                            JSONObject sentence = result.getJSONObject("body").getJSONArray("sentences")
                                    .getJSONObject(0);
                            JSONArray words = sentence.getJSONArray("words");
                            boolean problem = false;
                            Excel ex = new Excel();
                            ex.setData(d);
                            ex.setData2(data.getData2());
                            ex.setHas(hasWord(data.getData2()) ? "是" : "否");
                            // if (words.size() > 1) {
                            // // 分词错误，加词
                            // ex.setFix("加词");
                            // ex.setStr(d);
                            // problem = true;
                            // } else {
                            JSONArray concepts = result.getJSONObject("body").getJSONArray("concepts");
                            if (CollectionUtils.isEmpty(concepts)) {
                                problem = true;
                                ex.setWordType(words.getJSONObject(0).getString("types"));
                                if (StringUtils.isNotEmpty(ex.getWordType())) {
                                    ex.setWordType(
                                            JSON.toJSONString(type(JSON.parseArray(ex.getWordType()))));
                                }

                                if (BCConvertUtils.toBanjiaoLowercase(d)
                                        .equals(BCConvertUtils.toBanjiaoLowercase(data.getData2()))) {
                                    // 不错处理
                                    if (ex.getWordType() == null && !d.equals(data.getData2())) {
                                        // 无词性
                                        ex.setFix("加词");
                                        ex.setStr(d);
                                    }
                                } else if (ex.getWordType() == null && !d.equals(data.getData2())) {
                                    // 无词性
                                    ex.setFix("加词和关系");
                                    ex.setStr(d);
                                } else if (!d.equals(data.getData2())) {
                                    // 词性无法推出
                                    ex.setFix("加关联关系");
                                    ex.setStr(d);
                                }
                            } else {
                                if (concepts.size() == 1) {
                                    // 不包含核心词
                                    if (!contain(concepts.getJSONObject(0).getJSONArray("callbackWords"),
                                            data.getData2()) && !d.equals(data.getData2())) {
                                        // parent
                                        ex.setParent(parent(d));
                                        // node
                                        ex.setNode(node(d));
                                        ex.setConcepts(concepts.getJSONObject(0).getString("conceptName"));
                                        ex.setCallBacks(concepts.getJSONObject(0).getString("callbackWords"));
                                        problem = true;
                                        ex.setFix("加关联关系");
                                        ex.setStr(ex.getConcepts());
                                    }
                                } else {
                                    // 有问题的结果，直接看分词
                                    problem = true;
                                    List<String> strs = concepts.stream()
                                            .map(concept -> ((JSONObject) concept).getString("conceptName"))
                                            .collect(Collectors.toList());

                                    ex.setConcepts(JSON.toJSONString(strs));
                                }
                            }
                            // }

                            if (problem) {
                                ex.setData3(sentence.getString("words"));
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

            export.saveFile(new File(resultPath, "classification_result_test_92.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean contain(JSONArray jsonArray, String callback) {
        for (Object s : jsonArray) {
            if (s.toString().equalsIgnoreCase(callback)) {
                return true;
            }
        }
        return false;
    }

}