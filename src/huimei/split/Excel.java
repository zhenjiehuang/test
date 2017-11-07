package huimei.split;

import com.poi.excel.base.ExcelColumn;

public class Excel {
    @ExcelColumn(columnNum = 0, columnName = "原文", length = 10000)
    private String text;
    @ExcelColumn(columnNum = 1, columnName = "病史", length = 10000)
    private String A;
    @ExcelColumn(columnNum = 2, columnName = "既往史", length = 10000)
    private String B;
    @ExcelColumn(columnNum = 3, columnName = "入院查体", length = 10000)
    private String C;
    @ExcelColumn(columnNum = 4, columnName = "辅助检查", length = 10000)
    private String D;
    @ExcelColumn(columnNum = 5, columnName = "肌电图xxxxx", length = 10000)
    private String E;
    @ExcelColumn(columnNum = 6, columnName = "诊断及诊断依据", length = 10000)
    private String F;
    @ExcelColumn(columnNum = 7, columnName = "鉴别诊断", length = 10000)
    private String G;
    @ExcelColumn(columnNum = 8, columnName = "定位诊断", length = 10000)
    private String H;
    @ExcelColumn(columnNum = 9, columnName = "定性诊断", length = 10000)
    private String I;
    @ExcelColumn(columnNum = 10, columnName = "医生签名", length = 10000)
    private String J;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getA() {
        return A;
    }

    public void setA(String a) {
        A = a;
    }

    public String getB() {
        return B;
    }

    public void setB(String b) {
        B = b;
    }

    public String getC() {
        return C;
    }

    public void setC(String c) {
        C = c;
    }

    public String getD() {
        return D;
    }

    public void setD(String d) {
        D = d;
    }

    public String getE() {
        return E;
    }

    public void setE(String e) {
        E = e;
    }

    public String getF() {
        return F;
    }

    public void setF(String f) {
        F = f;
    }

    public String getG() {
        return G;
    }

    public void setG(String g) {
        G = g;
    }

    public String getH() {
        return H;
    }

    public void setH(String h) {
        H = h;
    }

    public String getI() {
        return I;
    }

    public void setI(String i) {
        I = i;
    }

    public String getJ() {
        return J;
    }

    public void setJ(String j) {
        J = j;
    }

}
