package huimei.data.recognize;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;
import com.poi.excel.base.ExcelColumn;
import com.poi.excel.base.Type;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年8月25日
 * author：huangzhenjie
 * @version 1.0
 */
public class TypeClass {

    {
        fields = TypeClass.class.getDeclaredFields();
    }

    private Field[] fields;

    @Override
    public boolean equals(Object obj) {
        try {
            return getData().equals(((TypeClass) obj).getData3());
            // for (Field field : fields) {
            // if (!field.getName().startsWith("data")) {
            // continue;
            // }
            // field.setAccessible(true);
            // Object t = field.get(this);
            // Object s = field.get(obj);
            // if (t == null && s == null) {
            // return true;
            // } else {
            // if (t != null && s != null && t.equals(s)) {
            // continue;
            // }
            //
            // return false;
            // }
            // }
            // return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getData() {
        List<String> list = new ArrayList<String>();
        try {
            for (Field field : fields) {
                if (!field.getName().startsWith("data")) {
                    continue;
                }
                field.setAccessible(true);
                Object t = field.get(this);
                if (t.toString().startsWith("[")) {
                    List<Long> ls = new ArrayList<Long>();
                    JSONArray array = JSONArray.parseArray(t.toString());
                    for (int i = 0; i < array.size(); i++) {
                        Long l = array.getLong(i);
                        if (l < 20L) {
                            ls.add(l);
                        }
                    }

                    list.add(JSONObject.toJSONString(ls));
                } else {
                    if (!StringUtils.isNullOrEmpty(t.toString())) {
                        list.add(t.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JSONObject.toJSONString(list);
    }

    @ExcelColumn(columnNum = 0, columnName = "0", columnType = Type.Num, length = 100000, none = true)
    private Integer count = 0;

    @ExcelColumn(columnNum = 1, columnName = "0", columnType = Type.String, length = 100000, none = true)
    private String data0;

    @ExcelColumn(columnNum = 2, columnName = "1", columnType = Type.String, length = 100000, none = true)
    private String data1;

    @ExcelColumn(columnNum = 3, columnName = "2", columnType = Type.String, length = 100000, none = true)
    private String data2;

    @ExcelColumn(columnNum = 4, columnName = "3", columnType = Type.String, length = 100000, none = true)
    private String data3;

    @ExcelColumn(columnNum = 5, columnName = "4", columnType = Type.String, length = 100000, none = true)
    private String data4;

    @ExcelColumn(columnNum = 6, columnName = "5", columnType = Type.String, length = 100000, none = true)
    private String data5;

    @ExcelColumn(columnNum = 7, columnName = "6", columnType = Type.String, length = 100000, none = true)
    private String data6;

    @ExcelColumn(columnNum = 8, columnName = "7", columnType = Type.String, length = 100000, none = true)
    private String data7;

    @ExcelColumn(columnNum = 9, columnName = "8", columnType = Type.String, length = 100000, none = true)
    private String data8;

    @ExcelColumn(columnNum = 10, columnName = "9", columnType = Type.String, length = 100000, none = true)
    private String data9;


    public String getData0() {
        return data0;
    }

    public void setData0(String data0) {
        this.data0 = data0;
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

    public String getData9() {
        return data9;
    }

    public void setData9(String data9) {
        this.data9 = data9;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public TypeClass addCount() {
        if (count == null) {
            count = 0;
        }
        count++;
        return this;
    }

}
