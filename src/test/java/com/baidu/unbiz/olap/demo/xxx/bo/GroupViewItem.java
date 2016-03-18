package com.baidu.unbiz.olap.demo.xxx.bo;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.annotation.OlapTablePlus;
import com.baidu.unbiz.olap.demo.xxx.constant.XXX;

/**
 * Xxx产品线的推广组维度模型
 * 
 * @author wangchongjie
 * @fileName GroupViewItem.java
 * @dateTime 2015-7-3 上午10:56:14
 */
@OlapTable(
	name=XXX.TABLE.GROUP, 
	keyVal= {XXX.COLUMN.PLANID, XXX.COLUMN.GROUPID}, 
	basicVal = {XXX.COLUMN.SRCHS, XXX.COLUMN.CLKS, XXX.COLUMN.COST},
	extCol = {XXX.COLUMN.CPM, XXX.COLUMN.CTR, XXX.COLUMN.ACP}, 
	extExpr = {XXX.EXPR.CPM, XXX.EXPR.CTR, XXX.EXPR.ACP}
)
@OlapTablePlus({
    @OlapTable(name=XXX.TABLE.GROUP_UV,
        keyVal= {XXX.COLUMN.PLANID, XXX.COLUMN.GROUPID},
        basicVal = {XXX.COLUMN.SRCHUV, XXX.COLUMN.CLKUV}),
        
    @OlapTable(name=XXX.TABLE.GROUP_TRANS,
        keyVal= {XXX.COLUMN.PLANID, XXX.COLUMN.GROUPID},
        basicVal = {XXX.COLUMN.DIRECTTRANS, XXX.COLUMN.INDIRECTTRANS}),
        
    @OlapTable(name=XXX.TABLE.HOLMES,
        keyVal= {XXX.COLUMN.PLANID, XXX.COLUMN.GROUPID},
        basicVal = {XXX.COLUMN.CLKS, XXX.COLUMN.ARRIVALCNT, XXX.COLUMN.HOPCNT, 
            XXX.COLUMN.RESTIME, XXX.COLUMN.EFFECTARRCNT},
        aliasCol = {XXX.COLUMN.HOLMESCLKS})
})
public class GroupViewItem extends StatInfo {

    private static final long serialVersionUID = 5264949626311874592L;

    @OlapColumn(XXX.COLUMN.PLANID)
    private Integer planId;

    private String planName;

    @OlapColumn(XXX.COLUMN.GROUPID)
    private Integer groupId;

    private String groupName;
   

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
