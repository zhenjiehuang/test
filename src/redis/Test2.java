package redis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Test2 {

    public static void main(String[] args) {
        DateFormat format = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.CHINESE);

        System.out.println(format.format(new Date()));

    }
}
