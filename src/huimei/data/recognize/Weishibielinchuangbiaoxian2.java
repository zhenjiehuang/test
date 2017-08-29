package huimei.data.recognize;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.mysql.jdbc.StringUtils;
import com.poi.excel.parse.ImportExcel;

public class Weishibielinchuangbiaoxian2 {

    static PushService service = new PushService();

    private static boolean hasNode(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getNodeSynonym/getWords", info);

        return JSONObject.parseObject(result).getJSONObject("body") != null;
    }

    private static boolean hasParent(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getTopLevelNodes/getWords", info);

        return JSONObject.parseObject(result).get("body") != null;
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Weishibielinchuangbiaoxian2.class.getPackage().getName().replace('.', '\\');
        String fileName = "未识别临床表现.xls";
        try {
            ImportExcel<Linchuangbiaoxian> im = new ImportExcel<Linchuangbiaoxian>(new File(path, fileName),
                    Linchuangbiaoxian.class);

            List<Linchuangbiaoxian> datas = im.getRowDatas();

            for (int i = 0; i < datas.size(); i++) {
                Linchuangbiaoxian data = datas.get(i);
                if (StringUtils.isEmptyOrWhitespaceOnly(data.getData1())) {
                    continue;
                }
                // System.out.println(data.getData1());
                Linchuangbiaoxian data1 = datas.get(i + 1);
                if (data.getData1().toLowerCase().contains(data1.getData1())) {
                    // System.out.println(datas.get(i + 1).getDataLength());
                    i++;
                }
                Linchuangbiaoxian data2 = null;
                if (i + 1 < datas.size()) {
                    if ((data2 = datas.get(i + 1)).contain()) {
                        i++;
                    }
                }

                if (data1.getDataLength() == 1) {
                    // 词性没有
                    if (hasParent(data.getData1())) {
                        // 有词性
                        // System.out.println(data.getData1());
                    } else {
                        if (hasNode(data.getData1())) {
                            // 有节点，没词性
                            // System.out.println(data.getData1());
                        } else {
                            // 没有节点
                            // System.out.println(data.getData1());
                        }
                    }
                } else if (data1.getDataLength() == 2) {
                    if (data2 != null) {
                        if (data2.getData1().equals("")) {
                            // 词性未识别
                            // System.out.println(data.getData1());
                            // System.out.println(data1.getData1());
                            if (data2.getData2().equals("")) {
                                // 都是空可能是分词错误
                                if (hasParent(data1.getData1())) {
                                    // 整体有词性，但是分词分开了
                                    // System.out.println(data1.getData1() + "/"
                                    // + data.getData1());
                                } else {
                                    if (hasNode(data1.getData1())) {
                                        // 有节点
                                        // System.out.println(data1.getData1() +
                                        // "," + data1.getData2() + ","
                                        // + hasParent(data1.getData2()) + ","
                                        // + hasNode(data1.getData2()) + "," +
                                        // data.getData1());
                                    } else {
                                        // 新节点
                                        // System.out.println(data1.getData1() +
                                        // "," + data1.getData2() + ","
                                        // + hasParent(data1.getData2()) + ","
                                        // + hasNode(data1.getData2()) + "," +
                                        // data.getData1());
                                        // System.out.println(data1.getData1());
                                    }
                                }
                                // System.out.println(data1.getData2());
                            } else {
                                // 可能是分词错误
                                // System.out.println(data.getData1());
                            }
                        } else {
                            if (data2.getData1().equals("")) {
                                // 可能是分词错误
                                // System.out.println(data.getData1());
                            } else {
                                // 词性都有，可能分词错误
                                // System.out.println(data.getData1());
                            }
                        }
                    } else {
                        // 无意义
                        // System.out.println(data.getData1());
                    }
                } else {
                    // 3个以上的就是分词错误
                    String last = data1.getDataIndex(data1.getDataLength());
                    try {
                        data.getData1().substring(0,
                                data.getData1().toLowerCase().indexOf(last));
                    } catch (Exception e) {
                        continue;
                    }
                    String content = data.getData1().substring(0,
                            data.getData1().toLowerCase().indexOf(last));

                    if (hasParent(content)) {
                        // 分词错误
                        // System.out.println(content + "," + last + "," +
                        // hasParent(last) + "," + hasNode(last)
                        // + "," + data1.getDataLength());
                    } else {
                        if (hasNode(content)) {
                            // 分词错误+无词性
                            // System.out.println(content + "," + last + "," +
                            // hasParent(last) + ","
                            // + hasNode(last) + "," + data1.getDataLength());
                        } else {
                            // 分词错误+无节点
                            System.out.println(data.getData1() + "," + content + "," + last + ","
                                    + hasParent(last) + "," + hasNode(last) + "," + data1.getDataLength());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
