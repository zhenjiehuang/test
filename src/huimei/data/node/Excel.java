package huimei.data.node;

import com.poi.excel.base.ExcelColumn;

public class Excel {

    @ExcelColumn(columnName = "", columnNum = 0, length = 10000)
    private String data1;

    @ExcelColumn(columnName = "", columnNum = 2, length = 10000)
    private String data3;

    @ExcelColumn(columnName = "", columnNum = 5, length = 10000)
    private String data6;

    @ExcelColumn(columnName = "", columnNum = 7, length = 10000)
    private String data7;

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData6() {
        return data6;
    }

    public void setData6(String data6) {
        this.data6 = data6;
    }

    public String getData7() {
        return data7;
    }

    public void setData7(String data7) {
        this.data7 = data7;
    }

}
