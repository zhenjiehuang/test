package huimei.test;

import java.util.Set;

import com.google.common.collect.Sets;
import com.hm.apollo.framework.utils.ExpressionParserUtils;
import com.hm.apollo.module.cdss.pojo.ExamRecognizePojo;
import com.hm.mayson.module.progress.model.PatientRecord;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月10日
 * author：huangzhenjie
 * @version 1.0
 */
public class ExpressionTest {
	public static void main(String[] args) {
        PatientRecord patientRecord = new PatientRecord();
        Set<String> param = Sets.newHashSet();
        patientRecord.setParam(param);
        // [胸痛, 女, T波改变, 缺血症状, 急性胸痛, 急性肌钙蛋白阴性, 无GRACE评分, 急性T波改变, 肌钙蛋白阴性, 女性]
        param.add("胸痛");
        param.add("女");
        param.add("T波改变");
        param.add("缺血症状");
        param.add("急性胸痛");
        param.add("急性肌钙蛋白阴性");
        param.add("无GRACE评分");
        param.add("急性T波改变");
        param.add("肌钙蛋白阴性");
        param.add("女性");

        System.out.println(ExpressionParserUtils.parseExpression(
                "(( param.contains('急性胸痛') ) ) and" + " (param.contains('T波改变') && "
                        + "( param.contains('肌钙蛋白阴性') || param.contains('cTnI阴性') || param.contains('cTnT阴性') )"
                        + " && param.contains('CK-MB正常') )",
                patientRecord));

        ExamRecognizePojo pojo = new ExamRecognizePojo();
        pojo.setNumberValue(0D);
        System.out.println(ExpressionParserUtils.parseExpression("numberValue==0", pojo));

	}
}
