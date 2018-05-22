package huimei.test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hm.mayson.module.qc.model.QcProject;

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
        List<QcProject> ps = new ArrayList<>();
        QcProject p1 = new QcProject();
        p1.setId(1L);
        p1.setCustomerId(1L);
        ps.add(p1);

        QcProject p2 = new QcProject();
        p2.setId(1L);
        p2.setCustomerId(2L);
        ps.add(p2);

        List<Long> ids = ps.stream()
                .collect(Collectors.mapping(QcProject::getId, Collectors.toList()));
        System.out.println(ids);
    }
}
