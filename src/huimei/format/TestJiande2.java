package huimei.format;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestJiande2 {
    public static void main(String[] args) {
        try {
            StringBuffer progress = new StringBuffer();
            Files.lines(Paths.get("Z:\\workspace\\huimei\\work\\test\\src\\huimei\\format\\case"),
                    StandardCharsets.UTF_8).forEach(str -> {
                        progress.append(str);
                    });


            // String result = format.parser(progress.toString(),
            // "C3E74C229156E6B31534E946BCDEBA94");
            // System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
