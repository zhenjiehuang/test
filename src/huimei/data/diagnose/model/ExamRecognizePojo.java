package huimei.data.diagnose.model;

import java.util.HashSet;
import java.util.Set;

import com.hm.apollo.framework.utils.RegexUtils;

/**
 * @author lipeng
 * @date 2017/6/1
 */
public class ExamRecognizePojo {
    String itemWord;
    String value;
    String examWord;
    String callbackWord;
    String valueDescription;
    Double numberValue;
    Double age;
    String unit;
    Double value1;
    Double value2;
    // 比较类型
    Integer compareType;
    static Set<String> numberTextValue = new HashSet<>();

    static {
        numberTextValue.add("+1");
        numberTextValue.add("+2");
        numberTextValue.add("+3");
        numberTextValue.add("+4");
        numberTextValue.add("1+");
        numberTextValue.add("2+");
        numberTextValue.add("3+");
        numberTextValue.add("4+");

    }

    public boolean containsNumberTextValue(String str) {
        for (String s : numberTextValue) {
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public ExamRecognizePojo() {
    }

    public ExamRecognizePojo(String itemWord, String unit, String value) {
        this.itemWord = itemWord;
        this.unit = unit;
        this.value = value;

    }

    public ExamRecognizePojo(String examWord, String itemWord, String valueDescription, String unit) {
        this.examWord = examWord;
        this.itemWord = itemWord;
        this.valueDescription = valueDescription;
        if (unit != null) {
            this.valueDescription += unit;
        }
        this.unit = unit;
        Double number = RegexUtils.matchNumber(valueDescription);
        if (number != null && !containsNumberTextValue(valueDescription)) {
            this.value = String.valueOf(number);
            this.numberValue = number;
        } else {
            this.value = valueDescription;
        }

    }

    public String getItemWord() {
        return itemWord;
    }

    public void setItemWord(String itemWord) {
        this.itemWord = itemWord;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExamWord() {
        return examWord;
    }

    public void setExamWord(String examWord) {
        this.examWord = examWord;
    }

    public String getCallbackWord() {
        return callbackWord;
    }

    public void setCallbackWord(String callbackWord) {
        this.callbackWord = callbackWord;
    }

    public String getValueDescription() {
        return valueDescription;
    }

    public void setValueDescription(String valueDescription) {
        this.valueDescription = valueDescription;
    }

    public Double getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(Double numberValue) {
        this.numberValue = numberValue;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }

    public Integer getCompareType() {
        return compareType;
    }

    public void setCompareType(Integer compareType) {
        this.compareType = compareType;
    }
}
