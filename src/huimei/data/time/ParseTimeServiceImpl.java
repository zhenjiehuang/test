package huimei.data.time;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hm.apollo.module.cdss.pojo.HttpRequestProperty;
import com.poi.excel.parse.ImportExcel;

/**
 * @author lipeng
 * @date 2017/7/20
 */
@Service
public class ParseTimeServiceImpl {
    @Autowired
    HttpRequestProperty httpRequestProperty;
    private static Map<String, Integer> numMap = null;
    private static Map<String, Double> unitMap = null;
    static String cnNums = null;
    static String pat = null;
    static String pat2 = null;
    static String pat3 = null;

    static {
        numMap = new HashMap<>();
        numMap.put("一", 1);
        numMap.put("二", 2);
        numMap.put("三", 3);
        numMap.put("四", 4);
        numMap.put("五", 5);
        numMap.put("六", 6);
        numMap.put("七", 7);
        numMap.put("八", 8);
        numMap.put("九", 9);
        numMap.put("十", 10);
        numMap.put("两", 2);

        unitMap = new HashMap<>();
        unitMap.put("天", 1D);
        unitMap.put("周", 7D);
        unitMap.put("年", 360D);
        unitMap.put("月", 30D);
        unitMap.put("小时", 0.04166667);
        unitMap.put("礼拜", 7D);
        unitMap.put("星期", 7D);

        cnNums = StringUtils.join(numMap.keySet(), "{1}|");
//        pat = "([([\\d|\\.]{1,4})|" + cnNums + "{1,3}|半]+个?)([天|日|周|年|月]{1}|[小时|礼拜|星期]{2})(前|以前|之前)?";
        pat = "(((\\d{2})?\\d{2}[年-]\\d\\d?[月-](\\d\\d?日?)? *(\\d\\d?([:：时点]\\d\\d?([:：分]\\d\\d?秒?)?)?)?)|(\\d\\d?([:：时点]\\d\\d?([:：分]\\d\\d?秒?)?)))|((\\d+|[一二三四五六七八九十半两]+?)个?余?([天日周年月]|(小时|星期|礼拜))(余|前|以前|之前)?)";
        pat2 = "([[\\u4e00-\\u9fa5]|\\pP]+)([([\\d|\\.]{1,4})|" + cnNums + "{1,3}]+个?)([天|日|周|年|月]{1}|[小时|礼拜|星期]{2})";
        pat3 = "([([\\d|\\.]{1,4})|" + cnNums + "{1,3}]+个?)([天|日|周|年|月]{1}|[小时|礼拜|星期]{2})([前|以前|之前]?)([[\\u4e00-\\u9fa5]|\\pP]+)";
//        pat3 = "([(\\d{1,4})|" + cnNums + "{1,3}]+个?)([天|日|周|年|月|小时|礼拜|星期]{1})([\\u4e00-\\u9fa5]+)";
    }

    public List<String> parseTime(String txt) {
        List<String> re = new ArrayList<String>();
        String pat = "(((\\d{2})?\\d{2}[年-]\\d\\d?[月-](\\d\\d?日?)? *(\\d\\d?([:：时点]\\d\\d?([:：分]\\d\\d?秒?)?)?)?)"
                + "|(\\d\\d?([:：时点]\\d\\d?([:：分]\\d\\d?秒?)?)))" + "|((\\d+|[一二三四五六七八九十半两]+?))";
        // 个?余?([天日周年月]|(小时|星期|礼拜))(余|前|以前|之前)?
        try {
            Pattern p = Pattern.compile(pat);
            Matcher m = p.matcher(txt);
            while (m.find()) {
                String timeDesc = m.group();
                // System.out.print(timeDesc + "\t");
                // System.out.print(m.start() + "\t");
                // System.out.println(m.end());
                System.out.println(txt.substring(m.start() - 2, m.end() + 5));
                re.add(timeDesc);
                // 时间标准化，统一为以天为单位的数字，目前只做时间段的标准化
                // 数值
                // String period = m.group(12);
                String period = "";
                // 单位
                // String unit = m.group(13);
                String unit = "";
                if (period != null && unit != null) {
                    // 数值转换为double，包括数字和汉语数字描述
                    Double num = NumberUtils.isNumber(period) ? Double.valueOf(period) : chineseNumber2Int(period);
                    // 单位对应的天数
                    Double dayOfUnit = unitMap.get(unit);
                    if (num != null && dayOfUnit != null) {
                        // 标准化为天数
                        Double time = num * dayOfUnit;
                        time = com.hm.apollo.framework.utils.NumberUtils.format(time, "0.00");
                        // httpRequestProperty.setDayCountOfTime(timeDesc,
                        // time);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public List<String> parseTime1(String txt) {
        List<String> re = new ArrayList<String>();
        try {
            Pattern p = Pattern.compile(pat);
            Matcher m = p.matcher(txt);
            while (m.find()) {
                String timeDesc = m.group();
                System.out.println(timeDesc);
                re.add(timeDesc);
                // 时间标准化，统一为以天为单位的数字，目前只做时间段的标准化
                // 数值
                long s = System.nanoTime();
                String period = m.group(12);
                System.out.println(period);
                // 单位
                s = System.nanoTime();
                String unit = m.group(13);
                System.out.println(unit);
                if (period != null && unit != null) {
                    // 数值转换为double，包括数字和汉语数字描述
                    Double num = NumberUtils.isNumber(period) ? Double.valueOf(period)
                            : chineseNumber2Int(period);
                    // 单位对应的天数
                    Double dayOfUnit = unitMap.get(unit);
                    if (num != null && dayOfUnit != null) {
                        // 标准化为天数
                        Double time = num * dayOfUnit;
                        time = com.hm.apollo.framework.utils.NumberUtils.format(time, "0.00");
                        // httpRequestProperty.setDayCountOfTime(timeDesc,
                        // time);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    private static int chineseNumber2Int(String chineseNumber) {
        int result = 0;
        int temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};
        char[] chArr = new char[]{'十', '百', '千', '万', '亿'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if (0 != count) {//添加下一个单位之前，先把上一个单位值添加到结果中
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
            if (b) {//单位{'十','百','千','万','亿'}
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
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }

    static {
        // WordDictionary.getInstance().init(Paths.get("D:\\huimei\\work\\apollo\\src\\main\\resources\\dict"));
    }

    private void xls() {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + ParseTimeServiceImpl.class.getPackage().getName().replace('.', '\\');
        String fileName = "1.xls";
        try {
            ImportExcel<ExcelTime> excel = new ImportExcel<>(new File(path, fileName), ExcelTime.class);
            for (ExcelTime time : excel.getRowDatas()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // JiebaSegmenter segmenter = new JiebaSegmenter();
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + ParseTimeServiceImpl.class.getPackage().getName().replace('.', '\\');
        String fileName = "2.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));

            ParseTimeServiceImpl service = new ParseTimeServiceImpl();
            String line = null;
            while ((line = br.readLine()) != null) {
                long s = System.nanoTime();
                service.parseTime1(line);
                System.out.println(line);
                System.out.println(System.nanoTime() - s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}