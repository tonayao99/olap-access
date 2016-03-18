package com.baidu.unbiz.olap.loadbalance;

/**
 * 优先选用主节点
 * 
 * @author wangchongjie
 * @fileName MasterBackupIndexSelector.java
 * @dateTime 2014-9-16 上午10:24:25
 */
public class MasterBackupIndexSelector implements IndexSelector {

    @SuppressWarnings("unused")
    private volatile int size;

    @Override
    public int select() {
        return 0;
    }

    @Override
    public void initSize(int size) {
        this.size = size;
    }

    public MasterBackupIndexSelector() {
    }

    public MasterBackupIndexSelector(int size) {
        initSize(size);
    }
}
