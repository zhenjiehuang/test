package huimei.data.recognize;

import com.poi.excel.base.ExcelColumn;

public class Excel {

    @ExcelColumn(columnName = "", columnNum = 1, length = 10000)
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
