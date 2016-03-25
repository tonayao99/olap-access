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
import com.baidu.unbiz.olap.demo.xxx.bo.GroupViewItem;
import com.baidu.unbiz.olap.demo.xxx.service.GroupRequest;
import com.baidu.unbiz.olap.demo.xxx.service.GroupStatService;
import com.baidu.unbiz.olap.demo.xxx.service.GroupTransStatService;
import com.baidu.unbiz.olap.demo.xxx.service.GroupUvStatService;

/**
 * Xxx产品线 olap service测试类
 * 
 * @author wangchongjie
 * @fileName OlapXxxServiceTest.java
 * @dateTime 2015-7-17 下午2:58:02
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class OlapXxxServiceTest {

    @Resource(name = "groupStatServiceImpl")
    private GroupStatService groupService;

    @Resource(name = "groupUvStatServiceImpl")
    private GroupUvStatService groupUvService;

    @Resource(name = "groupTransStatServiceImpl")
    private GroupTransStatService groupTransService;

    @Test
    public void testQueryGroupStat() {
        GroupRequest req = this.mockGroupRequest();
        List<GroupViewItem> data = groupService.queryGroupData(req);

        for (GroupViewItem item : data) {
            System.out.println(item.toString());
        }
        Assert.notEmpty(data);
    }

    @Test
    public void testQueryGroupUvStat() {
        GroupRequest req = this.mockGroupRequest();
        List<GroupViewItem> data = groupUvService.queryGroupData(req);

        for (GroupViewItem item : data) {
            System.out.println(item.toString());
        }
        Assert.notEmpty(data);
    }

    @Test
    public void testQueryGroupTransStat() {
        GroupRequest req = this.mockGroupRequest();
        List<GroupViewItem> data = groupTransService.queryGroupData(req);

        for (GroupViewItem item : data) {
            System.out.println(item.toString());
        }
        Assert.notEmpty(data);
    }
    
    @Test
    public void testQueryAllGroupStat() {
        GroupRequest req = this.mockGroupRequestWithNoneUserId();
        List<GroupViewItem> data = groupService.queryGroupData(req);

        for (GroupViewItem item : data) {
            System.out.println(item.toString());
        }
        Assert.notEmpty(data);
    }
    
    @Test
    public void testQueryGroupStatByUserSpecify() {
        GroupRequest req = this.mockGroupRequest();
        req.setTimeUnit(OlapConstants.TU_NONE);
        List<GroupViewItem> data = groupService.queryGroupDataByKeyValSpecify(req);

        for (GroupViewItem item : data) {
            System.out.println(item.toString());
        }
        Assert.notEmpty(data);
    }

    private GroupRequest mockGroupRequest() {
        GroupRequest req = new GroupRequest();
        req.setUserId(8);
        req.setFrom(newDate(2015, 7, 1));
        req.setTo(newDate(2015, 7, 30));
        req.setTimeUnit(OlapConstants.TU_DAY);
        return req;
    }
    
    // 慎用无userId查询,如果需查全量数据，建议配合索引表，优先使用getBatchStorageData接口
    private GroupRequest mockGroupRequestWithNoneUserId() {
        GroupRequest req = new GroupRequest();
        req.setFrom(newDate(2015, 7, 1));
        req.setTo(newDate(2015, 7, 30));
        return req;
    }

    private Date newDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
