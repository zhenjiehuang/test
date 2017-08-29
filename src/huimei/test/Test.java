package huimei.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.module.recognition.RecordContentTypeEnum;
import com.hm.apollo.module.recognition.pojo.Sentence;
import com.hm.apollo.module.recognition.pojo.Word;
import com.hm.apollo.module.recognition.service.impl.IntelligentRecognitionDataServiceImpl;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;

public class Test {

    /**
     * 特殊标点编码
     *
     * @param source
     * @return
     */
    private static String encodePunctuation(String source) {
        return source.replace("(", "enleftb").replace(")", "enrightb").replace("（", "leftb").replace("）",
                "rightb");
    }

    /**
     * 特殊标点解码
     *
     * @param str
     * @return
     */
    private static String decodePunctuation(String str) {
        // 括号，%，℃ 这些加入词典不管用所以这样处理，以后需要改进
        return str.replace("enleftb", "(").replace("enrightb", ")").replace("leftb", "（")
                .replace("rightb", "）").replace("%。", "%").replace("%,", "%").replace("%，", "%");
    }

    private static List<Sentence> punctuation(String content, RecordContentTypeEnum type) {
        List<Sentence> list = new ArrayList<Sentence>();
        if (StringUtils.isEmpty(content)) {
            return list;
        }
        content += "。";
        char[] cs = content.toCharArray();
        int index = 0;
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            if (IntelligentRecognitionDataServiceImpl.puncTokenSet.contains(String.valueOf(c))
                    && index != i) {
                String ss = new String(content.substring(index, i));
                Sentence sentence = new Sentence();
                sentence.setRecordType(type.getType());
                sentence.setSentence(ss);
                System.out.println(ss);
                list.add(sentence);
                index = i + 1;
            }
        }

        return list;
    }

    static {
        WordDictionary.getInstance().init(Paths.get("D:\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }

    public static void main(String[] args) throws IOException {
        // List<Sentence> sentences =
        // punctuation("3天前头痛。2天之前感冒。发烧。血压大于150mh。体温>40℃",
        // RecordContentTypeEnum.主诉);
        List<Sentence> sentences = punctuation("感冒（7天），我同工酶(CK-MB)你,体温超过正常50%,血压超过100%",
                RecordContentTypeEnum.主诉);
        JiebaSegmenter segmenter = new JiebaSegmenter();
        int index = 0;
        for (Sentence sentence : sentences) {
            List<Word> words = new ArrayList<Word>();
            List<SegToken> tokens = segmenter.process(sentence.getSentence(), JiebaSegmenter.SegMode.SEARCH);
            // List<SegToken> tokens =
            // segmenter.process(encodePunctuation(sentence.getSentence()),
            // JiebaSegmenter.SegMode.SEARCH);
            for (SegToken token : tokens) {
                Word word = new Word();
                // System.out.println(token.word);
                // token.word = decodePunctuation(token.word);
                word.setEnd(token.endOffset);
                word.setStart(token.startOffset);
                word.setWord(token.word);
                System.out.println(token);
                words.add(word);
            }
            sentence.setWords(words);
            // wordRecognitionService.recognizeWords(sentence, null);
            System.out.println(JSONObject.toJSONString(sentence));
        }
    }
}
