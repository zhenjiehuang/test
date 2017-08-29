package huimei.data.recognize.result;

import com.poi.excel.base.ExcelColumn;

public class RecognizedResult {

    @ExcelColumn(columnName = "", columnNum = 0, length = 10000)
    private String data1 = "";

    @ExcelColumn(columnName = "", columnNum = 1, length = 10000)
    private String data2 = "";

    @ExcelColumn(columnName = "", columnNum = 2, length = 10000)
    private String data3 = "";

    @ExcelColumn(columnName = "", columnNum = 3, length = 10000)
    private String data4 = "";

    private boolean recognized = false;

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

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public void setRecognized(boolean recognized) {
        this.recognized = recognized;
    }

    public boolean isRecognized() {
        return recognized;
    }

    @Override
    public boolean equals(Object obj) {
        return data1 != null && data1.equals(((RecognizedResult) obj).getData1());
    }

}
