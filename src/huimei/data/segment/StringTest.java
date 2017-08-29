package huimei.data.segment;

import java.nio.file.Paths;
import java.util.List;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class StringTest {

    static {
        WordDictionary.getInstance()
                .init(Paths.get("Z:\\workspace\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }

    public static void main(String[] args) {
        try {
            String line = "动脉压痛";
            JiebaSegmenter segmenter = new JiebaSegmenter();
            List<SegToken> tokens = segmenter.process(line, JiebaSegmenter.SegMode.SEARCH);
            System.out.println(tokens);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
