package huimei.test;

import com.poi.excel.base.ExcelColumn;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月14日
 * author：huangzhenjie
 * @version 1.0
 */
public class TT {

    @ExcelColumn(columnNum = 0, columnName = "MOBANMC")
    private String mobanmc;

    @ExcelColumn(columnNum = 1, columnName = "bodyMessage")
    private String bodyMessage;

    public String getMobanmc() {
        return mobanmc;
    }

    public void setMobanmc(String mobanmc) {
        this.mobanmc = mobanmc;
    }

    public String getBodyMessage() {
        return bodyMessage;
    }

    public void setBodyMessage(String bodyMessage) {
        this.bodyMessage = bodyMessage;
    }

    public static void main(String[] args) {
        System.out.println((char) 12288);
    }

}
