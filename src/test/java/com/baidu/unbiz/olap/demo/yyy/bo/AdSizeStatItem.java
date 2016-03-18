package com.baidu.unbiz.olap.demo.yyy.bo;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;

@OlapTable(
	name=YYY.TABLE.ADSIZE, 
	keyVal= {YYY.COLUMN.ADSIZEID}, 
	basicVal = {YYY.COLUMN.SRCHS, YYY.COLUMN.CLKS, YYY.COLUMN.COST},
	extCol = {YYY.COLUMN.CPM, YYY.COLUMN.CTR, YYY.COLUMN.ACP}, 
	extExpr = {YYY.EXPR.CPM, YYY.EXPR.CTR, YYY.EXPR.ACP},
	aliasCol = {YYY.COLUMN.CLKSALAIS},
	filter = {YYY.COLUMN.CLKS+">0"}
)
public class AdSizeStatItem extends StatInfo {	
	
	private static final long serialVersionUID = -7297777467848065984L;
	public static final String FROMDATE = "fromdate";
	
	@OlapColumn(YYY.COLUMN.ADSIZEID)
	private long adSizeId;
	
    @OlapColumn(value=YYY.COLUMN.CLKS, alias=YYY.COLUMN.CLKSALAIS)
    protected long clksAlias; //别名的点击
    
    @OlapColumn(YYY.COLUMN.DSPID)
    private int dspId;

	public int getDspId() {
        return dspId;
    }

    public void setDspId(int dspId) {
        this.dspId = dspId;
    }

    public long getClksAlias() {
        return clksAlias;
    }

    public void setClksAlias(long clksAlias) {
        this.clksAlias = clksAlias;
    }

    public long getAdSizeId() {
        return adSizeId;
    }

    public void setAdSizeId(long adSizeId) {
        this.adSizeId = adSizeId;
    }

    @Override
	public void afterAssemble(int timeUnit){
		super.afterAssemble(timeUnit);
		// DO OTHER THINGS
	}
}
