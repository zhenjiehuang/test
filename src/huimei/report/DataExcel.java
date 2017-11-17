package huimei.report;

import com.poi.excel.base.ExcelColumn;

public class DataExcel {

    @ExcelColumn(columnNum = 0, columnName = "医院")
    private String hospital;

    @ExcelColumn(columnNum = 1, columnName = "患者")
    private String patient;

    @ExcelColumn(columnNum = 2, columnName = "医生")
    private String doctor;

    @ExcelColumn(columnNum = 3, columnName = "病例")
    private String cases;

    @ExcelColumn(columnNum = 5, columnName = "操作")
    private String opr;

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

}
