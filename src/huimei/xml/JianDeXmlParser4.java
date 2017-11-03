package huimei.xml;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import com.alibaba.druid.util.StringUtils;

/**
 * 建德电子病历 xml文档解析器
 * Created by tangww on 2017-07-12.
 */
public class JianDeXmlParser4 {

    public static void main(String[] args) {
        JianDeTemplate template = template("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\xml\\JianDe.xml");
        parse("C:/Users/Administrator/Desktop/病程/胸痛病历/10110599/病程记录.xml", template);
    }

    private static JianDeTemplate template(String file) {
        try {
            SAXReader xmlReader = new SAXReader();
            Document document = xmlReader.read(new File(file));
            DefaultElement root = (DefaultElement) document.getRootElement();

            JianDeTemplate template = new JianDeTemplate();

            template.setPatientName(getElementArrayValue(root.element("patientName")));
            template.setPatientAge(getElementArrayValue(root.element("patientAge")));
            template.setPatientGender(getElementArrayValue(root.element("patientGender")));
            template.setPatientGuid(getElementArrayValue(root.element("patientGuid")));
            template.setBingchuang(getElementArrayValue(root.element("bingchuang")));
            template.setKeshi(getElementArrayValue(root.element("keshi")));
            template.setInhospital(getElementArrayValue(root.element("inhospital")));
            template.setInhospitalFormat(getElementArrayValue(root.element("inhospitalFormat")));

            Element recordElement = root.element("record");

            TemplateRecord record = new TemplateRecord();
            record.setBackstep(getElementIntValue(recordElement.element("backstep")));
            record.setBingli(getElementArrayValue(recordElement.element("bingli")));
            record.setChubu(getElementArrayValue(recordElement.element("chubu")));
            record.setDoctor(getElementArrayValue(recordElement.element("doctor")));
            record.setJianbie(getElementArrayValue(recordElement.element("jianbie")));
            record.setNizhen(getElementArrayValue(recordElement.element("nizhen")));
            record.setPath(getElementArrayValue(recordElement.element("path")));
            record.setRecordTimeFormat(getElementArrayValue(recordElement.element("recordTimeFormat")));
            record.setRecordTime(getElementArrayValue(recordElement.element("recordTime")));
            record.setStart(getElementIntValue(recordElement.element("start")));
            record.setStep(getElementIntValue(recordElement.element("step")));
            record.setSubjective(getElementArrayValue(recordElement.element("subjective")));
            record.setZhenduan(getElementArrayValue(recordElement.element("zhenduan")));
            record.setZhenliao(getElementArrayValue(recordElement.element("zhenliao")));

            template.setRecord(record);

            return template;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void parse(String file, JianDeTemplate template) {
        try {
            SAXReader xmlReader = new SAXReader();
            Document document = xmlReader.read(new File(file));
            DefaultElement root = (DefaultElement) document.getRootElement();

            reflectValue1(root, template);

            template.getPatientName();
            template.getPatientAge();
            template.getPatientGender();
            template.getPatientGuid();
            template.getBingchuang();
            template.getKeshi();
            template.getInhospital();
            template.getInhospitalFormat();
            TemplateRecord record = template.getRecord();

            DefaultElement parent = getElement(root, record.getPath());
            if (parent == null) {
                return;
            }

            int start = record.getStart();
            start = reflectValue2(parent, record.getSubjective(), start, record.getStep(),
                    record.getBackstep());
            start = reflectValue2(parent, record.getBingli(), start, record.getStep(), record.getBackstep());
            start = reflectValue2(parent, record.getNizhen(), start, record.getStep(), record.getBackstep());
            start = reflectValue2(parent, record.getChubu(), start, record.getStep(), record.getBackstep());
            start = reflectValue2(parent, record.getZhenduan(), start, record.getStep(),
                    record.getBackstep());
            start = reflectValue2(parent, record.getJianbie(), start, record.getStep(), record.getBackstep());
            start = reflectValue2(parent, record.getZhenliao(), start, record.getStep(),
                    record.getBackstep());
            start = reflectValue2(parent, record.getDoctor(), start, record.getStep(), record.getBackstep());

            record.getRecordTime();
            record.getRecordTimeFormat();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reflectValue1(DefaultElement root, JianDeTemplate template) {
        try {
            Class<?> clz = JianDeTemplate.class;
            for (Field field : clz.getDeclaredFields()) {
                if (field.getType().equals(TemplateRecord.class)) {

                } else {
                    int[] index = (int[]) field.get(template);
                    DefaultElement element = getElement(root, index);
                    if (element != null) {
                        getElementValue(element);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int reflectValue2(DefaultElement parent, int[] index, int start, int step, int backStep) {
        DefaultElement element = getElement(parent, index);
        if (element == null) {
            start -= backStep;
        } else {
            getElementValue(element);
        }
        return start + step;
    }



    private static void getName(Object element) {
        if (element == null) {
            return;
        }
        getElementValue((DefaultElement) element);
    }

    private static DefaultElement getElement(DefaultElement element, int... index) {
        return getNode(element, 0, index);
    }

    private static DefaultElement getNode(DefaultElement element, int i, int... index) {
        if (index.length == i) {
            return element;
        }

        List<?> contents = element.content();
        if (contents.size() <= index[i]) {
            return null;
        }
        return getNode((DefaultElement) contents.get(index[i]), i + 1, index);
    }

    private static Integer getElementIntValue(Element element) {
        String str = element.getStringValue();
        return StringUtils.isEmpty(str) ? 0 : Integer.valueOf(str);
    }

    private static String getElementValue(Element element) {
        System.out.println(element.getStringValue());
        return element.getStringValue();
    }

    private static int[] getElementArrayValue(Element element) {
        String str = element.getStringValue();

        if (StringUtils.isEmpty(str)) {
            return new int[0];
        }

        String[] ss = str.split(", *");
        int[] value = new int[ss.length];
        for (int i = 0; i < ss.length; i++) {
            value[i] = Integer.valueOf(ss[i]);
        }
        return value;
    }

    private static void getContentValue(List<?> content) {
        StringBuffer str = new StringBuffer();
        for (Object obj : content) {
            Node node = (Node) obj;
            str.append(node.getStringValue());
        }

        System.out.println(str);
    }

}
