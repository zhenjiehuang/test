package huimei.data.set;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class SegmentFilter {

    static {
        WordDictionary.getInstance().init(Paths.get("D:\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }


    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + SegmentFilter.class.getPackage().getName().replace('.', '\\');
        WordDictionary.getInstance().init(Paths.get(path));
        String fileName = "set.txt";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));

            JiebaSegmenter segmenter = new JiebaSegmenter();
            String line = null;

            Set<String> lines = new HashSet<String>();
            while ((line = br.readLine()) != null) {
                List<SegToken> tokens = segmenter.process(line, JiebaSegmenter.SegMode.SEARCH);
                if (tokens.size() > 1) {
                    lines.add(line);
                }
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
