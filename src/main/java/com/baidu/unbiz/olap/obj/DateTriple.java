package com.baidu.unbiz.olap.obj;

/**
 * 日期三元组
 * 
 * @author wangchongjie
 * @fileName DateTriple.java
 * @dateTime 2014-1-4 下午9:43:25
 */
public class DateTriple {

    private int dateYYYY;
    private int dateMM;
    private int dateDD;

    public DateTriple() {

    }

    public DateTriple(int yyyy, int mm, int dd) {
        this.dateYYYY = yyyy;
        this.dateMM = mm;
        this.dateDD = dd;
    }

    public int getDateYYYY() {
        return dateYYYY;
    }

    public void setDateYYYY(int dateYYYY) {
        this.dateYYYY = dateYYYY;
    }

    public int getDateMM() {
        return dateMM;
    }

    public void setDateMM(int dateMM) {
        this.dateMM = dateMM;
    }

    public int getDateDD() {
        return dateDD;
    }

    public void setDateDD(int dateDD) {
        this.dateDD = dateDD;
    }
}
