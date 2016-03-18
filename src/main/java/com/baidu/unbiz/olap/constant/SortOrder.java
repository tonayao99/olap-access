package com.baidu.unbiz.olap.constant;

/**
 * 排序方向
 * 
 * @author wangchongjie
 * @fileName SortOrder.java
 * @dateTime 2015-7-15 下午3:36:57
 */
public enum SortOrder {
    ASC /* 升序 */, DESC /* 降序 */;

    public static SortOrder val(String order) {
        if ("ASC".equalsIgnoreCase(order)) {
            return ASC;
        } else if ("DESC".equalsIgnoreCase(order)) {
            return DESC;
        }
        return ASC;
    }

    public static SortOrder val(int num) {
        if (num >= 0) {
            return ASC;
        } else {
            return DESC;
        }
    }

    public static int num(SortOrder order) {
        if (ASC.equals(order)) {
            return 1;
        } else {
            return -1;
        }
    }

    public static void main(String[] args) {
        System.out.println(SortOrder.val(1));
        System.out.println(SortOrder.val("DESC"));
    }
}