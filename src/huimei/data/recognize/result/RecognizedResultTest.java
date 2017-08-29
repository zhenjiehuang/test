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

    static String data = "入院时间:  科别:  出院时间:  主诊医生：季红慧入院诊断: Impression          出院诊断: Discharge Diagnosis     入院原因 Reason to the Hospital患者蔡彩云，女，66岁，退休。因“胸痛1月”入院。入院情况T 37.4℃ P 80次/分 R 20次/分 BP 94/64mmHg 神清，精神可，唇不绀，颈静脉无怒张，两肺呼吸音清，未闻及干湿罗音，心界不大，心率90次/分，律绝对不齐，第一心音强弱不等，各瓣膜未闻及明显病理性杂音，腹平软，全腹无压痛，肝脾肋下未及，双下肢无浮肿。四肢肌力5级，肌张力正常。住院诊治经过(包括重要发现和结论，接受的手术和操作，药物和其他治疗)Hospital Course(Contains Any Significant Findings,Procedure Performed,Medication and Other Treatments) 入院后完善相关辅助检查：2016-10-28  血常规+CRP 平均血小板体积 16.0 fl; 凝血功能常规+D-D 凝血酶时间 22.8 s;大生化血管紧张素转化酶 6.9 ;载脂蛋白B 0.46 g/L;总胆固醇 2.62 mmol/L;甘油三酯 0.49 mmol/L;葡萄糖(GLU) 7.73 mmol/L;肌酐 38.5 μmol/L;碱性磷酸酶 154 IU/L;谷草谷丙比值 0.7 ;前白蛋白 162 mg/L; BNP+cTnI B型纳尿肽 383.00 pg/ml;血清肌钙蛋白 0.012 ng/ml;2016-10-29  急诊cTnI+急诊生化全套+急诊心肌酶谱血清肌钙蛋白 0.082 ng/ml;肌酐 42.2 umol/L;钾 2.9 mmol/L;谷草/谷丙 0.7 /;白球蛋白比例 1.07 ;葡萄糖 7.1 mmol/L;2016-10-30 急诊cTnI+急诊心肌酶谱肌酸激酶同功酶 11 IU/L;肌酸激酶 43 U/L;乳酸脱氢酶 123 IU/L;谷草转氨酶 17 IU/L; 急诊cTnI+急诊心肌酶谱血清肌钙蛋白 0.155 ng/ml;常规十二导心电图检测报告(2016-10-28) 检查号:  检查所见:  检查结果:窦性心律下壁异Q波，请结合临床。ST-T改变; 治疗上予：强化抗血小板、调脂稳定斑块治疗、扩张冠脉，控制心室率，改善心室重塑、控制血压、改善心肌代谢（拜阿司匹林、氯吡格雷、阿托伐他汀、倍他乐克片,单硝酸异山梨酯、贝那普利片、曲美他嗪片）。于2016.10.29日行冠脉造影,术中见:右冠优势形，右冠状动脉：右冠开口30%狭窄，右冠近段原支架内未见明显狭窄，右冠中段及远段弥漫性病变约40%狭窄。左冠状动脉：左主干内膜光滑，无明显狭窄，前降支近中段80%狭窄，累及对角支近段50%狭窄。回旋支中段90%狭窄”,行左冠FFR检查及回旋支PCI术，在回旋支植入支架一枚。出院计划 Discharge Plan 出院时情况患者诉无明显胸闷，无咳嗽咳痰，无黑朦晕厥，无夜间阵发性呼吸困难等不适，食欲可，夜间平稳入睡，二便无殊。出院状态 Patient's Condition on Discharge改善出院去向 Disposition回家交通工具：□120急救车■私家车□公交车□步行□其他：出院带药 Discharge Medications阿司匹林肠溶片[拜阿司匹灵] 100mg/1粒*1粒 100mg  1次/日☆硫酸氢氯吡格雷片[波立维] 75mg/1粒*1粒 75mg    1次/日阿托伐他汀钙片[立普妥] 20mg/1粒*1粒 20mg        1次/日⊙△盐酸贝那普利片[洛汀新] 10mg/1粒*1粒 10mg     1次/日※△☆阿卡波糖片[拜唐苹] 0.1g/1片*1片 0.1g     1次/日△☆⊙酒石酸美托洛尔片[倍他乐克] 25mg/1粒*1粒 6.25mg  1次/日☆盐酸曲美他嗪片[万爽力] 20mg/1粒*1粒 20mg    1次/日⊙冠心舒通胶囊 0.3克/1粒*1粒 0.9克            1次/日☆复方丹参滴丸 27毫克/1粒*180粒 270毫克     1次/日出院指导 Followup Instructions 生活自理:■调整自我照料方法□完全能自理□部分自理□不能自理活动：■在能耐受范围适当活动□限制活动（正常活动的恢复须根据医生建议）药物：□无特殊指导■食物/药物间相互作用指导□具体用药指导见说明书食物/药物间相互作用指导：▲β受体阻滞剂（达利全、倍他乐克）：应用β受体阻滞剂时应每日自测心率，保持静息心率&gt;50bpm，否则请到心内科门诊就诊；不宜自行调整剂量，切忌突然停药，▲抗血小板药物（阿司匹林、波立维）：服药期间切忌外伤，请按时复查大便OB、血常规，如有全身出血点、瘀斑出现，请即时来诊。健康指导护理指导：疾病护理指导，日常自我照顾护理指导，服药/处置注意事项，其他，无康复指导: ■无□康复器材□轮椅□拐杖□助行器□便盆椅□其他：饮食指导：□无禁忌■特殊饮食□流质饮食□管饲□其它特殊饮食指导：□无■低盐饮食■低脂饮食□糖尿病饮食□低蛋白饮食□低嘌呤饮食□忌碘饮食医疗技术教育：■无□导尿管照顾□胰岛素注射□引流管/袋□吸氧装置□鼻胃管□其他：是否有植入物：有冠脉内植入支架，嘱定期门诊随访治疗，长期服用药物治疗，定期复查肝肾功能，波立维至少服用12个月以上，1年后复查冠脉造影，有胸闷胸痛症状可及时回医院就诊，必要时复查冠脉造影。抗血小板药物（阿司匹林、波立维）治疗期间尽量避免外科手术。疼痛管理的宣教：■不需要□需要：告知患者如何进行疼痛评分，疼痛≥4分时及时就医。其它指导：（可能需要的紧急医疗）：如果出现胸痛气急情况，请及时来院或就近医院治疗出院后持续照护建议：随访（复诊安排）时间地点复诊目的科室医师 2周本院门诊继续治疗，检查康复情况心血管内科季红慧出院方式:医嘱出院知情告知信息：患者经主管医生评估其病情后，可以安排出院，为提供患者持续性的医疗服务，制定本出院计划，以提供患者出院持续医护指导及协助，已向患者或家属说明本计划的内容，并获得患者及家属的了解及同意。医师签名:江喜平责任护士:欧阳琴签名时间:2016-10-28 10:36  患者或授权人:  日期：";

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
                    if (result1.getData3().equals(excel.getData3()) && result1.getData4() != null
                            && result1.getData4().equals(excel.getData4())) {
                        result1.setRecognized(true);
                        correct.add(result1.getData1());
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
