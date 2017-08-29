package huimei.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;

/**
 * Description: Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月12日
 * 
 * author：huangzhenjie
 * 
 * @version 1.0
 */
public class JianchajieguoFile {

    static byte[] textSplit = "<BodyText>".getBytes();

    static Connection con = null;

    static BufferedWriter bw = null;

    static {
        try {
            bw = new BufferedWriter(new FileWriter(new File("D://1.sql")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void match(String data) {
        String[] columns = { "病历号", "住院ID", "姓名", "性别", "生日", "科室", "项目", "模态", "检查描述", "检查结果", "检查诊断" };
        String[] cs = { "charts_id", "hospitalized_id", "name", "gender", "birthday", "departments", "item",
                "mode", "description", "result", "diagnosis" };

        StringBuffer str = new StringBuffer("insert into jiande_examination_result (");
        for (String c : cs) {
            str.append(c).append(",");
        }

        str.setCharAt(str.length() - 1, ')');
        str.append(" values (");

        for (String column : columns) {
            int start = data.indexOf(column) + column.length() + 1;
            int end = data.indexOf(column, start) - 2;
            str.append("\"").append(data.substring(start, end)).append("\",");
            // System.out.println(data.substring(start, end));
        }
        str.setCharAt(str.length() - 1, ')');
        str.append(";");
        try {
            bw.write(str.toString());
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void string() {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + JianchajieguoFile.class.getPackage().getName().replace('.', '\\');
        String fileName = "检查结果.xml";

        int indexLine = 0;
        String line = null;
        boolean dataStart = false;
        StringBuffer data = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(path, fileName)));

            while ((line = br.readLine()) != null
            // && indexLine++ < 2000 //
                    ) {
                if (line.contains("<RECORD>")) {
                    data = new StringBuffer();
                    dataStart = true;
                    continue;
                } else if (line.contains("</RECORD>")) {
                    match(data.toString());
                }

                if (dataStart) {
                    data.append(line);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        string();
        // match("<病历号>320748</病历号><住院ID>10082219</住院ID><姓名>谢恩梁</姓名><性别>男</性别><生日>1991-10-10
        // 00:00:00</生日><科室>泌尿外科</科室><项目>立位胸片(正位)</项目><模态>DR</模态><检查描述>两侧胸廓对称，气管居中。两侧肺野清晰，肺纹理走向规则，未见实质性病变。两侧肺门结构正常。纵隔不宽。心影大小形态正常。两侧膈面光滑，肋膈角锐利。</检查描述><检查结果>两肺及心膈未见异常。</检查结果><检查诊断></检查诊断>");
    }
}
