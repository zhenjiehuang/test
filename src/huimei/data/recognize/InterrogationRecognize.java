package huimei.data.recognize;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.base.ExcelColumn;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

public class InterrogationRecognize {

    public static class Excel {
        @ExcelColumn(columnName = "句子", columnNum = 0)
        private String data;
        @ExcelColumn(columnName = "109召回", columnNum = 1)
        private String callback1;
        @ExcelColumn(columnName = "95召回", columnNum = 2)
        private String callback2;
        @ExcelColumn(columnName = "是否包含召回", columnNum = 5)
        private String containCallback;
        public String getData() {
            return data;
        }
        public void setData(String data) {
            this.data = data;
        }
        public String getCallback1() {
            return callback1;
        }
        public void setCallback1(String callback1) {
            this.callback1 = callback1;
        }
        public String getCallback2() {
            return callback2;
        }
        public void setCallback2(String callback2) {
            this.callback2 = callback2;
        }
        public String getContainCallback() {
            return containCallback;
        }
        public void setContainCallback(String containCallback) {
            this.containCallback = containCallback;
        }
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + InterrogationRecognize.class.getPackage().getName().replace('.', '\\');
        String fileName = "collection.xls";
        try {
            ImportExcel<Excel> in = new ImportExcel<Excel>(new File(path, fileName), Excel.class);
            // ImportExcel<Excel> in = new ImportExcel<>(new File(resultPath,
            // "\\检验.xls"), Excel.class);
            List<Excel> results = new ArrayList<Excel>();
            int i=0;
            for (Excel data : in.getRowDatas()) {
                if (i == 1000) {
                    break;
                }
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
                            String str1 = pushService.push("http://127.0.0.1:8080/apollo",
                                    "/v_2_2/diagnose_through_interrogation", info);
                            JSONObject result1 = JSONObject.parseObject(str1);
                            List<String> callback1 = result1.getJSONObject("body")
                                    .getJSONArray("suspectedDiseases").stream().map(obj -> {
                                        return ((JSONObject) obj).getString("diseaseName");
                                    }).collect(Collectors.toList());

                            String str2 = pushService.push("http://127.0.0.1:8099/apollo",
                                    "/v_2_2/diagnose_through_interrogation", info);
                            JSONObject result2 = JSONObject.parseObject(str2);
                            List<String> callback2 = result2.getJSONObject("body")
                                    .getJSONArray("suspectedDiseases").stream().map(obj -> {
                                        return ((JSONObject) obj).getString("diseaseName");
                                    }).collect(Collectors.toList());

                            Excel e = new Excel();
                            e.setData(d);
                            if (callback1 != null) {
                                e.setCallback1(JSON.toJSONString(callback1));
                            }
                            if (callback2 != null) {
                                e.setCallback2(JSON.toJSONString(callback2));
                            }
                            e.setContainCallback(contain(callback1, callback2) + "");
                            results.add(e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            System.out.println(i);

            ExportExcel<Excel> export = new ExportExcel<Excel>(results, Excel.class);

            export.saveFile(new File(resultPath, "classification_result_interrogation_1.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String contain(Collection<?> col1, Collection<?> col2) {
        if (CollectionUtils.isEmpty(col1)) {
            return "不包含";
        }

        if (CollectionUtils.isEmpty(col2)) {
            return "包含";
        }

        List<Object> re = new ArrayList<Object>();
        for (Object o : col2) {
            if (!col1.contains(o)) {
                re.add(o);
            }
        }
        return JSON.toJSONString(re);
    }
}