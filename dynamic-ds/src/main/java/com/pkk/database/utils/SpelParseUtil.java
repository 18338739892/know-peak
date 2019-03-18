package com.pkk.database.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * <p>Title: spel解析<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2018<／p>
 *
 * @author pkk
 * @version 1.0
 * @date 2019/3/18 0018
 */
public class SpelParseUtil {

    /**
     * <p>Title: parserKey<／p>
     * <p>Description: 解析参数与值拼接到spel中<／p>
     * <p>Copyright: Copyright (c) 2018<／p>
     *
     * @author pkk
     * @date 2019/3/18 0018
     * @version 1.0
     */
    public static String parserKey(Object[] args, String[] parameterNames, String spel) {
        // 把方法参数名与参数值成对放入SPEL上下文中
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }

        //解析key
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(spel).getValue(evaluationContext, String.class);
    }
}
