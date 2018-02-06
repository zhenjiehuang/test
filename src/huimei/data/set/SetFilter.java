package huimei.data.set;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.poi.excel.parse.ExportExcel;

import huimei.data.recognize.Excel;

public class SetFilter {

    public static void main(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + SetFilter.class.getPackage().getName().replace('.', '\\');
        // WordDictionary.getInstance().init(Paths.get(path));
        String fileName = "set2.txt";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));

            String line = null;

            List<Excel> es = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                }
                String[] s = line.split(",");
                Excel e = new Excel();
                e.setData(s[0].trim());
                e.setData2(s[1].trim());
                es.add(e);
            }
            ExportExcel<Excel> ex = new ExportExcel<>(es, Excel.class);
            ex.saveFile("D://test.xls");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main1(String[] args) {
        String resultPath = "D://1";
        String charset = "GBk";
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + SetFilter.class.getPackage().getName().replace('.', '\\');
        // WordDictionary.getInstance().init(Paths.get(path));
        String fileName = "set.txt";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(path, fileName))));

            String line = null;

            Set<String> lines = new HashSet<String>();
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            StringBuffer ss = new StringBuffer();
            for (String s : lines) {
                // ss.append("insert into node_temp (str) values (");
                // ss.append("\"");
                // ss.append(s);
                // ss.append("\");\n");
                ss.append("\"");
                ss.append(s);
                ss.append("\",");
            }
            System.out.println(ss);

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
