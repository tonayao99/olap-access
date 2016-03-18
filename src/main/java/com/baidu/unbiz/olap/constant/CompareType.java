package com.baidu.unbiz.olap.constant;

/**
 * 数值比较类型
 * 
 * @author wangchongjie
 * @fileName CompareType.java
 * @dateTime 2015-7-15 下午3:33:59
 */
public enum CompareType {

    GT(">"),   // 大于
    LT("<"),   // 小于
    GE(">="),  // 大于等于
    LE("<="),  // 小于等于
    EQ("="),   // 等于
    NEQ("!="); // 不等于

    String op;

    CompareType(String op) {
        this.op = op;
    }

    public String val() {
        return op;
    }
}
