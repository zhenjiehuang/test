package huimei.data.recognize;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.poi.excel.parse.ExportExcel;
import com.poi.excel.parse.ImportExcel;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年8月25日
 * author：huangzhenjie
 * @version 1.0
 */
public class FilterTest {

    public static void main(String[] args) {
        String path = System.getProperty("user.dir");
        path = path + "\\src\\" + Feibiaojicihui.class.getPackage().getName().replace('.', '\\');
        String fileName = "2.xlsx";
        try {
            ImportExcel<TypeClass> im = new ImportExcel<>(new File(path, fileName), TypeClass.class);
            List<TypeClass> types = im.getRowDatas();

            List<TypeClass> filter = new ArrayList<TypeClass>();

            int count = 0;
            for (int i = 2; i < types.size(); i += 3) {
                TypeClass type = types.get(i);

                if (!filter.contains(type)) {
                    // filter.add(types.get(i - 2));
                    // filter.add(types.get(i - 1));
                    // type.setda
                    type.setData3(type.getData());
                    type.setData0(types.get(i - 2).getData());
                    type.setData1(types.get(i - 1).getData());
                    filter.add(type.addCount());
                    // filter.add(new TypeClass().addCount());
                    // filter.add(new TypeClass());
                    count++;
                } else {
                    filter.get(filter.indexOf(type)).addCount();
                }
            }

            int c = 0;
            for (int i = 0; i < filter.size(); i += 1) {
                // filter.get(i).setData0("出现频率");
                // filter.get(i).setData1(filter.get(i).getCount() + "");
                System.out.println(c += filter.get(i).getCount());
            }
            System.out.println(c);

            TypeClass type = new TypeClass();
            type.setData0("总数");
            type.setData1(count + "");

            filter.add(type);

            ExportExcel<TypeClass> export = new ExportExcel<>(filter, TypeClass.class);
            export.saveFile("D://1//4.xls");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
