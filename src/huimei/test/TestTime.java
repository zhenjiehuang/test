package huimei.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.framework.utils.ParseTimeUtils;
import com.hm.apollo.module.cdss.enums.WordTypeEnum;
import com.hm.apollo.module.recognition.pojo.Sentence;
import com.hm.apollo.module.recognition.pojo.Word;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月10日
 * author：huangzhenjie
 * @version 1.0
 */
public class TestTime {

    private void recognizeTime(Sentence sentence) {
        List<String> times = ParseTimeUtils.parseTime(sentence.getSentence());
        if (CollectionUtils.isEmpty(times)) {
            return;
        }
        sentence.setWords(getWords(sentence.getSentence(), sentence.getWords(), times, WordTypeEnum.时间词));
    }

    private List<Word> getWords(String sentence, List<Word> source, List<String> targets, WordTypeEnum type) {
        List<Word> words = new ArrayList<Word>();
        Iterator<Word> it = source.iterator();

        int end = 0;
        for (String target : targets) {
            int start = sentence.indexOf(target, end);
            end = target.length() + start;
            StringBuffer str = new StringBuffer();
            while (it.hasNext()) {
                Word word = it.next();
                if (word.getStart() >= start) {
                    // 大于开始字符的表示包含在target内
                    str.append(word.getWord());
                } else {
                    // 否则不是target内字符
                    words.add(word);
                }
                if (word.getEnd() >= end) {
                    // 到结束字符跳出当前循环开始匹配下一个target
                    words.add(getWord(str.toString(), start, end, type));
                    break;
                }
            }
        }

        // 分词可能会大于targets的长度，多余的保留
        while (it.hasNext()) {
            words.add(it.next());
        }

        return words;
    }

    private Word getWord(String word, int start, int end, WordTypeEnum type) {
        Word result = new Word();
        result.setWord(word);
        result.setStart(start);
        result.setEnd(end);
        result.setTypes(Arrays.asList(type.getType()));
        return result;
    }

    public static void main(String[] args) throws ParseException {
        Sentence sentence = new Sentence();
        sentence.setSentence("发热3天，咳嗽3天");
        List<Word> words = new ArrayList<Word>();
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> tokens = segmenter.process(sentence.getSentence(), JiebaSegmenter.SegMode.SEARCH);
        for (SegToken token : tokens) {
            Word word = new Word();
            word.setEnd(token.endOffset);
            word.setStart(token.startOffset);
            word.setWord(token.word);
            words.add(word);
        }

        sentence.setWords(words);

        new TestTime().recognizeTime(sentence);

        System.out.println(JSONObject.toJSONString(sentence));
    }
}
