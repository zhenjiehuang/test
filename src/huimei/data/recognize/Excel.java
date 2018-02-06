package huimei.data.recognize;

import com.poi.excel.base.ExcelColumn;

public class Excel {

    @ExcelColumn(columnName = "分型词", columnNum = 0, length = Integer.MAX_VALUE)
    private String data;

    @ExcelColumn(columnName = "核心词", columnNum = 1, length = 10000)
    private String data2;

    @ExcelColumn(columnName = "核心词是否存在", columnNum = 2, length = 10000)
    private String has;

    @ExcelColumn(columnName = "分词", columnNum = 3, length = 10000)
    private String data3;

    @ExcelColumn(columnName = "分词词性", columnNum = 4, length = 10000)
    private String wordType;

    @ExcelColumn(columnName = "召回词", columnNum = 5, length = 10000)
    private String callBacks;

    @ExcelColumn(columnName = "concepts", columnNum = 6, length = 10000)
    private String concepts;

    @ExcelColumn(columnName = "祖节点", columnNum = 7, length = 10000)
    private String parent;

    @ExcelColumn(columnName = "同义词", columnNum = 8, length = 10000)
    private String node;

    @ExcelColumn(columnName = "fix", columnNum = 9, length = 10000)
    private String fix;

    @ExcelColumn(columnName = "", columnNum = 10, length = 10000)
    private String str;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getConcepts() {
        return concepts;
    }

    public void setConcepts(String concepts) {
        this.concepts = concepts;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCallBacks() {
        return callBacks;
    }

    public void setCallBacks(String callBacks) {
        this.callBacks = callBacks;
    }

    public String getFix() {
        return fix;
    }

    public void setFix(String fix) {
        this.fix = fix;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getWordType() {
        return wordType;
    }

    public void setWordType(String wordType) {
        this.wordType = wordType;
    }

    public String getHas() {
        return has;
    }

    public void setHas(String has) {
        this.has = has;
    }

}
