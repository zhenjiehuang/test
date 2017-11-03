package huimei.xml;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import com.alibaba.fastjson.JSONObject;
import com.hm.mayson.module.progress.model.ProgressRecordInfo;

public class XmlTemplateParser<T> {

    public static void main(String[] args) throws Exception {
        XmlTemplateParser<ProgressRecordInfo> parser = new XmlTemplateParser<ProgressRecordInfo>(
                "Z:\\workspace\\huimei\\work\\test\\src\\huimei\\xml\\template.xml",
                "C:\\Users\\Administrator\\Desktop\\实际患者信息模板\\入院记录_format.xml", ProgressRecordInfo.class);

        ProgressRecordInfo result = parser.parser();
        System.out.println(JSONObject.toJSONString(result));
    }

    private Document template;

    private Document xml;

    private Class<T> clz;

    public XmlTemplateParser(String templateXmlFile, String xmlFile, Class<T> clz) throws DocumentException {
        SAXReader xmlReader = new SAXReader();
        template = xmlReader.read(new File(templateXmlFile));
        xml = xmlReader.read(new File(xmlFile));
        this.clz = clz;
    }

    private T parser() {
        try {
            T t = clz.newInstance();
            DefaultElement root = (DefaultElement) template.selectSingleNode("//template");
            for (Object obj : root.content()) {
                if (obj instanceof DefaultElement) {
                    DefaultElement content = (DefaultElement) obj;
                    String fieldName = content.getName();
                    Object value = getValue(content);
                    Method method = clz.getDeclaredMethod(
                            "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                            value.getClass());
                    method.invoke(t, value);
                }
            }

            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Object getValue(DefaultElement content) {
        Element pathnode = content.element("pathnode");
        if (pathnode != null) {
            return getNodepathValue(pathnode);
        }

        Element paths = content.element("paths");
        if (paths != null) {
            return getPathValue(paths);
        }

        return null;
    }

    private String getNodepathValue(Element pathnode) {
        return xml.valueOf(pathnode.getText()).trim();
    }

    private String getPathValue(Element paths) {
        StringBuffer value = new StringBuffer();
        List<?> sentences = paths.selectNodes("sentence|punctuation");
        if (!sentences.isEmpty()) {
            for (Object object : sentences) {
                DefaultElement sentence = (DefaultElement) object;
                List<?> nodes = sentence.content();
                for (Object node : nodes) {
                    if (node instanceof DefaultElement) {
                        DefaultElement element = (DefaultElement) node;
                        if (element.getName().equals("staticnode")) {
                            value.append(element.getText());
                        } else if (element.getName().equals("pathnode")) {
                            value.append(xml.valueOf(element.getText()).trim());
                        }
                    }
                }
                if (nodes.size() > 0) {
                    value.append(sentence.getName().equals("sentence") ? "。" : ",");
                }
            }
        }

        return value.toString();
    }

}
