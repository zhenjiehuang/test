package huimei.test;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月7日
 * author：huangzhenjie
 * @version 1.0
 */
public class StreamTest {

	static class A {
		String id;

		public A() {
		}
	}

	public static void print(String a) {
		System.out.println(a);
	}

    public static void main(String[] args) {
        List<String> l = Arrays.asList("1", "2", "3", "4", "5");


        System.out.println(Integer.toHexString("1234".hashCode()));

        System.out.println("无".startsWith("无#"));

    }
}
