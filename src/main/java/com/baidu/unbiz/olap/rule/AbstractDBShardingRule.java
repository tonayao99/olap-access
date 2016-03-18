package com.baidu.unbiz.olap.rule;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.baidu.unbiz.olap.log.AopLogFactory;

@Service
public abstract class AbstractDBShardingRule implements DBShardingRule {

    private static final Logger LOG = AopLogFactory.getLogger(AbstractDBShardingRule.class);
    
    protected int shardingNum;

    protected List<String> dbCodeCollection;

    /**
     * 计算数据库分片flag
     */
    abstract public String calculateDatabaseNo(Number userId);

    
    /**
     * 设置数据库分片数
     * 
     * @param shardingNum
     * @since 2015-7-28 by wangchongjie
     */
    public void setShardingNum(int shardingNum) {
        if ((shardingNum & (shardingNum - 1)) != 0) {
            String message = "shardingNum should be a power of 2, like 4,8,16,32";
            RuntimeException t = new IllegalArgumentException(message);
            LOG.error(message, t);
            throw t;
        }
        this.shardingNum = shardingNum;
        setDbCodeCollection();
    }

    /**
     * 初始化dbcode的集合,依赖于数据库的shardingNum
     */
    @PostConstruct
    private void setDbCodeCollection() {
        dbCodeCollection = new ArrayList<String>(shardingNum);
        int codeLength = Integer.toBinaryString(shardingNum - 1).length();
        for (int i = 0; i < shardingNum; i++) {
            String dbcode = Integer.toBinaryString(i);
            for (int j = dbcode.length(); j < codeLength; j++) {
                dbcode = "0" + dbcode;
            }
            dbCodeCollection.add(i, dbcode);
        }
    }

}
