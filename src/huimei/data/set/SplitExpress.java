package huimei.data.set;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.poi.excel.parse.ExportExcel;

import huimei.data.recognize.Excel;

public class SplitExpress {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + SetFilter.class.getPackage().getName().replace('.', '\\');
        String fileName = "express";
        Set<String> exam = new HashSet<String>();
        Set<String> hit = new HashSet<String>();
        try {
            Files.lines(Paths.get(path + "\\" + fileName)).forEach(str -> {
                String[] sss = str.split("｜");
                for (String ss : sss) {
                    if (StringUtils.isNotBlank(ss)) {
                        String[] s = ss.split("&");
                        for (String sts : s) {
                            if (sts.contains(">") || sts.contains("=") || sts.contains("<")
                                    || sts.contains("等于") || sts.contains("大于") || sts.contains("小于")
                                    || sts.contains("大于等于") || sts.contains("小于等于") || sts.contains("小于等于")
                                    || sts.contains("高于") || sts.contains("低于")) {
                                exam.add(sts);
                            } else {
                                hit.add(sts);
                            }
                        }
                    }
                }
            });

            List<Excel> es = new ArrayList<>();
            for (String s : exam) {
                Excel e = new Excel();
                e.setData(s);
                es.add(e);
            }
            ExportExcel<Excel> ex = new ExportExcel<Excel>(es, Excel.class);
            ex.saveFile(new File("D://1/检验.xls"));

            es = new ArrayList<>();
            for (String s : hit) {
                Excel e = new Excel();
                e.setData(s);
                es.add(e);
            }

            ex = new ExportExcel<Excel>(es, Excel.class);
            ex.saveFile(new File("D://1/规则.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
