package huimei.data.node;

import com.poi.excel.base.ExcelColumn;

public class Excel3 {

    @ExcelColumn(columnName = "项目名称", columnNum = 5)
    private String str;

    @ExcelColumn(columnName = "节点表关系", columnNum = 9)
    private String r;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

}
