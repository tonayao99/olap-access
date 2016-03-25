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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class OlapBatchServiceTest {

    @Resource(name = "groupStatServiceImpl")
    private GroupStatService groupService;
    

    @Test
    public void testQueryGroupStat() {
        GroupRequest req = this.mockGroupRequest();
        List<GroupViewItem> data = groupService.queryBatchGroupData(req);

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
    
    private Date newDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);
        return c.getTime();
    }
}
