package huimei.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.poi.excel.parse.ExportExcel;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月12日
 * author：huangzhenjie
 * @version 1.0
 */
public class TestFile {

    static byte[] textSplit = "<BodyText>".getBytes();

    private static void string() {
        int indexLine = 0;
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("1.xml")));
            List<TT> ts = new ArrayList<TT>();

            TT t = null;
            StringBuffer str = null;

            boolean titleStarted = false;
            boolean started = false;
            String split = "<MOBANMC>";
            while ((line = br.readLine()) != null
            // && indexLine++ < 100
            ) {
                int index = line.indexOf(split);
                if (titleStarted) {
                    if (started) {
                        if (index == -1) {
                            str.append(line);
                        } else {
                            str.append(line.substring(0, index));
                            t.setBodyMessage(str.toString());
                            started = false;
                            titleStarted = false;
                            split = "<MOBANMC>";
                            if (str.length() < 32767) {
                                ts.add(t);
                            }
                        }
                    } else {
                        if (index == -1) {

                        } else {
                            str.append(line.substring(index + "<BodyText>".length()));
                            started = true;
                            split = "</BodyText>";
                        }
                    }
                } else {
                    if (index != -1) {
                        str = new StringBuffer();
                        t = new TT();
                        t.setMobanmc(
                                line.substring(index + "<MOBANMC>".length(), line.indexOf("</MOBANMC>")));
                        split = "<BodyText>";
                        titleStarted = true;
                    }
                }
            }

            ExportExcel<TT> excel = new ExportExcel<TT>(ts, TT.class);
            excel.saveFile(new File("1.xls"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        string();
    }
}
