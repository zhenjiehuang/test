package huimei.data.recognize;

import java.lang.reflect.Field;

import com.poi.excel.base.ExcelColumn;

public class Linchuangbiaoxian {

    @ExcelColumn(columnName = "0", columnNum = 0, length = 10000)
    private String data1;

    @ExcelColumn(columnName = "1", columnNum = 1, length = 10000)
    private String data2;

    @ExcelColumn(columnName = "2", columnNum = 2, length = 10000)
    private String data3;

    @ExcelColumn(columnName = "3", columnNum = 3, length = 10000)
    private String data4;

    @ExcelColumn(columnName = "4", columnNum = 4, length = 10000)
    private String data5;

    @ExcelColumn(columnName = "5", columnNum = 5, length = 10000)
    private String data6;

    @ExcelColumn(columnName = "6", columnNum = 6, length = 10000)
    private String data7;

    @ExcelColumn(columnName = "7", columnNum = 7, length = 10000)
    private String data8;

    public int getDataLength() {
        int count = 0;
        try {
            Field[] fields = Linchuangbiaoxian.class.getDeclaredFields();
            for (Field field : fields) {
                Object value = field.get(this);
                if (value != null && value.toString().length() != 0) {
                    count++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("1234er566u7i");
        }

        return count;
    }

    public String getDataIndex(int index) {
        try {
            Field field = Linchuangbiaoxian.class.getDeclaredField("data" + (index));
            Object value = field.get(this);
            return value.toString();
        } catch (Exception e) {
            throw new RuntimeException("1234er566u7i");
        }
    }

    public boolean contain() {
        try {
            Field[] fields = Linchuangbiaoxian.class.getDeclaredFields();
            for (Field field : fields) {
                Object value = field.get(this);
                if (value != null && value.toString().startsWith("[")) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("985746534er");
        }

        return false;
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

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
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

    public String getData8() {
        return data8;
    }

    public void setData8(String data8) {
        this.data8 = data8;
    }

}
