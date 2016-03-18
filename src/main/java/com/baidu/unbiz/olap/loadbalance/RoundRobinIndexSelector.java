package com.baidu.unbiz.olap.loadbalance;

/**
 * 轮训选择策略
 * 
 * @author wangchongjie
 * @fileName RoundRobinIndexSelector.java
 * @dateTime 2014-9-16 上午10:25:11
 */
public class RoundRobinIndexSelector implements IndexSelector {

    private volatile int size;
    private int current = 0;

    @Override
    public int select() {
        current++;
        if (current >= size) {
            current = 0;
        }
        return current;
    }

    @Override
    public void initSize(int size) {
        this.size = size;
    }

    public RoundRobinIndexSelector() {
    }

    public RoundRobinIndexSelector(int size) {
        initSize(size);
    }
}
