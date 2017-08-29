package huimei.data.time;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.hm.apollo.service.PushService;
import com.poi.excel.parse.ImportExcel;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月25日
 * author：huangzhenjie
 * @version 1.0
 */
public class Test {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + ParseTimeServiceImpl.class.getPackage().getName().replace('.', '\\');
        String fileName = "1.xls";

        PushService service = new PushService();

        try {
            RecordInfo info = new RecordInfo();
            info.setAge(55D);
            info.setAgeType("岁");
            info.setBodyTempr(44D);
            info.setHighBldPress(190D);
            info.setLowBldPress(120D);
            ImportExcel<ExcelTime> excel = new ImportExcel<>(new File(path, fileName), ExcelTime.class);
            for (ExcelTime time : excel.getRowDatas()) {
                info.setSymptom(time.getText());
                String result = service.push("http://127.0.0.1:8080/apollo/", "intelligent_recognize", info);
                JSONArray sentences = JSONObject.parseObject(result).getJSONObject("body")
                        .getJSONArray("sentences");
                List<JSONObject> list = new ArrayList<JSONObject>();
                List<String> c = new ArrayList<String>();
                for (int i = 0; i < sentences.size(); i++) {
                    JSONArray words = sentences.getJSONObject(i).getJSONArray("words");
                    for (int j = 0; j < words.size(); j++) {
                        if (words.getJSONObject(j).getJSONArray("types") != null
                                && words.getJSONObject(j).getJSONArray("types").contains(6)) {
                            list.add(words.getJSONObject(j));
                        }
                    }

                    JSONArray concepts = sentences.getJSONObject(i).getJSONArray("concepts");
                    if (concepts != null) {
                        for (int j = 0; j < concepts.size(); j++) {
                            if (concepts.getJSONObject(j).getString("conceptName") != null) {
                                c.add(concepts.getJSONObject(j).getString("conceptName"));
                            }
                        }
                    }

                }
                System.out.println(JSONObject.toJSONString(list));
                System.out.println(JSONObject.toJSONString(c));
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
