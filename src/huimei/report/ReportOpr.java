package huimei.report;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.poi.excel.parse.ImportExcel;

public class ReportOpr {

    private static void opr(File file) {
        try {
            Map<String, Report> map = new HashMap<String, Report>();
            ImportExcel<DataExcel> im = new ImportExcel<DataExcel>(file, DataExcel.class);
            List<DataExcel> datas = im.getRowDatas();
            Report all = new Report();
            all.setOpr("合计");
            for (DataExcel data : datas) {
                if (data.getOpr().equals("搜索输入")) {
                    continue;
                }
                Report report = map.get(data.getOpr());
                if (report == null) {
                    map.put(data.getOpr(), report = new Report());
                    report.setOpr(data.getOpr());
                }

                report.getHospital().add(data.getHospital());
                report.getCases().add(data.getCases());
                report.getDoctor().add(data.getDoctor());
                report.getPatient().add(data.getPatient());

                all.getHospital().add(data.getHospital());
                all.getCases().add(data.getCases());
                all.getDoctor().add(data.getDoctor());
                all.getPatient().add(data.getPatient());
            }
            System.out.println("------------" + file.getName() + "------------");
            for (Entry<String, Report> entry : map.entrySet()) {
                printReport(entry.getValue());
            }
            printReport(all);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printReport(Report report) {
        System.out.print(report.getOpr());
        System.out.print(",");
        System.out.print(report.getHospital().size());
        System.out.print(",");
        System.out.print(report.getDoctor().size());
        System.out.print(",");
        System.out.print(report.getPatient().size());
        System.out.print(",");
        System.out.print(report.getCases().size());

        System.out.println();
    }

    public static void main(String[] args) {
        File dir = new File("D:\\report");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".xlsx")) {
                opr(file);
                System.out.println("------------------------------");
            }
        }
    }
}
