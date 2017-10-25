package huimei.data.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.hm.apollo.module.cdss.enums.WordTypeEnum;
import com.hm.apollo.module.recognition.pojo.Word;

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

    public void test(String str) {
        try {
            Pattern p = Pattern.compile("(" //
                    + "(\\d{4}|\\d{2}|\\d{1})?"// 年月日形式
                    + "|(\\d+|[一二三四五六七八九十半两]+?)"// 中文数字
                    + ")(" // 括号分隔
                    + "(多|余|\\+|个)?"// xx余[年月日]
                    + "(/|\\.|-|:|：|时|点|分|秒|S|s|天|日|号|周期|周|年|月|小时|星期|礼拜) ?"// 时间分隔
                    + "|(\\d{4}|\\d{2}|\\d{1}) ?"// 防止2017-07-08最后一个数字没有识别
                    + ")"// 括号分隔
                    + "(半|余|\\+|前|以前|之前)?");

            Matcher m = p.matcher(str);

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
            timeWords.stream().filter(ws -> ws.size() > 0 && !StringUtils.isEmpty(ws.get(0).getUnit()))
                    .forEach(ws -> {
                        Word word = getDateWord(ws);
                        if (word != null) {
                            words.add(word);
                        }
                    });

            words.stream().forEach(w -> System.out.println(JSONObject.toJSONString(w)));
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Word getDateWord(List<Word> source) {
        Word first = source.get(0);
        if (source.size() == 1) {
            // 只有一个时
            if (first.getUnit().matches("[/|\\.|-]")) {
                // 无意义
                return null;
            } else {
                first.setTypes(Arrays.asList(WordTypeEnum.时间词.getType()));
                return first;
            }
        } else if (!StringUtils.isEmpty(first.getUnit()) && first.getUnit().matches("[/|:|：|\\.|年|月|-]")) {
            Calendar c = Calendar.getInstance();
            int fieldIndex = 0;// DATE_FIELDS对应的下标
            if (first.getUnit().equals("年")) {
                // 确定是年开始
                fieldIndex = 0;
            } else if (first.getUnit().equals("月")) {
                // 确定是月开始
                fieldIndex = 1;
            } else {
                // 剩下2017-07-01,2017/07/01,2017.07.01这种形式
                if (source.size() >= 2) {
                    long count = source.stream()
                            .filter(s -> s.getUnit().matches("[/|\\.|-]")).count();
                    if (count >= 2) {
                        // 2个是年开始2017-07-01
                        // 只识别年月日齐全的
                        fieldIndex = 0;
                    } else {
                        // 无法确认
                        return null;
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
                    WordTypeEnum.时间词);
            Long current = System.currentTimeMillis();
            Long time = c.getTimeInMillis();
            // 小于当前时间才有意义
            if (current > time) {
                word.setPeriod(com.hm.apollo.framework.utils.NumberUtils.format((current - time) / 86400000D,
                        "0.00"));
                word.setUnit("天");
            }
            return word;
        } else {
            // 暂定无法识别
            // return unitWords(source, null);
            return null;
        }
    }

    private Word unitWords(List<Word> words, WordTypeEnum type) {
        StringBuffer str = new StringBuffer();
        for (Word word : words) {
            str.append(word.getWord());
        }

        Word word = getWord(str.toString(), words.get(0).getStart(), words.get(words.size() - 1).getEnd(),
                type);

        return word;
    }

    private Word getTimeWord(Matcher m) {
        String s = m.group();
        if (s.length() == 1) {
            return null;
        }
        Word word = getWord(s, m.start(), m.end());
        // 提取数字
        Matcher mDay = Pattern.compile("\\d{4}|\\d{2}|\\d{1}|(\\d+|[一二三四五六七八九十半两]+?)").matcher(s);
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

    private Word getWord(String word, int start, int end, WordTypeEnum type) {
        Word result = getWord(word, start, end);
        if (type != null) {
            result.setTypes(Arrays.asList(type.getType()));
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

    public static void main(String[] args) {
        try {
            // new TestTime().test("20170908");
            new TestTime().test("右乳癌术后3 S，4周期化疗后20日");
            // new TestTime().test("余2017.07.08 17:30在全麻下行");
            // new TestTime().test("50余年");
            // new TestTime().test("2017/07/08 17:30感冒");
            // new TestTime().test("2017/07/08 17:30感冒");
            // new TestTime().test("2017-07-08 17:30感冒");
            // new TestTime().test("2017年07月08日 17:30感冒");
            // new TestTime().test("2017年07月08日17:30感冒");
            // new TestTime().test("07-08 17:30感冒");
            // new TestTime().test("07月08日17:30感冒");
            // new TestTime().test("2014-07-01 17：30：22");
            // new TestTime().test("39+周");
            // new TestTime().test("四十周");
            // new TestTime().test("1天半");
            // new TestTime().test("腹泻3天，间断抽搐3次");
            // new TestTime().test("2017-5-9 8:42:27");
            // new TestTime().test("369度");
            // new TestTime().test("FT3:5.8pmol/L");
            // new TestTime().test("P 0.55mmol/L（8:20）");
            // new TestTime().test("右乳癌术后3月，4周期化疗后20日");
            // new TestTime().test("感冒半年");
            // new TestTime().test("辅助检查：2017-7-28本院B超（USY287699");
            // new TestTime().test("辅助检查：2017.6.14.建德市中医院HCG");
            // new TestTime().test("尿培养 2017-6-20 15:02:30 大肠埃希");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
