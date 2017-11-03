package huimei.xml;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

/**
 * 建德电子病历 xml文档解析器
 * Created by tangww on 2017-07-12.
 */
public class JianDeXmlParser5 {

    public static void main(String[] args) {
        // parserProgressXml("Z:\\hm\\config\\mayson\\xml\\ruyuan.xml");
        parserProgressXml("C:/Users/Administrator/Desktop/实际患者信息模板/首次查房_format.xml");
        // parserProgressXml("C:/Users/Administrator/Desktop/实际患者信息模板/入院记录_format.xml");
        // parserProgressXml("C:/Users/Administrator/Desktop/实际患者信息模板/首次病程_format.xml");
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
                // info.content().get(1)));// 病程
            // System.out.println(XTextDocument.valueOf("//Template//NewCtrl[@ElementID=56]//Content_Text"));

            List<?> nodes = XTextDocument
                    .selectNodes("//Template//DocObjContent//NewCtrl[@ElementID=204277][0]/Content_Text");
            for (Object node : nodes) {
                System.out.println(((Node) node).getText());
            }
            // Node node = XTextDocument
            // .selectSingleNode("//Template//DocObjContent//Section[@ControlName='既往史']//Content_Text");
            // System.out.println(node.getText());
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private static void getName(Object element) {
        getElementValue((DefaultElement) element);
    }

    private static DefaultElement getElement(DefaultElement element, int... index) {
        return getNode(element, 0, index);
    }

    private static DefaultElement getNode(DefaultElement element, int i, int... index) {
        if (index.length == i) {
            return element;
        }
        return getNode((DefaultElement) element.content().get(index[i]), i + 1, index);
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
