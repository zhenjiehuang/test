package huimei.data.jjrq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.cdss.model.request.PatientProfileRequest;
import com.poi.excel.parse.ImportExcel;

public class Test {

    private static String getString(String str) {
        if (str != null && str.length() > 0) {
            return str.replaceAll(",", "\",\"");
        }
        return "";
    }

    private static void physicalSign(BufferedWriter bw, JSONObject data) throws IOException {
        bw.newLine();
        bw.write("physicalSign,");
        if (data == null) {
            bw.newLine();
            return;
        }
        bw.write(data.toJSONString());
        bw.newLine();
    }

    private static void assistExams(BufferedWriter bw, JSONArray data) throws IOException {
        bw.newLine();
        bw.write("assistExams,");
        if (data == null) {
            bw.newLine();
            return;
        }

        bw.write("examName,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("examName")).replaceAll(",", "\",\""));
            bw.write(",");
        }
        bw.newLine();
        bw.write(",itemName,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("itemName")).replaceAll(",", "\",\""));
            bw.write(",");
        }
        bw.newLine();
        bw.write(",numberValue,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("numberValue")));
            bw.write(",");
        }
        bw.newLine();
        bw.write(",value,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("value")));
            bw.write(",");
        }
        bw.newLine();
        bw.newLine();
    }

    private static void diseases(BufferedWriter bw, JSONArray data) throws IOException {
        bw.newLine();
        bw.write("diseaseHistorys,");
        if (data == null) {
            bw.newLine();
            return;
        }
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getString(j).replaceAll(",", "\",\"")));
            bw.write(",");
        }
        bw.newLine();
        bw.newLine();
    }

    private static void diseaseHistorys(BufferedWriter bw, JSONArray data) throws IOException {
        bw.newLine();
        bw.write("diseaseHistorys,");
        if (data == null) {
            bw.newLine();
            return;
        }
        bw.write(",disease,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("disease")).replaceAll(",", "\",\""));
            bw.write(",");
        }
        bw.newLine();
        bw.write(",occur,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("occur")));
            bw.write(",");
        }
        bw.newLine();
        bw.newLine();
    }

    private static void clinicalFindings(BufferedWriter bw, JSONArray data) throws IOException {
        bw.newLine();
        bw.write("clinicalFindings,");
        if (data == null) {
            bw.newLine();
            return;
        }
        bw.write("symptom,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("symptom")).replaceAll(",", "\",\""));
            bw.write(",");
        }
        bw.newLine();
        bw.write(",occur,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("occur")));
            bw.write(",");
        }
        bw.newLine();
        bw.write(",attribute,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("attribute")));
            bw.write(",");
        }
        bw.newLine();
        bw.write(",part,");
        for (int j = 0; j < data.size(); j++) {
            bw.write(getString(data.getJSONObject(j).getString("part")));
            bw.write(",");
        }
        bw.newLine();
        bw.newLine();
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Test.class.getPackage().getName().replace('.', '\\');
        String fileName = "jjrq.xls";
        try {
            ImportExcel<JjrqExcel> infos = new ImportExcel<JjrqExcel>(new File(path, fileName),
                    JjrqExcel.class);
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(resultPath, "2.csv")), charset));
            BufferedWriter em = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(resultPath, "3.csv")), charset));

            PatientProfileRequest info = new PatientProfileRequest();
            info.setAge(55D);
            info.setAgeType("岁");
            info.setBodyTempr(44D);
            info.setHighBldPress(190D);
            info.setLowBldPress(120D);

            int count=0;
            for (JjrqExcel row : infos.getRowDatas()) {
                // if (count++ >= 1)
                // break;
                if (!StringUtils.isEmpty(row.getText())) {
                    String line = row.getText();
                    info.setSymptom(line);

                    PushService pushService = new PushService();

                    String str = pushService.push("http://localhost:8080/apollo", "/record_recognition",
                            info);
                    JSONObject result = JSONObject.parseObject(str);

                    JSONObject body = result.getJSONObject("body");
                    if (body.getJSONArray("clinicalFindings") == null
                            && body.getJSONArray("physicalSign") == null
                            && body.getJSONArray("diseaseHistorys") == null
                            && body.getJSONArray("assistExams") == null
                            && body.getJSONArray("diseases") == null) {
                        em.write(line);
                        em.write(",");
                        result.remove("basicInfo");
                        em.write("\"" + result.toJSONString().replaceAll("\"", "\"\"") + "\"");
                        em.newLine();
                        em.flush();
                    } else {
                        bw.write(line);
                        bw.write(",");
                        result.remove("basicInfo");
                        bw.write("\"" + result.toJSONString().replaceAll("\"", "\"\"") + "\"");
                        bw.newLine();
                        clinicalFindings(bw, body.getJSONArray("clinicalFindings"));
                        physicalSign(bw, body.getJSONObject("physicalSign"));
                        diseaseHistorys(bw, body.getJSONArray("diseaseHistorys"));
                        assistExams(bw, body.getJSONArray("assistExams"));
                        diseases(bw, body.getJSONArray("diseases"));
                        bw.newLine();
                        bw.flush();
                    }

                }
            }
            bw.close();
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
