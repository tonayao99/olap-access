package com.baidu.unbiz.olap.demo.testcase.client;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.baidu.unbiz.olap.constant.OlapConstants;
import com.baidu.unbiz.olap.demo.zzz.palo.bo.SiteViewItem;
import com.baidu.unbiz.olap.demo.zzz.palo.service.SiteRequest;
import com.baidu.unbiz.olap.demo.zzz.palo.service.SiteStatService;

/**
 * Zzz产品线 olap service测试类
 * 
 * @author wangchongjie
 * @fileName OlapZzzServiceTest.java
 * @dateTime 2015-7-30 上午11:05:49
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class OlapZzzServiceTest {

    @Resource(name = "siteStatServiceImpl")
    private SiteStatService siteService;

    @Test
    public void testQuerySiteStat() {
        SiteRequest req = this.mockRequest();
        List<SiteViewItem> data = siteService.querySiteData(req);

        for (SiteViewItem item : data) {
            System.out.println(item.toString());
        }
        Assert.notEmpty(data);
    }

   

    private SiteRequest mockRequest() {
        SiteRequest req = new SiteRequest();
        req.setUserId(1);
        req.setFrom(newDate(2015, 7, 1));
        req.setTo(newDate(2015, 7, 30));
        req.setTimeUnit(OlapConstants.TU_DAY);
        return req;
    }

    private Date newDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
