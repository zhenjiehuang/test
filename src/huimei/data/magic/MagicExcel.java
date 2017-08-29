package huimei.data.magic;

import com.poi.excel.base.ExcelColumn;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月20日
 * author：huangzhenjie
 * @version 1.0
 */
public class MagicExcel {

    @ExcelColumn(columnNum = 0, columnName = "")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
