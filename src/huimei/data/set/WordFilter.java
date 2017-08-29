package huimei.data.set;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import com.poi.excel.parse.ExportDynamicExcel;

public class WordFilter {

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

    private static List<List<?>> getList(Collection<String> collection) {
        List<List<?>> list = new ArrayList<List<?>>();
        for (String data : collection) {
            list.add(Arrays.asList(data));
        }

        return list;
    }

    static {
        WordDictionary.getInstance().init(Paths.get("D:\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + WordFilter.class.getPackage().getName().replace('.', '\\');
        WordDictionary.getInstance().init(Paths.get(path));
        String fileName = "set.txt";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));
            JiebaSegmenter segmenter = new JiebaSegmenter();
            String line = null;

            Set<String> parents = new HashSet<String>();
            Set<String> nodes = new HashSet<String>();
            Set<String> ses = new HashSet<String>();

            while ((line = br.readLine()) != null) {
                List<SegToken> tokens = segmenter.process(line, JiebaSegmenter.SegMode.SEARCH);
                if (tokens.size() > 1) {
                    // 可以多次分词
                    ses.add(line);
                }
                if (hasNode(line)) {
                    if (hasParent(line)) {

                    } else {
                        // 没有词性
                        parents.add(line);
                    }
                } else {
                    // 没有节点
                    nodes.add(line);
                }
            }

            if (!ses.isEmpty()) {
                ExportDynamicExcel sesExcel = new ExportDynamicExcel(getList(ses));
                sesExcel.saveFile(new File(resultPath, "ses.xls"));
            }
            if (!nodes.isEmpty()) {
                ExportDynamicExcel nodesExcel = new ExportDynamicExcel(getList(nodes));
                nodesExcel.saveFile(new File(resultPath, "nodes.xls"));
            }
            if (!parents.isEmpty()) {
                ExportDynamicExcel parentsExcel = new ExportDynamicExcel(getList(parents));
                parentsExcel.saveFile(new File(resultPath, "parents.xls"));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
