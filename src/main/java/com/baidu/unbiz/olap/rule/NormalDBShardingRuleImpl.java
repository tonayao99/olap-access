package com.baidu.unbiz.olap.rule;

import org.springframework.stereotype.Service;

/**
 * olap-access用户可重写该类，完成所需分库业务
 * 
 * @author wangchongjie
 * @fileName NormalDBShardingRuleImpl.java
 * @dateTime 2014-7-8 下午6:33:52
 */
@Service
public class NormalDBShardingRuleImpl extends AbstractDBShardingRule {

    private int tableShardingLength = 6;

    public String calculateDatabaseNo(Number userId) {
        if (shardingNum == 1) {
            return null;
        }
        int userIdCode = 0;
        if (userId == null || userId.longValue() < 0) {
            throw new RuntimeException("No userid contexted,so you can't know how to routing!!");
        } else {
            userIdCode = (int) ((userId.longValue() >>> tableShardingLength) & (shardingNum - 1));
        }
        return super.dbCodeCollection.get(userIdCode);
    }
    
}
