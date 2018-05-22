package huimei.report;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLSplit {

    static String pattern = "(\\S+)\\s-\\s-\\s\\[(\\S+\\s\\S+)]\\s(\\d+)\\s\"((GET|POST)\\s+\\S+(1\\.gif\\?)([^\"]+)\\s+(HTTP/1\\.1))\"\\s(\\d+)\\s(\\d+)\\s\"([^\"]+)\"\\s-\\s\"([^\"]+)\"\\s([^\"]+)\\s-";


    public static void main(String[] args) {
        try {
            String str = "dd/MMM/yyyy:HH:mm:ss +SSSS";
            SimpleDateFormat f = new SimpleDateFormat(str, Locale.ENGLISH);
            System.out.println(f.format(new Date()));

            String path = System.getProperty("user.dir");
            path = path + "\\src\\" + URLSplit.class.getPackage().getName().replace('.', '\\');
            // String fileName = "test";
            String fileName = "http_access.log";
            // String fileName = "cdss_access.log-20180124";
            Files.lines(Paths.get(path, fileName)).filter(line -> line.contains("/1.gif?")).forEach(line -> {
                System.out.println(line);
                Pattern p = Pattern.compile(
                        "(\\S+)\\s-\\s-\\s\\[(\\S+\\s\\S+)]\\s(\\d+)\\s\"((GET|POST)\\s+\\S+(1\\.gif\\?)([^\"]+)\\s+(HTTP/1\\.1))\"\\s(\\d+)\\s(\\d+)\\s\"([^\"]+)\"\\s-\\s\"([^\"]+)\"\\s([^\"]+)\\s(.*)");
                // \\s\"([^\"]+)\"\\s(\\d+\\.\\d+)(\\s\\d+)(\\s\\d+)\\s\"([^\"]+)\"\\s\"([^\"]+)\"\\s\"([^\"]+)\"
                Matcher m = p.matcher(line);
                while (m.find()) {
                    // System.out.println(JSON.toJSONString(m.group().split("([&|=])")));
                    for (int i = 0; i < m.groupCount(); i++) {
                        System.out.println(i + " " + m.group(i));
                    }
                    try {
                        System.out.println(new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss +SSSS", Locale.ENGLISH)
                                .parse(m.group(2)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(m.group());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
