package huimei.xml;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

/**
 * 建德电子病历 xml文档解析器
 * Created by tangww on 2017-07-12.
 */
public class JianDeXmlTemplate {

    static Map<String, String> map = new HashMap<String, String>();

    public static void main(String[] args) {
        parserProgressXml("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\xml\\病程记录.xml");
    }

    /**
     * 解析 病程 类型的xml 文档
     * 
     * @param xmlStr
     * @return BodyText 的节点的内容
     *
     */
    public static void parserProgressXml(String xmlStr) {
        try {
            // 使用 SAXReader 读 xml ,兼容性较好，对格式和编码 较宽松 DocumentHelper.parseText
            // 对格式要求严格，容易报错
            SAXReader xmlReader = new SAXReader();
            Document document = xmlReader.read(new File(xmlStr));
            // Document document = DocumentHelper.parseText(xmlStr);; //
            // 将字符串转为XML
            DefaultElement XTextDocument = (DefaultElement) document.getRootElement();
            getValue(XTextDocument);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getValue(DefaultElement element) {
        // root比较特殊，要单独循环
        List<?> contents = element.content();
        for (int i = 0; i < contents.size(); i++) {
            Object content = contents.get(i);
            if (content instanceof DefaultElement) {
                getValue((DefaultElement) content, i - 2 + "");
            }
        }
    }

    private static void getValue(DefaultElement element, String path) {
        String text = element.getText();
        if (text.startsWith("hm.")) {
            map.put(text, path);
            System.out.println(text + ":" + path);
        }
        
        int index = 0;
        for (Object content : element.content()) {
            if (content instanceof DefaultElement) {
                getValue((DefaultElement) content, path + ", " + index++);
            }
        }
    }

}
