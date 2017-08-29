package huimei.declass;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月12日
 * author：huangzhenjie
 * @version 1.0
 */
public class JarFile {

    public void jar() {
        try {
            File jar = new File("elasticsearch-5.5.0.jar");
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jar));
            JarEntry jarEntry = null;
            while ((jarEntry = jarFile.getNextJarEntry()) != null) {
                System.out.println(jarEntry.getName());
            }

            jarFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            FileInputStream in = new FileInputStream(new File("JarFile.class"));

            byte[] bs = new byte[1024];
            while (in.read(bs) != -1) {
                for (byte b : bs) {
                    System.out.print((char) b);
                }
                System.out.println();
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
