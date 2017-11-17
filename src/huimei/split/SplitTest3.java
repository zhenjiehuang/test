package huimei.split;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.hm.mayson.module.customer.service.impl.ParserTextServiceImpl;
import com.hm.mayson.module.customer.template.text.TextNode;
import com.hm.mayson.module.customer.template.text.TextSentence;
import com.hm.mayson.module.customer.template.text.TextTemplate;
import com.hm.mayson.module.progress.model.ProgressRecordInfo;
import com.poi.excel.parse.ExportExcel;

public class SplitTest3 {

    public static void main(String[] args) {
        try {
            ParserTextServiceImpl split = new ParserTextServiceImpl();
            TextTemplate template = getTemplate1();

            System.out.println(JSON.toJSONString(template));

            BufferedReader br = new BufferedReader(new FileReader(
                    new File("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\split\\xuanwu.txt")));
            String line = null;
            List<Excel> excels = new ArrayList<Excel>();
            long start = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                ProgressRecordInfo record = split.parserByTemplate(line, template);
                Excel excel = new Excel();
                excel.setText(line);
                excel.setA(record.getSymptom());
                excel.setB(record.getCurrentDiagnosis());
                excel.setC(record.getPhysicalExamination());
                excels.add(excel);
            }
            System.out.println(System.currentTimeMillis() - start);
            br.close();
            ExportExcel<Excel> export = new ExportExcel<>(excels, Excel.class);
            export.saveFile(new File("D://11-15-首次病程2.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TextTemplate getTemplate1() {
        TextTemplate template = new TextTemplate();

        List<TextNode> nodes = new ArrayList<TextNode>();
        TextNode symptom = new TextNode();
        symptom.setAttr("symptom");
        symptom.setName("主诉");
        List<TextSentence> sentences = new ArrayList<TextSentence>();
        TextSentence sentence = new TextSentence();
        sentence.setStart("病程记录");
        sentence.setEnds(Arrays.asList("病程记录", "病例特点"));
        sentences.add(sentence);

        sentence = new TextSentence();
        sentence.setStart("病例特点");
        sentence.setEnds(Arrays.asList("入院查体", "辅助检查"));
        sentences.add(sentence);
        symptom.setSentences(sentences);
        nodes.add(symptom);

        TextNode physicalExamination = new TextNode();
        physicalExamination.setAttr("physicalExamination");
        physicalExamination.setName("体格检查");
        sentences = new ArrayList<TextSentence>();
        sentence = new TextSentence();
        sentence.setStart("入院查体");
        sentence.setEnds(Arrays.asList("辅助检查"));
        sentences.add(sentence);

        physicalExamination.setSentences(sentences);
        nodes.add(physicalExamination);

        TextNode currentDiagnosis = new TextNode();
        currentDiagnosis.setAttr("currentDiagnosis");
        currentDiagnosis.setName("当前诊断");
        sentences = new ArrayList<TextSentence>();
        sentence = new TextSentence();
        sentence.setStart("定性诊断和鉴别诊断");
        sentence.setEnds(Arrays.asList("鉴别诊断", "2.", "2、", "（2）", "(2)", "2）", "2)"));
        sentences.add(sentence);

        currentDiagnosis.setSentences(sentences);
        nodes.add(currentDiagnosis);

        template.setNodes(nodes);

        return template;
    }

    private static TextTemplate getTemplate2() {
        TextTemplate template = new TextTemplate();

        List<TextNode> nodes = new ArrayList<TextNode>();
        TextNode symptom = new TextNode();
        symptom.setAttr("symptom");
        symptom.setName("主诉");
        List<TextSentence> sentences = new ArrayList<TextSentence>();
        TextSentence sentence = new TextSentence();
        sentence.setStart("病程记录");
        sentence.setEnds(Arrays.asList("病程记录", "病例特点"));
        sentences.add(sentence);

        sentence = new TextSentence();
        sentence.setStart("病例特点");
        sentence.setEnds(Arrays.asList("入院查体", "辅助检查"));
        sentences.add(sentence);
        symptom.setSentences(sentences);
        nodes.add(symptom);

        TextNode physicalExamination = new TextNode();
        physicalExamination.setAttr("physicalExamination");
        physicalExamination.setName("体格检查");
        sentences = new ArrayList<TextSentence>();
        sentence = new TextSentence();
        sentence.setStart("入院查体");
        sentence.setEnds(Arrays.asList("辅助检查"));
        sentences.add(sentence);

        physicalExamination.setSentences(sentences);
        nodes.add(physicalExamination);

        TextNode currentDiagnosis = new TextNode();
        currentDiagnosis.setAttr("currentDiagnosis");
        currentDiagnosis.setName("当前诊断");
        sentences = new ArrayList<TextSentence>();
        sentence = new TextSentence();
        sentence.setStart("诊断及诊断依据");
        sentence.setEnds(Arrays.asList("诊疗计划", "鉴别诊断", "定性诊断", "其他相关疾病诊断"));
        sentences.add(sentence);

        currentDiagnosis.setSentences(sentences);
        nodes.add(currentDiagnosis);

        template.setNodes(nodes);

        return template;
    }

}
