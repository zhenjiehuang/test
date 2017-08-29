package huimei.data.magic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.parse.ImportExcel;

public class Test {

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test.class.getPackage().getName().replace('.', '\\');
        String fileName = "药品禁忌症识别0608.xlsx";
        try {
            ImportExcel<MagicExcel> infos = new ImportExcel<MagicExcel>(new File(path, fileName),
                    MagicExcel.class);
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(resultPath, "1.csv")), charset));

            for (MagicExcel row : infos.getRowDatas()) {
                if (!StringUtils.isEmpty(row.getText())) {
                    String line = row.getText();
                    RecordInfo info = new RecordInfo();
                    info.setAge(55D);
                    info.setAgeType("岁");
                    info.setBodyTempr(44D);
                    info.setHighBldPress(190D);
                    info.setLowBldPress(120D);
                    info.setSymptom(line);

                    PushService pushService = new PushService();

                    String str = pushService.push("http://localhost:8080/apollo", "/intelligent_recognize",
                            info);

                    JSONObject result = JSONObject.parseObject(str);

                    JSONArray sentences = result.getJSONObject("body").getJSONArray("sentences");

                    for (int i = 0; i < sentences.size(); i++) {
                        JSONObject sentence = sentences.getJSONObject(i);
                        bw.write(sentence.getString("sentence"));
                        bw.newLine();
                        JSONArray words = sentence.getJSONArray("words");
                        for (int j = 0; j < words.size(); j++) {
                            bw.write(words.getJSONObject(j).getString("word").replaceAll(",", "\",\""));
                            bw.write(",");
                        }
                        bw.newLine();
                        for (int j = 0; j < words.size(); j++) {
                            bw.write((words.getJSONObject(j).getString("types") + "").replaceAll(",",
                                    "\",\""));
                            bw.write(",");
                        }
                        bw.newLine();
                        bw.newLine();
                    }
                    bw.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
