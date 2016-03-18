package com.baidu.unbiz.olap.loadbalance;

import java.util.Random;

/**
 * 随机选择策略
 * 
 * @author wangchongjie
 * @fileName RamdomIndexSelector.java
 * @dateTime 2014-9-16 上午10:24:57
 */
public class RamdomIndexSelector implements IndexSelector {

    private Random randomProvider = new Random();
    private volatile int size = 1;

    @Override
    public int select() {
        return randomProvider.nextInt(size);
    }

    @Override
    public void initSize(int size) {
        this.size = size;
    }

    public RamdomIndexSelector() {
    }

    public RamdomIndexSelector(int size) {
        initSize(size);
    }
}
