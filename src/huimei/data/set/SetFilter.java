package huimei.data.set;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.controller.PushService;
import com.hm.apollo.module.recognition.model.NodeSynonym;
import com.huaban.analysis.jieba.WordDictionary;

public class SetFilter {

    static PushService service = new PushService();

    private static boolean hasNode(String word) {
        NodeSynonym info = new NodeSynonym();
        info.setSynonymWord(word);

        String result = service.push("http://127.0.0.1:8080/apollo", "/getNodeSynonym/getWords", info);

        return JSONObject.parseObject(result).getJSONObject("body") != null;
    }

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + SetFilter.class.getPackage().getName().replace('.', '\\');
        WordDictionary.getInstance().init(Paths.get(path));
        String fileName = "set.txt";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));

            String line = null;

            Set<String> lines = new HashSet<String>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            for (String s : lines) {
                System.out.println(s);
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
