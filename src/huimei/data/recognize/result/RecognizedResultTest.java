package huimei.data.recognize.result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.pojo.RecordInfo;
import com.poi.excel.parse.ImportExcel;

public class RecognizedResultTest {

    static String data = "首次病程记录患者姓名: 性别: 年龄:  病历号:   入院日期: 床号:  临床科室:      患者，，，。因“突发左肢无力6天余”入院。病例特点:1、老年男性；否认既往有高血压，糖尿病病史。我院住院期间诊断为“脑梗死 2型糖尿病高血压病脂肪肝胆囊多发结石前列腺增生”2、急性起病；患者6天余前站立时突发右肢无力，下肢为重，口角歪斜，言语不清，无头痛呕吐，无肢体抽搐，急诊求诊，查头颅CT提示考虑“脑梗死”，予活血化瘀、护脑促醒等治疗后，急诊拟“脑梗死”收入我科住院治疗，今因医保原因办理周转，继续入院治疗。3、入科时查体：T 36.7℃，P67 次/分，R 18次/分，BP167/91mmHg，神志清，双侧瞳孔等大等圆，直径2.5mm，光反射存在，双眼球活动自如，双侧额纹对称，伸舌右偏，双肺呼吸音粗，未闻及干湿性罗音，心律齐，杂音未闻及，腹软，无压痛，双下肢未见明显水肿，右肢肌力5级，左肢肌力4级，肌张力正常，双侧肢体深浅感觉对称，左肢腱反射（++），右侧（++），病理征未引出，左右侧阴性，颈软，克氏征阴性。4、辅助检查：颅脑CT平扫报告 2016-12-25 15:00:00 检查号：CTY75987 检查结果:右侧枕顶叶皮层下及左侧半卵圆中心多发软化灶，双侧脑室旁脑白质缺血性改变，老年性脑改变。拟诊讨论初步诊断:   诊断依据: 1、老年男性，我院住院期间诊断为“脑梗死 2型糖尿病高血压病脂肪肝胆囊多发结石前列腺增生”。2、急性起病；局灶性神经功能缺损症状。3、意识清楚，左肢偏瘫。4、头颅CT未见明显责任病灶。鉴别诊断: 1.脑出血：急性起病，既往有高血压病史，局灶性神经功能受损症状体征，但是患者无头痛、恶心呕吐等颅高压症状，发病后头颅CT未见到高密度病灶。2.脑肿瘤：患者有颅内局灶性神经功能受损表现，但是脑肿瘤一般为慢性起病，进行性加重神经功能受损和颅高压症状，头颅CT可见到占位性病变，必要时可行头颅MRI平扫+增强扫描相鉴别。诊疗计划:1.神经内科2级护理，监测血压，留陪1人。2.对症支持治疗：控制血糖，调脂（瑞舒伐他汀胶囊）、改善脑代谢（丁苯肽软胶囊）、改善循环（长春西汀针）、抗血小板聚集（阿司匹林肠溶片）等。3.完善相关检查，如完善头颅MR等，根据病情调整用药。4.健康宣教：脑梗塞病情可能加重，若发生内脏或颅内出血时，则有可能需转ICU进一步治疗；饮食指导：低盐低脂糖尿病饮食；病情稳定后康复指导适度功能锻炼，按时服药，监测血压。5.可测量目标：1周左右控制病情进展。记录医生: 毛意记录日期及时间:";

    static String[] occur = { "阴性", "阳性" };

    public static void main(String[] args) {
        String resultPath = "D://1";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + RecognizedResultTest.class.getPackage().getName().replace('.', '\\');
        String fileName = "识别结果.xlsx";
        try {
            ImportExcel<RecognizedResult> im = new ImportExcel<RecognizedResult>(new File(path, fileName),
                    RecognizedResult.class);

            List<RecognizedResult> results = im.getRowDatas();

            PushService service = new PushService();

            RecordInfo info = new RecordInfo();

            info.setSymptom(data);

            String str = service.push("http://localhost:8080/apollo", "/intelligent_recognize", info);

            JSONObject result = JSONObject.parseObject(str);

            JSONArray sentences = result.getJSONObject("body").getJSONArray("sentences");

            List<String> correct = new ArrayList<String>();

            for (int i = 0; i < sentences.size(); i++) {
                JSONObject sentence = sentences.getJSONObject(i);
                JSONArray concepts = sentence.getJSONArray("concepts");
                if (concepts == null) {
                    continue;
                }
                for (int j = 0; j < concepts.size(); j++) {
                    JSONObject concept = concepts.getJSONObject(j);
                    JSONObject properties = concept.getJSONObject("properties");

                    RecognizedResult excel = new RecognizedResult();
                    excel.setData1(concept.getString("conceptName"));
                    excel.setData3(occur[concept.getInteger("occur")]);

                    int index = results.indexOf(excel);
                    if (index == -1) {
                        if (properties.containsKey("时间")) {
                            excel.setData4(properties.getJSONArray("时间").getString(0));
                        }

                        if (properties.containsKey("部位")) {
                            excel.setData1(properties.getJSONArray("部位").getString(0) + excel.getData1());
                        }
                        if (properties.containsKey("性状")) {
                            excel.setData1(properties.getJSONArray("性状").getString(0) + excel.getData1());
                        }
                        index = results.indexOf(excel);
                        if (index == -1) {
                            System.out.println(excel.getData1());
                            continue;
                        }
                    }
                    RecognizedResult result1 = results.get(index);
                    if (result1.getData4() != null
                            && result1.getData4().equals(excel.getData4())) {
                        if ("临床表现".equals(result1.getData2())) {
                            if (excel.getData3().equals(result1.getData3())) {
                                result1.setRecognized(true);
                                correct.add(result1.getData1());
                            } else {
                                System.out.println("反     " + result1.getData1());
                            }
                        } else {
                            result1.setRecognized(true);
                            correct.add(result1.getData1());
                        }
                    } else {
                        // System.out.println(result1.getData1());
                    }
                }
            }
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("1111");

            correct.stream().forEach(str1 -> System.out.println(str1));

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            int count = 0;
            for (RecognizedResult result1 : results) {
                if (result1.isRecognized()) {
                    count++;
                } else {
                    System.out.println(result1.getData1());
                }
            }

            System.out.println(count);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
