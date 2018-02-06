package huimei.data.recognize;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.base.ExcelColumn;
import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

public class CallbackRecognize {

    public static class Excel {
        @ExcelColumn(columnName = "句子", columnNum = 0)
        private String data;
        @ExcelColumn(columnName = "109召回", columnNum = 1)
        private String callback1;
        @ExcelColumn(columnName = "95召回", columnNum = 2)
        private String callback2;
        @ExcelColumn(columnName = "109核心", columnNum = 3)
        private String core1;
        @ExcelColumn(columnName = "95核心", columnNum = 4)
        private String core2;
        @ExcelColumn(columnName = "是否包含召回", columnNum = 5)
        private String containCallback;
        @ExcelColumn(columnName = "是否包含核心", columnNum = 6)
        private String containCore;
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
        public String getCore1() {
            return core1;
        }
        public void setCore1(String core1) {
            this.core1 = core1;
        }
        public String getCore2() {
            return core2;
        }
        public void setCore2(String core2) {
            this.core2 = core2;
        }
        public String getContainCallback() {
            return containCallback;
        }
        public void setContainCallback(String containCallback) {
            this.containCallback = containCallback;
        }
        public String getContainCore() {
            return containCore;
        }
        public void setContainCore(String containCore) {
            this.containCore = containCore;
        }
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + CallbackRecognize.class.getPackage().getName().replace('.', '\\');
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
                                    "/v_2_2/callbackWords", info);
                            JSONObject result1 = JSONObject.parseObject(str1);
                            JSONArray callback1 = result1.getJSONObject("body")
                                    .getJSONArray("callbackWords");

                            String str2 = pushService.push("http://127.0.0.1:8099/apollo",
                                    "/v_2_2/callbackWords", info);
                            JSONObject result2 = JSONObject.parseObject(str2);
                            JSONArray callback2 = result2.getJSONObject("body").getJSONArray("callbackWords");

                            Excel e = new Excel();
                            e.setData(d);
                            if (callback1 != null) {
                                e.setCallback1(callback1.toJSONString());
                            }
                            if (callback2 != null) {
                                e.setCallback2(callback2.toJSONString());
                            }
                            boolean contain = contain(callback1, callback2);
                            e.setContainCallback(contain + "");
                            results.add(e);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            System.out.println(i);

            ExportExcel<Excel> export = new ExportExcel<Excel>(results, Excel.class);

            export.saveFile(new File(resultPath, "classification_result_callbak_4.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean contain(Collection<?> col1, Collection<?> col2) {
        if (CollectionUtils.isEmpty(col1)) {
            return false;
        }

        if (CollectionUtils.isEmpty(col2)) {
            return true;
        }

        return col1.containsAll(col2) && col1.size() == col2.size();
    }
}