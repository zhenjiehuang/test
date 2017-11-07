package huimei.split;

import java.util.ArrayList;
import java.util.List;

import huimei.split.model.KeyProgress;

public class SpliteTest {

    static String[] keyWords = { "一、病例特点：", "二、诊断依据及鉴别诊断：" };
    static String progress = "病程记录   。2017-10-26 16:17 。    患者，刘殿梅，女性，84岁，主因“间断胸闷10年，再发伴咳嗽、喘息5天”于2017-10-26 13:37急诊以“心衰，肺部感染”收入我科。。"
            + "一、病例特点：一、病例特点：111。。      1、患者，老年，女性，慢性病程，急性加重。。      2、病史：10年前无明显诱因出现胸闷，伴心前区疼痛，就诊复兴门医院，明确诊断心肌梗死，予行冠脉支架术，植入3枚支架，此后症状缓解，但胸闷仍间断发作，逐渐出现双下肢水肿，喘息，长期口服波立维75mgqd、倍他乐克6.25mgqd、速尿20mgqd等治疗，2年前就诊体检时诊断房颤，2月前因消化道出血停用波立维，5天前受凉后出现咳嗽，咳少许白痰，无发热，伴胸闷症状较前加重，喘息，夜间不能平卧，无胸痛，无心悸，1天前因喘息加重，就诊我院急诊，查胸片示双肺感染伴胸腔积液，心影大，NTproBNP25229pg/ml，考虑“心衰，肺炎”，予异舒吉扩冠，丽泉利尿及一君抗感染等治疗，喘息有所改善，为进一步巩固治疗，收入留观。。      3、既往史：高血压病史40年，血压最高180/100mmHg，近期血压良好，未服用降压药物，老年痴呆、多动症病史7年，否认糖尿病史，否认食物及药物过敏史。。      4、入院查体：神清，精神弱，痴呆状态，交流障碍，喘息貌，半卧位休息，心电监护示：HR82次/分，BP137/80mmHg，R21次/分，SPO2 98%，口唇无紫绀，双肺呼吸音粗，双肺可及少许湿罗音，心律绝对不齐，S1强弱不等，各瓣膜听诊区未闻及病理学杂音，全腹软，无压痛、反跳痛，无肌紧张，肝脾肋下未触及，墨菲氏征阴性，肠鸣音3次/分，双下肢无水肿。。      5、辅助检查: 心梗三项+BNP（宣武医院：2017-10-25）：cTNI0.174ng/ml，CKMB6.02ng/ml，Myo 39.9ng/ml，NTproBNP：25229pg/ml；。心电图（宣武医院：2017-10-26）：房颤律，HR82次/分。。血常规（宣武医院：2017-10-25）：WBC 8.54*10^9/L,NE 83.6%,HGB 100g/L,PLT 157*10^ 9/L； 。凝血（宣武医院：2017-10-25）：D二聚体：8.46ug/ml；。 生化（宣武医院：2017-10-25）:ALT 43IU/L AST 64IU/L CREA 106umol/L Urea 11.17mmol/L K 3.7mmol/L Na 148mmol/L；。胸片（宣武医院：2017-10-25）：双肺感染伴胸腔积液，心影大，动脉硬化改变。。二、诊断依据及鉴别诊断：。。      1、诊断及诊断依据：。1）冠心病，急性非ST段抬高型心肌梗死 陈旧性心肌梗死，永久房颤，慢性心衰急性加重 冠脉支架术后 心功能KillipII级：患者胸闷急性加重，心肌酶升高，无相应导联ST抬高表现，听诊双肺可及少许湿罗音，ECG示房颤，结合既往陈旧心梗，冠脉支架病史，慢性喘息，下肢水肿，NTproBNP升高，故当前诊断成立。。2）双肺炎，双侧胸腔积液：患者老年女性，此次受凉收出现咳嗽，少量咳痰，查体双肺呼吸音粗，可及少许湿罗音，血象NE%升高，胸片提示双肺感染伴胸腔积液，诊断明确。。3）高血压3级 极高危：患者既往多次非同日于安静状态下测量血压值均高于140/80mmHg，最高180/100mmHg，结合患者高龄，心梗病史，诊断明确。。4）老年痴呆，多动症：患者7年前逐渐出现认知障碍，多次走失，明确诊断老年痴呆多动症，入院后不能正确回答问题，肢体乱动，不配合治疗，故当前诊断成立。。      2、鉴别诊断：。      1）主动脉夹层：患者胸闷喘息，既往高血压，需鉴别，但患者无胸痛，双上肢血压均正常，且经抗心衰治疗后，症状可有所缓解，故可鉴别除外，必要时可行增强CT进一步明确。。      2）肺动脉栓塞：患者胸闷伴喘息，且D二聚体升高，需鉴别，但患者无咯血，无胸痛，ECG无典型SⅠQⅢTⅢ表现，血气无顽固低氧血症表现，经抗心衰治疗后症状可改善，可鉴别，必要时可行肺栓塞CT以鉴别。。      3）支气管哮喘：患者喘息，需鉴别，但患者老年起病，既往无过敏病史及家族史，发作时经抗心衰治疗可缓解，可鉴别，必要时可行支气管舒张试验以鉴别。。三、诊疗计划：。。       1、予患者1级护理、低盐低脂饮食、低流量吸氧，告病重，心电监护等；。       2、完善血尿便常规、生化、凝血等，动态观察心电图，心肌酶，BNP变化；。       3、予异舒吉扩冠，丽泉利尿减轻心脏负荷，倍他乐克控制心室率等冠心病二级预防治疗；。       4、予一君抗感染，沐舒坦祛痰促进痰液引流，待各项结果回报，调整治疗。。                                    医生签名：            。";

    private static KeyProgress segmentIndex(String progress, String keyWord, int index) {
        KeyProgress key = new KeyProgress();
        key.setKeyWord(keyWord);
        int keyIndex = -1;
        int loop = 0;
        int wordIndex = -1;
        while ((keyIndex = progress.indexOf(keyWord, keyIndex)) != -1) {
            wordIndex = keyIndex;
            keyIndex += keyWord.length();
            if (loop++ == index) {
                break;
            }
        }
        key.setIndex(wordIndex);

        while (loop++ != index && (keyIndex = progress.indexOf(keyWord, keyIndex)) != -1) {
            key.setIndex(keyIndex);
            keyIndex += keyWord.length();
        }


        return key;
    }

    private static void segmentProgress(List<KeyProgress> progresses) {
        KeyProgress pre = progresses.get(0);
        KeyProgress current = null;
        for (int i = 1; i < progresses.size(); i++) {
            current = progresses.get(i);
            pre.setProgress(progress.substring(pre.getIndex(), current.getIndex()));
            pre = current;
        }

        if (current == null) {
            current = pre;
        }

        current.setProgress(progress.substring(current.getIndex()));
    }

    public static void main(String[] args) {
        try {
            List<KeyProgress> progresses = new ArrayList<KeyProgress>();
            for (String keyWord : keyWords) {
                progresses.add(segmentIndex(progress, keyWord, 2));
            }

            segmentProgress(progresses);

            for (KeyProgress keyProgress : progresses) {
                System.out.println(keyProgress.getProgress());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
