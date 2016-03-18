package com.baidu.unbiz.olap.demo.yyy.bo;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.annotation.OlapMergeKey;
import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.annotation.OlapTablePlus;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;

    @OlapTable(
		name=YYY.TABLE.ADSIZE, 
		keyVal= {YYY.COLUMN.ADSIZEID}, 
		basicVal = {YYY.COLUMN.SRCHS, YYY.COLUMN.CLKS, YYY.COLUMN.COST},
		extCol = {YYY.COLUMN.CPM, YYY.COLUMN.CTR, YYY.COLUMN.ACP}, 
		extExpr = {YYY.EXPR.CPM, YYY.EXPR.CTR, YYY.EXPR.ACP}
	)
	@OlapTablePlus(
		@OlapTable(
			name="DailyAdSizeUvStats", 
			keyVal= {YYY.COLUMN.ADSIZEID}, 
			basicVal = {"SearchUv", "ClickUv"}
		)
	)
	public class MultiStatItem extends StatInfo {	
		private static final long serialVersionUID = -1;
		
		@OlapColumn(YYY.COLUMN.USERID)
		private Long dspId;

		@OlapMergeKey
		@OlapColumn(YYY.COLUMN.ADSIZEID)
		private Long adSizeId;

        @OlapColumn("SearchUv")
		protected long srchUv;//展现
		
		@OlapColumn("ClickUv")
		protected long clkUv;//点击
		
        public long getSrchUv() {
			return srchUv;
		}
		public void setSrchUv(long srchUv) {
			this.srchUv = srchUv;
		}
		public long getClkUv() {
			return clkUv;
		}
		public void setClkUv(long clkUv) {
			this.clkUv = clkUv;
		}
		public Long getAdSizeId() {
			return adSizeId;
		}
		public void setAdSizeId(Long adSizeId) {
			this.adSizeId = adSizeId;
		}
		public Long getDspId() {
			return dspId;
		}
		public void setDspId(Long dspId) {
			this.dspId = dspId;
		}
		
		@Override
		public void afterAssemble(int timeUnit){
			super.afterAssemble(timeUnit);
			// DO OTHER THINGS
		}
	}

