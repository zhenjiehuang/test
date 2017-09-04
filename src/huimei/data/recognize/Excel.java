package huimei.data.recognize;

import com.poi.excel.base.ExcelColumn;

public class Excel {

    @ExcelColumn(columnName = "eqwe", columnNum = 0, length = 10000)
    private String data;

    @ExcelColumn(columnName = "eqweqq", columnNum = 1, length = 10000)
    private String data2;

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

}
