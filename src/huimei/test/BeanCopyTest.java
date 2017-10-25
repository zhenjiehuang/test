package huimei.test;

import java.lang.reflect.Method;

import com.hm.apollo.module.cdss.model.request.PatientProfileRequest;

public class BeanCopyTest {
    public static void main(String[] args) {
        try {
            Class<?> c1 = PatientProfileRequest.class;
            Method[] ms1 = c1.getDeclaredMethods();

            Class<?> c2 = PatientProfileRequest.class;
            Method[] ms2 = c2.getDeclaredMethods();

            System.out.println(c1.getSimpleName() + " target= new " + c1.getSimpleName() + "();");

            for (Method m1 : ms1) {
                if (m1.getName().startsWith("set")) {
                    String name = m1.getName().substring(3);
                    System.out.print("target." + m1.getName() + "(");
                    int i = 0;
                    for (; i < ms2.length; i++) {
                        Method m2 = ms2[i];
                        if (m2.getName().startsWith("get")) {
                            if (m2.getName().substring(3).equals(name)) {
                                System.out.println("source." + m2.getName() + "());");
                                break;
                            }
                        }
                    }
                    if (i == ms2.length) {
                        System.out.println("null);");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
