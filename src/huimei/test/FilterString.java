package huimei.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FilterString {

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(
                            "Z:\\workspace\\huimei\\work\\test\\src\\huimei\\test\\disease.dict"))));

            String line = null;
            while ((line = br.readLine()) != null) {
              String[] token=  line.split("[\t ]+");
//              token
              if(token.length==1){
                  System.out.println(line);
                } else if (!token[0].matches("^[A-Za-z]+$")) {
                    System.out.println(line);
              }
              
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
