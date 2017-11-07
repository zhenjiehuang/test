package huimei.split;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.poi.excel.parse.ExportExcel;

import huimei.split.model.KeySegment;

public class SplitTest3 {

    public static void main(String[] args) {
        try {
            SpliteTest2 split = new SpliteTest2();
            split.init();

            BufferedReader br = new BufferedReader(new FileReader(
                    new File("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\split\\xuanwu.txt")));
            String line = null;
            List<Excel> excels = new ArrayList<Excel>();
            while ((line = br.readLine()) != null) {
                Map<String, KeySegment> map = split.parser(line, 2);
                Excel excel = new Excel();
                excel.setText(line);
                excel.setA(map.get("A").getText());
                excel.setB(map.get("B").getText());
                excel.setC(map.get("C").getText());
                excel.setD(map.get("D").getText());
                excel.setE(map.get("E").getText());
                excel.setF(map.get("F").getText());
                excel.setG(map.get("G").getText());
                excel.setH(map.get("H").getText());
                excel.setI(map.get("I").getText());
                excel.setJ(map.get("J").getText());
                excels.add(excel);
            }
            br.close();
            ExportExcel<Excel> export = new ExportExcel<>(excels, Excel.class);
            export.saveFile(new File("D://11-06.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
