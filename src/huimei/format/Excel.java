package huimei.format;

import com.poi.excel.base.ExcelColumn;

public class Excel {

    @ExcelColumn(columnName = "原文", columnNum = 0, length = 10000)
    private String data1;

    @ExcelColumn(columnName = "格式化", columnNum = 1, length = 10000)
    private String data2;

    @ExcelColumn(columnName = "识别", columnNum = 2, length = 10000)
    private String data3;

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
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
}
