package huimei.es;
// package com.hm.apollo.controller;
//
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// import org.junit.Test;
//
// import com.hm.apollo.module.cdss.pojo.MaysonRecognizeRequest;
// import com.poi.excel.parse.ExportExcel;
//
/// **
// * Description:
// * Copyright (C) 2017 HuiMei All Right Reserved.
// * createDate：2017年7月14日
// * author：huangzhenjie
// * @version 1.0
// */
// public class MaysonRecognizeControllerTest {
//
// PushService pushService = new PushService();
//
// @Test
// public void test() {
// try {
// BufferedReader br = new BufferedReader(new FileReader(new
// File("D://1.txt")));
// String line = null;
// List<TT> ts = new ArrayList<TT>();
// int index = 0;
// while ((line = br.readLine()) != null) {
// System.out.println(line);
// MaysonRecognizeRequest req = new MaysonRecognizeRequest();
// Map<String, String> map = new HashMap<>();
// map.put("主诉", line);
//
// req.setRecordInfo(map);
// req.setAge(55D);
// req.setAgeType("岁");
// req.setPregnancyStatus(0);
// req.setBodyTempr(44D);
// req.setLowBldPress(120D);
// req.setHighBldPress(190D);
//
// String content = pushService.push("http://127.0.0.1:8080/apollo",
// "/mayson/recognize", req);
//
// TT t = new TT();
// t.setBodyMessage(line);
// t.setResult(content);
//
// ts.add(t);
// }
//
//
// ExportExcel<TT> out = new ExportExcel<>(ts, TT.class);
// out.saveFile("D://3.xls");
//
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
//
// }
