package huimei.test;

import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.hm.apollo.framework.utils.ExpressionParserUtils;

/**
 * Description:
 * Copyright (C) 2017 HuiMei All Right Reserved.
 * createDate：2017年7月10日
 * author：huangzhenjie
 * @version 1.0
 */
public class ExpressionTest {
	public static void main(String[] args) {
		// 低危｜中危
		String s = "'低危'.equals(#value)||'中危'.equals(#value)";
		// String s = "#value>140";
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("value", "1中危");
		System.out.println(ExpressionParserUtils.parseExpression(s, context));
	}
}
