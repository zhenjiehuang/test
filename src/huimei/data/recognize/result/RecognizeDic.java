package huimei.data.recognize.result;

import java.io.File;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.mayson.pojo.MaysonRecognizeRequest;
import com.poi.excel.parse.ImportExcel;

public class RecognizeDic {

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + RecognizeDic.class.getPackage().getName().replace('.', '\\');
        String fileName = "宣武识别字典.xls";
        try {
            ImportExcel<RecognizedResult> dics = new ImportExcel<>(new File(path, fileName),
                    RecognizedResult.class);

            PushService pushService = new PushService();
            MaysonRecognizeRequest req = new MaysonRecognizeRequest();
            for (RecognizedResult dic : dics.getRowDatas()) {
                req.setTextMessage(dic.getData1());
                String str = pushService.push("http://localhost:8080/apollo", "/v_3_0/recognize", req);

                JSONObject result = JSONObject.parseObject(str);

                JSONArray sentences = result.getJSONObject("body").getJSONArray("recognizeResultList");
                int i = 0;
                for (; i < sentences.size(); i++) {
                    if (sentences.getJSONObject(i).getString("word").equals(dic.getData1())) {
                        break;
                    }
                }

                if (sentences.size() == i) {
                    System.out.println(dic.getData1());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
