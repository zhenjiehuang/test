package huimei.data.time;

import com.poi.excel.base.ExcelColumn;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月25日
 * author：huangzhenjie
 * @version 1.0
 */
public class ExcelTime {

    @ExcelColumn(columnName = "text", columnNum = 1)
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
