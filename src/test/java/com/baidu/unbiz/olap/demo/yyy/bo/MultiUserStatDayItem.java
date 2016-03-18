package com.baidu.unbiz.olap.demo.yyy.bo;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;

@OlapTable(
	name=YYY.TABLE.ADSIZE, 
	keyVal= {YYY.COLUMN.DSPID}, 
	basicVal = {YYY.COLUMN.SRCHS, YYY.COLUMN.CLKS, YYY.COLUMN.COST},
	extCol = {YYY.COLUMN.CPM, YYY.COLUMN.CTR, YYY.COLUMN.ACP}, 
	extExpr = {YYY.EXPR.CPM, YYY.EXPR.CTR, YYY.EXPR.ACP}
)
public class MultiUserStatDayItem extends StatInfo {	
	
	private static final long serialVersionUID = 2625832823739087652L;
	public static final String FROMDATE = "fromdate";

	@OlapColumn(YYY.COLUMN.USERID)
	private int dspId;

	@Override
	public void afterAssemble(int timeUnit){
		super.afterAssemble(timeUnit);
		// DO OTHER THINGS
	}
	
	public int getDspId() {
		return dspId;
	}

	public void setDspId(int dspId) {
		this.dspId = dspId;
	}
}
