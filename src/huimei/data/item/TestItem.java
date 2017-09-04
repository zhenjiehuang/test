package huimei.data.item;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

import com.hm.apollo.module.cdss.pojo.ExamRecognizePojo;

public class TestItem {

    private static char[] cs = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public static List<String> parseItemWords(String str) {
        String patternStr = "(>|＞|>=|＞=|≥|<|＜|<=|＜=|≤|=|大于|小于|等于|大于等于|小于等于|高于|低于)?" + "("
                + "(([\\d]+([.]{1}[\\d]+){0,1})\\s?[:|：|∶|/|／|^]\\s?([\\d]+([.]{1}[\\d]+){0,1}))"
                + "|([\\d]+([.]{1}[\\d]+){0,1})" + ")+" + "(以上|以下)?";
        List<String> re = new ArrayList<>();
        try {
            Pattern p = Pattern.compile(patternStr);
            Matcher m = p.matcher(str);
            while (m.find()) {

                String s = m.group();
                String value = m.group(2);

                // 纯数字不做处理，后面逻辑搞定
                // TODO 如果str包含的项目值包含数字的话，识别可能出问题，例如ANA1:40
                // 应为ANA1=40，但是会识别为ANA=1:40
                if (org.apache.commons.lang3.StringUtils.isNotBlank(s)
                        && !org.apache.commons.lang3.StringUtils.containsNone(s, cs) && value != null) {
                    ExamRecognizePojo examRecognizePojo = new ExamRecognizePojo();
                    examRecognizePojo.setValueDescription(s);
                    if (NumberUtils.isNumber(value)) {
                        examRecognizePojo.setNumberValue(Double.valueOf(value));
                    }
                    // 解析A:B
                    String[] vs = null;
                    if (value.contains(":")) {
                        vs = value.split(":");
                    } else if (value.contains("：")) {
                        vs = value.split("：");
                    } else if (value.contains("∶")) {
                        vs = value.split("∶");
                    }
                    if (vs != null && vs.length == 2) {
                        String s1 = vs[0].trim();
                        String s2 = vs[1].trim();
                        if (NumberUtils.isNumber(s1) && NumberUtils.isNumber(s2)) {
                            Double d1 = Double.valueOf(s1);
                            Double d2 = Double.valueOf(s2);
                            if (d2 != 0) {
                                Double numberValue = d1 / d2;
                                numberValue = com.hm.apollo.framework.utils.NumberUtils.format(numberValue,
                                        "0.00000000");
                                examRecognizePojo.setNumberValue(numberValue);
                            }
                        }
                    }

                    // 解析A/B型
                    String[] vs2 = null;
                    if (value.contains("/")) {
                        vs2 = value.split("/");
                    } else if (value.contains("／")) {
                        vs2 = value.split("／");
                    }
                    if (vs2 != null && vs2.length == 2) {
                        String s1 = vs2[0].trim();
                        String s2 = vs2[1].trim();
                        if (NumberUtils.isNumber(s1) && NumberUtils.isNumber(s2)) {
                            Double d1 = Double.valueOf(s1);
                            Double d2 = Double.valueOf(s2);
                            examRecognizePojo.setValue1(d1);
                            examRecognizePojo.setValue2(d2);
                        }
                    }
                    examRecognizePojo.setValue(value);
                    re.add(s);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public static void main(String[] args) {
        System.out.println(parseItemWords("C反应蛋白     <1.28 mg/L"));
    }
}
