package huimei.data.node;

import com.poi.excel.base.ExcelColumn;
import com.poi.excel.base.Type;

public class Excel2 {

    private String sheet;

    @ExcelColumn(columnName = "ID", columnNum = 3, columnType = Type.Num, length = 10000)
    private Long id;

    @ExcelColumn(columnName = "词", columnNum = 0, length = 10000)
    private String data1;

    @ExcelColumn(columnName = "是否存在", columnNum = 1, length = 10000)
    private String data2;

    @ExcelColumn(columnName = "是否审核通过", columnNum = 2, length = 10000)
    private String data3;

    public Excel2(String sheet, Long id, String data1, String data2, String data3) {
        super();
        this.sheet = sheet;
        this.id = id;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
    }

    public Excel2() {
    }

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

    public String getSheet() {
        return sheet;
    }

    public void setSheet(String sheet) {
        this.sheet = sheet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return data1.equals(((Excel2) obj).getData1());
    }

}
