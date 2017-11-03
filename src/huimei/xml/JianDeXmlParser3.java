package huimei.xml;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

import com.alibaba.fastjson.JSONArray;

/**
 * 建德电子病历 xml文档解析器
 * Created by tangww on 2017-07-12.
 */
public class JianDeXmlParser3 {

    public static void main(String[] args) {
        // parserProgressXml("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\xml\\病程记录.xml");
        parserProgressXml("C:/Users/Administrator/Desktop/病程/胸痛病历/10110599/病程记录.xml");
        // parserProgressXml2("C:/Users/Administrator/Desktop/病程/胸痛病历/10112193/病程记录.xml");
        // parserProgressXml("C:/Users/Administrator/Desktop/病程/胸痛病历/10110599/病程记录1.xml");
    }

    private static void parserProgressXml2(String xmlStr) {
        JianDeXmlTemplate.main(null);
        Map<String, String> map = JianDeXmlTemplate.map;

        try {
            SAXReader xmlReader = new SAXReader();
            Document document = xmlReader.read(new File(xmlStr));
            DefaultElement XTextDocument = (DefaultElement) document.getRootElement();

            for (Entry<String, String> entry : map.entrySet()) {
                List<Object> list = JSONArray.parseArray("[" + entry.getValue() + "]");
                int[] array = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = (int) list.get(i);
                }
                System.out.print(entry.getKey() + ":");
                getName(getElement(XTextDocument, array));
                System.out.println();
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析 病程 类型的xml 文档
     * @param xmlStr
     * @return  BodyText 的节点的内容
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

            // getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 23, 11));

            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 1, 3, 0, 3, 1, 13));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 1, 3, 1, 3, 1, 13));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 1, 3, 2, 3, 1, 13));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 1, 3, 3, 3, 1, 13));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 2, 3, 0, 3, 1, 11, 1));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 2, 3, 0, 3, 1, 12));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 2, 3, 2, 3, 1, 12));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 0, 4, 2, 3, 3, 3, 1, 12));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 12, 7, 0, 8, 0, 1));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 16, 11));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 24, 11));

            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 26, 4, 0, 3, 0, 3, 1, 13));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 26, 4, 0, 3, 1, 3, 1, 11, 0));
            getName(getElement(XTextDocument, 3, 1, 5, 0, 3, 26, 4, 0, 3, 1, 3, 1, 12));

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static void getElementValue(DefaultElement element) {
        System.out.println(element.getStringValue());
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
