package huimei.data.time;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.module.recognition.enums.TypeEnum;
import com.hm.apollo.module.recognition.pojo.Sentence;
import com.hm.apollo.module.recognition.pojo.Word;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.huaban.analysis.jieba.WordDictionary;
import com.mysql.jdbc.StringUtils;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月25日
 * author：huangzhenjie
 * @version 1.0
 */
public class TestTime {

    int[] DATE_FIELDS = { Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY,
            Calendar.MINUTE, Calendar.SECOND };

    static WordDictionary wordDictionary = new WordDictionary(
            Paths.get("Z:\\workspace\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    static JiebaSegmenter segmenter = new JiebaSegmenter(wordDictionary);

    private void segment(Sentence sentence) {
        List<SegToken> tokens = segmenter.process(sentence.getSentence(), JiebaSegmenter.SegMode.SEARCH);
        sentence.setWords(new ArrayList<>());
        tokens.forEach(token -> {
            Word word = new Word();
            word.setEnd(token.endOffset);
            word.setStart(token.startOffset);
            word.setWord(token.word);
            sentence.getWords().add(word);
        });
    }

    public void test(String str) {
        try {
            Pattern p = Pattern.compile("(" //
                    // + "(\\d{4}|\\d{2}|\\d{1}"// 年月日形式
                    + "(\\d+|[一二三四五六七八九十半两])?"// 中文数字
                    + ")(" // 括号分隔
                    + "(多|余|\\+|个)?"// xx余[年月日]
                    + "(/|\\.|-|:|：|时|点|分钟|分|秒|天|日|号|周期|周|年|月|小时|星期|礼拜) ?"// 时间分隔
                    + "|(\\d{4}|\\d{2}|\\d{1}) ?"// 防止2017-07-08最后一个数字没有识别
                    + ")"// 括号分隔
                    + "(余前|半|余|\\+|前|以前|之前)?");

            Matcher m = p.matcher(str);

            Sentence sentence = new Sentence();

            sentence.setSentence(str);

            segment(sentence);
            List<List<Word>> timeWords = new ArrayList<List<Word>>();

            if (m.find()) {
                List<Word> words = new ArrayList<Word>();
                timeWords.add(words);
                int lastIndex = 0;
                do {
                    Word word = getTimeWord(m);
                    if (word != null) {
                        // 连续的时间词放在一起
                        if (lastIndex == word.getStart()) {
                            // 当前词是上一个词的结束，说明是连续的词
                            lastIndex = word.getEnd();
                            words.add(word);
                        } else {
                            lastIndex = word.getEnd();
                            words = new ArrayList<Word>();
                            timeWords.add(words);
                            words.add(word);
                        }
                    }
                } while (m.find());
            }

            // 获得最终时间词
            List<Word> words = new ArrayList<Word>();
            int index = 0;
            for (List<Word> ws : timeWords) {
                if (ws.size() > 0 && !StringUtils.isNullOrEmpty(ws.get(0).getUnit())) {
                    Word word = getDateWord(ws);
                    if (word != null) {
                        addTimeWords(index, sentence, word, words);
                    } else {
                        for (Word w : ws) {
                            word = getDateWord(Arrays.asList(w));
                            if (word != null) {
                                addTimeWords(index, sentence, word, words);
                            }
                        }
                    }
                }
            }

            Iterator<Word> itSource = sentence.getWords().iterator();
            List<Word> newWords = new ArrayList<Word>();
            // 时间词替换分词
            Word lastWord = null;

            List<Integer> timeTypes = intelligentRecognitionDataService().convertTypes(TypeEnum.时间);
            for (Word time : words) {
                while (itSource.hasNext()) {
                    Word word = itSource.next();
                    // 还未开始匹配规则
                    if (word.getStart() < time.getStart()) {
                        if (lastWord != null && containsType(timeTypes, lastWord.getTypes())
                                && containsType(timeTypes, word.getTypes())) {
                            // 连续的时间词
                            Word temp = unitWords(Arrays.asList(lastWord, word), timeTypes);
                            lastWord.setWord(temp.getWord());
                            lastWord.setStart(temp.getStart());
                            lastWord.setEnd(temp.getEnd());
                            lastWord.setUnit(word.getUnit());
                        } else {
                            newWords.add(lastWord = word);
                        }
                    }
                    // 匹配规则结束，但是还在匹配范围内，不加入新list
                    if (word.getEnd() >= time.getEnd()) {
                        break;
                    }
                }
                if (lastWord != null && containsType(timeTypes, lastWord.getTypes())
                        && containsType(timeTypes, time.getTypes())) {
                    // 连续的时间词
                    Word temp = unitWords(Arrays.asList(lastWord, time), timeTypes);
                    lastWord.setWord(temp.getWord());
                    lastWord.setStart(temp.getStart());
                    lastWord.setEnd(temp.getEnd());
                    lastWord.setUnit(time.getUnit());
                } else {
                    newWords.add(lastWord = time);
                }
            }

            while (itSource.hasNext()) {
                newWords.add(itSource.next());
            }

            // 去掉词2边空格
            int startIndex = 0;
            for (Word word : newWords) {
                word.setWord(word.getTypes() == null && word.getWord().length() > 1 ? word.getWord().trim()
                        : word.getWord());
                if (word.getWord().length() == 0) {
                    continue;
                }
                word.setStart(startIndex);
                word.setEnd(startIndex = (startIndex + word.getWord().length()));
            }

            sentence.setWords(newWords);

            System.out.println(JSONObject.toJSONString(newWords));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param index
     * @param sentence
     * @param word
     * @param words
     */
    private void addTimeWords(int index, Sentence sentence, Word word, List<Word> words) {
        boolean start = false;
        for (; index < sentence.getWords().size(); index++) {
            Word sen = sentence.getWords().get(index);
            if (sen.getStart() == word.getStart()) {
                start = true;
            }
            // 可能有识别错误的，按分词为准
            if (start) {
                if (sen.getEnd() > word.getEnd()) {
                    // 当遇到没有词性的词时，把词拆开，按识别为准
                    if (CollectionUtils.isEmpty(sen.getTypes())) {
                        sentence.getWords().add(index,
                                getWord(sen.getWord().substring(0, sen.getEnd() - word.getEnd()),
                                        sen.getEnd(), sen.getEnd()));
                        sen.setStart(sen.getEnd() - word.getEnd() + sen.getStart());
                        sen.setWord(sen.getWord().substring(sen.getEnd() - word.getEnd()));
                        words.add(word);
                    }
                    break;
                }
                if (sen.getEnd() == word.getEnd()) {
                    words.add(word);
                    break;
                }
            }
        }
    }

    private A intelligentRecognitionDataService() {
        return new A();
    }

    private class A {
        private List<Integer> convertTypes(TypeEnum type) {
            return Arrays.asList(type.getNewType());
        }
    }

    private Word getDateWord(List<Word> source) {
        List<Integer> timeTypes = intelligentRecognitionDataService().convertTypes(TypeEnum.时间);
        Word first = source.get(0);
        if (source.size() == 1) {
            // 只有一个时
            if (first.getUnit().matches("[/|\\.|-|:|+| ]")) {
                // 无意义
                return null;
            } else {
                if ("分".equals(first.getUnit().trim())) {
                    return null;
                }
                first.setTypes(timeTypes);
                first.setPeriodTime(true);
                return first;
            }
        } else if (!StringUtils.isNullOrEmpty(first.getUnit())
                && first.getUnit().matches("[/|：|:|\\.|年|月|-]")) {
            Calendar c = Calendar.getInstance();
            int fieldIndex = 0;// DATE_FIELDS对应的下标
            if ("年".equals(first.getUnit())) {
                // 确定是年开始
                fieldIndex = 0;
            } else if ("月".equals(first.getUnit())) {
                // 确定是月开始
                fieldIndex = 1;
            } else {
                // 剩下2017-07-01,2017/07/01,2017.07.01这种形式
                if (source.size() >= 2) {
                    long count = source.stream()
                            .filter(s -> s.getUnit() != null && s.getUnit().matches("[/|\\.|-]")).count();
                    if (count >= 2 && source.size() >= 3) {
                        // 2个是年开始2017-07-01，长度必须比单位大1才有意义，3-8.这种的没有意义
                        // 只识别年月日齐全的
                        fieldIndex = 0;
                    } else {
                        count = source.stream()
                                .filter(s -> s.getUnit() != null && s.getUnit().matches("[：|:]")).count();
                        if (count > 0 && count < 3) {
                            return unitWords(source, timeTypes);
                        }
                        // 无法确认
                        return null;
                        // return unitWords(source, null);
                    }
                }
            }
            int index = 0;
            StringBuffer str = new StringBuffer();
            while (fieldIndex < DATE_FIELDS.length && index < source.size()) {
                str.append(source.get(index).getWord());
                int field = DATE_FIELDS[fieldIndex++];
                int value = (int) source.get(index++).getPeriod();
                if (field == Calendar.MONTH) {
                    value--;
                }
                c.set(field, value);
            }

            // 剩余的再加入string
            while (index < source.size()) {
                str.append(source.get(index++).getWord());
            }
            // 剩余的全都设置0
            while (fieldIndex < DATE_FIELDS.length) {
                c.set(DATE_FIELDS[fieldIndex++], 0);
            }

            Word word = getWord(str.toString(), first.getStart(), source.get(source.size() - 1).getEnd(),
                    timeTypes);
            Long current = System.currentTimeMillis();
            Long time = c.getTimeInMillis();
            // 小于当前时间才有意义
            if (current > time) {
                word.setPeriod(com.hm.apollo.framework.utils.NumberUtils.format((current - time) / 86400000D,
                        "0.00"));
                word.setUnit("天");
            }
            word.setPeriodTime(false);
            return word;
        } else {
            // 暂定无法识别
            // return unitWords(source, null);
            return null;
        }
    }

    private Word unitWords(List<Word> words, List<Integer> types) {
        StringBuffer str = new StringBuffer();
        for (Word word : words) {
            str.append(word.getWord());
        }

        Word word = getWord(str.toString(), words.get(0).getStart(), words.get(words.size() - 1).getEnd(),
                types);

        return word;
    }

    private static Pattern numberPattern = Pattern.compile("\\d{4}|\\d{2}|\\d{1}|(\\d+|[一二三四五六七八九十半两]+?)");

    private Word getTimeWord(Matcher m) {
        String s = m.group();
        if (s.length() == 1) {
            return null;
        }
        Word word = getWord(s, m.start(), m.end());
        // 提取数字
        Matcher mDay = numberPattern.matcher(s);
        if (mDay.find()) {
            String num = mDay.group();
            int last = mDay.end();
            while (mDay.find()) {
                // 把连续的不是单位的拼接起来，1天半
                if (last != mDay.start()) {
                    break;
                }
                num += mDay.group();
                last = mDay.end();
            }
            if (NumberUtils.isParsable(num)) {
                word.setPeriod(Double.parseDouble(num));
            } else if (num.matches("([亿|万|千|百|十]|[零一二三四五六七八九])+?")) {
                word.setPeriod(chineseNumber2Int(num));
            }
            word.setUnit(s.substring(last));
        }
        return word;
    }

    private Word getWord(String word, int start, int end, List<Integer> types) {
        Word result = getWord(word, start, end);
        if (types != null) {
            result.setTypes(types);
        }
        return result;
    }

    private Word getWord(String wordStr, int start, int end) {
        Word word = new Word();
        word.setStart(start);
        word.setEnd(end);
        word.setWord(wordStr);
        return word;
    }

    private static int chineseNumber2Int(String chineseNumber) {
        int result = 0;
        int temp = 1;// 存放一个单位的数字如：十万
        int count = 0;// 判断是否有chArr
        char[] cnArr = new char[] { '一', '二', '三', '四', '五', '六', '七', '八', '九' };
        char[] chArr = new char[] { '十', '百', '千', '万', '亿' };
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;// 判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {// 非单位，即数字
                if (c == cnArr[j]) {
                    if (0 != count) {// 添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if (b) {// 单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                        case 0:
                            temp *= 10;
                            break;
                        case 1:
                            temp *= 100;
                            break;
                        case 2:
                            temp *= 1000;
                            break;
                        case 3:
                            temp *= 10000;
                            break;
                        case 4:
                            temp *= 100000000;
                            break;
                        default:
                            break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {// 遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }

    private boolean containsType(List<Integer> source, List<Integer> target) {
        if (CollectionUtils.isEmpty(target) || CollectionUtils.isEmpty(source)) {
            return false;
        }
        for (Integer type : source) {
            if (target.contains(type)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        try {
            new TestTime().test("2017-12-06 5:13:22");
            new TestTime().test("2017-12-6 5:13:22");
            new TestTime().test("右乳癌术后3 S，4周期化疗后20日");
            new TestTime().test("余2017.07.08 17:30在全麻下行");
            new TestTime().test("50余年");
            new TestTime().test("2017/07/08 17:30感冒");
            new TestTime().test("2017/07/08 17:30感冒");
            new TestTime().test("2017-07-08 17:30感冒");
            new TestTime().test("2017年07月08日 17:30感冒");
            new TestTime().test("2017年07月08日17:30感冒");
            new TestTime().test("07-08 17:30感冒");
            new TestTime().test("07月08日17:30感冒");
            new TestTime().test("2014-07-01 17：30：22");
            new TestTime().test("39+周");
            new TestTime().test("四十周");
            new TestTime().test("1天半");
            new TestTime().test("腹泻3天，间断抽搐3次");
            new TestTime().test("2017-5-9 8:42:27");
            new TestTime().test("369度");
            new TestTime().test("FT3:5.8pmol/L");
            new TestTime().test("P 0.55mmol/L（8:20）");
            new TestTime().test("右乳癌术后3月，4周期化疗后20日");
            new TestTime().test("感冒半年");
            new TestTime().test("辅助检查：2017-7-28本院B超（USY287699");
            new TestTime().test("辅助检查：2017.6.14.建德市中医院HCG");
            new TestTime().test("尿培养 2017-6-20 15:02:30 大肠埃希");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
