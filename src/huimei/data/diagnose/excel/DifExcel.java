package huimei.data.diagnose.excel;

import com.poi.excel.base.ExcelColumn;

public class DifExcel {

    @ExcelColumn(columnName = "disease", columnNum = 0, length = 10000)
    private String str1;

    @ExcelColumn(columnName = "8080", columnNum = 1, length = 10000)
    private String str2;

    @ExcelColumn(columnName = "8081", columnNum = 2, length = 10000)
    private String str3;

    @ExcelColumn(columnName = "dif", columnNum = 3, length = 10000)
    private String str4;

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public String getStr3() {
        return str3;
    }

    public void setStr3(String str3) {
        this.str3 = str3;
    }

    public String getStr4() {
        return str4;
    }

    public void setStr4(String str4) {
        this.str4 = str4;
    }

}
