package huimei.xml;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;

/**
 * 建德电子病历 xml文档解析器
 * Created by tangww on 2017-07-12.
 */
public class JianDeXmlParser {

    public static void main(String[] args) {
        parserProgressXml("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\xml\\病程记录.xml");
        parserProgressXml("C:/Users/Administrator/Desktop/病程/胸痛病历/10110599/病程记录.xml");
    }

    private void init() {
        Element root = new DefaultElement("XTextDocument");
        Element XElements = new DefaultElement("XElements");
        root.add(XElements);
        XElements.add(new DefaultElement("Element"));

        Element XTextBody = new DefaultElement("Element");
        XElements.add(XTextBody);
        

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
            if (XTextDocument != null) {
                DefaultElement info = getElement(XTextDocument, 3, 1, 5);

                DefaultElement basic = getElement((DefaultElement) info.content().get(0), 3, 0, 4, 1, 3);
                System.out.print("姓名:");
                getName(getElement((DefaultElement) basic.content().get(0), 3, 1, 13));// 姓名
                System.out.print("性别:");
                getName(getElement((DefaultElement) basic.content().get(1), 3, 1, 13));// 性别
                System.out.print("年龄:");
                getName(getElement((DefaultElement) basic.content().get(2), 3, 1, 13));// 年龄
                System.out.print("病例号:");
                getName(getElement((DefaultElement) basic.content().get(3), 3, 1, 13));// 病例号

                basic = getElement((DefaultElement) info.content().get(0), 3, 0, 4, 2, 3);
                System.out.print("入院日期Format:");
                getName(getElement((DefaultElement) basic.content().get(0), 3, 1, 11, 1));// 入院日期Format
                System.out.print("入院日期:");
                getName(getElement((DefaultElement) basic.content().get(0), 3, 1, 12));// 入院日期
                System.out.print("床号:");
                getName(getElement((DefaultElement) basic.content().get(2), 3, 1, 12));// 床号
                System.out.print("科室:");
                getName(getElement((DefaultElement) basic.content().get(3), 3, 1, 12));// 科室

                System.out.print("主诉:");
                DefaultElement element = getElement((DefaultElement) info.content().get(0), 3, 2);
                getName(getElement(element, 11));// 主诉

                int index = 5;
                System.out.print("病例特点:");
                element = getElement((DefaultElement) info.content().get(0), 3, index);
                if (element.content().size() != 2) {
                    getContentValue(getElement(element, 7).content());// 病例特点
                } else {
                    index -= 2;
                    System.out.println();
                }

                index += 4;
                System.out.print("拟诊讨论:");
                element = getElement((DefaultElement) info.content().get(0), 3, index);
                if (element.content().size() != 2) {
                    getContentValue(getElement(element, 7).content());// 拟诊讨论
                } else {
                    index -= 2;
                    System.out.println();

                }

                index += 4;
                System.out.print("初步诊断:");
                element = getElement((DefaultElement) info.content().get(0), 3, index);
                if (element.content().size() != 2) {
                    getContentValue(getElement(element, 7).content());// 初步诊断
                } else {
                    index -= 2;
                    System.out.println();
                }

                index += 4;
                System.out.print("诊断依据:");
                element = getElement((DefaultElement) info.content().get(0), 3, index);
                if (element.content().size() != 2) {
                    getContentValue(getElement(element, 7).content());// 诊断依据
                } else {
                    index -= 2;
                    System.out.println();
                }

                index += 4;
                System.out.print("鉴别诊断:");
                element = getElement((DefaultElement) info.content().get(0), 3, index);
                if (element.content().size() != 2) {
                    getContentValue(getElement(element, 7).content());// 鉴别诊断
                } else {
                    index -= 2;
                    System.out.println();
                }

                index += 4;
                System.out.print("诊疗计划:");
                element = getElement((DefaultElement) info.content().get(0), 3, index);
                if (element.content().size() != 2) {
                    getContentValue(getElement(element, 7).content());// 诊疗计划
                } else {
                    index -= 2;
                    System.out.println();
                }

                index += 2;
                element = getElement((DefaultElement) info.content().get(0), 3, index);
                System.out.print("记录医生:");
                getName(getElement(element, 4, 0, 3, 0, 3, 1, 13));
                System.out.print("记录日期及时间Format:");
                getName(getElement(element, 4, 0, 3, 1, 3, 1, 11, 0));
                System.out.print("记录日期及时间:");
                getName(getElement(element, 4, 0, 3, 1, 3, 1, 12));

                // getName(getElement((DefaultElement)
                // info.content().get(1)));// 病程
            }
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
