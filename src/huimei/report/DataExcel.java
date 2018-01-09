package huimei.report;

import com.poi.excel.base.ExcelColumn;

public class DataExcel {

    @ExcelColumn(columnNum = 0, columnName = "医院id")
    private String hospital;

    @ExcelColumn(columnNum = 1, columnName = "患者id")
    private String patient;

    @ExcelColumn(columnNum = 2, columnName = "医生id")
    private String doctor;

    @ExcelColumn(columnNum = 3, columnName = "病例id")
    private String cases;

    @ExcelColumn(columnNum = 4, columnName = "时间")
    private String requstTime;

    @ExcelColumn(columnNum = 6, columnName = "操作")
    private String opr;

    @ExcelColumn(columnNum = 7, columnName = "content")
    private String content;

    @ExcelColumn(columnNum = 8, columnName = "查询串")
    private String query;

    @ExcelColumn(columnNum = 9, columnName = "疾病")
    private String disease;

    @ExcelColumn(columnNum = 10, columnName = "tab")
    private String tab;

    @ExcelColumn(columnNum = 11, columnName = "authkey")
    private String authkey;

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getOpr() {
        return opr;
    }

    public void setOpr(String opr) {
        this.opr = opr;
    }

    public String getRequstTime() {
        return requstTime;
    }

    public void setRequstTime(String requstTime) {
        this.requstTime = requstTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getAuthkey() {
        return authkey;
    }

    public void setAuthkey(String authkey) {
        this.authkey = authkey;
    }

}
