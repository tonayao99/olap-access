package com.baidu.unbiz.olap.demo.yyy.bo;

import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;

@OlapTable(
	name=YYY.TABLE.ADSIZE, 
	keyVal= {}, 
	basicVal = {YYY.COLUMN.SRCHS, YYY.COLUMN.CLKS, YYY.COLUMN.COST},
	extCol = {YYY.COLUMN.CPM, YYY.COLUMN.CTR, YYY.COLUMN.ACP}, 
	extExpr = {YYY.EXPR.CPM, YYY.EXPR.CTR, YYY.EXPR.ACP}
//	filter = {Constants.COLUMN.SRCHS+">=0","Adsize>0"}
)
public class StatDayItem extends StatInfo {	
	
	private static final long serialVersionUID = -7297777467848065984L;
	public static final String FROMDATE = "fromdate";

	@Override
	public void afterAssemble(int timeUnit){
		super.afterAssemble(timeUnit);
		// DO OTHER THINGS
	}
}
