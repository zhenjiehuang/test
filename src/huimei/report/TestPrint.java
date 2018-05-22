package huimei.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestPrint {
    public static void main(String[] args) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date date = calendar.getTime();
            SimpleDateFormat f = new SimpleDateFormat("'cdss-access_log_'yyyy-MM-dd'.txt'");
            System.out.println(f.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
