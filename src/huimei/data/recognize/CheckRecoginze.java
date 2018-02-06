package huimei.data.recognize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.parse.ExportExcel;

import huimei.data.recognize.result.RecognizedResult;

public class CheckRecoginze {

    public static void main(String[] args) {
        String resultPath = "D://1//1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + CheckRecoginze.class.getPackage().getName().replace('.', '\\');
        // String fileName = "建德线上辅助检查.txt";
        String fileName = "建德胸痛病例.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));
            String line = null;
            int count = 0;
            while ((line = br.readLine()) != null && count < 5) {
                RecordInfo info = new RecordInfo();
                info.setSymptom(line);

                PushService pushService = new PushService();

                String str = pushService.push("http://localhost:8080/apollo", "/intelligent_recognize", info);

                JSONObject result = JSONObject.parseObject(str);

                JSONArray sentences = result.getJSONObject("body").getJSONArray("sentences");

                List<RecognizedResult> datas = new ArrayList<>();

                for (int i = 0; i < sentences.size(); i++) {
                    JSONObject sentence = sentences.getJSONObject(i);
                    JSONArray words = sentence.getJSONArray("words");
                    for (int j = 0; j < words.size(); j++) {
                        RecognizedResult data = new RecognizedResult();
                        data.setData1(words.getJSONObject(j).getString("word"));
                        data.setData2(words.getJSONObject(j).getString("types"));
                        // data.getData2()
                        if (data.getData2() != null && !data.getData2().equals("null")) {
                            if (CollectionUtils.containsAny(JSON.parseArray(data.getData2()),
                                    Arrays.asList(-1, 4, 16))) {
                                data.setData3("pass");
                            }
                        }
                        datas.add(data);
                    }

                }

                ExportExcel<RecognizedResult> export = new ExportExcel<>(datas, RecognizedResult.class);
                export.saveFile(new File(resultPath, "item" + count++ + ".xls"));
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
