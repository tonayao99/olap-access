package com.baidu.unbiz.olap.demo.zzz.palo.bo;

import com.baidu.unbiz.olap.annotation.OlapColumn;
import com.baidu.unbiz.olap.annotation.OlapTable;
import com.baidu.unbiz.olap.demo.zzz.palo.constant.ZZZ;
import com.baidu.unbiz.olap.obj.BaseItem;

@OlapTable(
      name = ZZZ.TABLE.SITE, 
      keyVal = { ZZZ.COLUMN.SITEID, ZZZ.COLUMN.SITENAME }, 
      basicVal = { ZZZ.COLUMN.PV, ZZZ.COLUMN.UV }, 
      extCol = { ZZZ.COLUMN.UVPVR }, 
      extExpr = { ZZZ.EXPR.UVPVR }
)
public class SiteViewItem extends BaseItem {

    private static final long serialVersionUID = 5264949626311874592L;

    @OlapColumn(ZZZ.COLUMN.SITEID)
    private Integer siteId;

    @OlapColumn(ZZZ.COLUMN.SITENAME)
    private String siteName;

    @OlapColumn(ZZZ.COLUMN.PV)
    private long pv;

    @OlapColumn(ZZZ.COLUMN.UV)
    private long uv;

    @OlapColumn(ZZZ.COLUMN.UVPVR)
    private double uvpvr;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public long getPv() {
        return pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }

    public long getUv() {
        return uv;
    }

    public void setUv(long uv) {
        this.uv = uv;
    }

    public double getUvpvr() {
        return uvpvr;
    }

    public void setUvpvr(double uvpvr) {
        this.uvpvr = uvpvr;
    }
}
