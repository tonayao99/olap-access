package com.baidu.unbiz.olap.demo.testcase.client;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.constant.OlapConstants;
import com.baidu.unbiz.olap.demo.yyy.constant.YYY;
import com.baidu.unbiz.olap.driver.OlapEngineDriver;
import com.baidu.unbiz.olap.driver.bo.OlapRequest;
import com.baidu.unbiz.olap.factory.OlapDriverFactory;

/**
 * OlapDriver测试类
 * 
 * @author wangchongjie
 * @fileName OlapDriverTest.java
 * @dateTime 2014-7-9 下午7:27:58
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-test.xml")
public class OlapDriverTest {

    @Resource(name = "olapDriverFactoryImpl")
    private OlapDriverFactory driverFactory;
    
    @Resource(name = "xxxOlapInstanceConfig")
    private OlapInstanceConfig olapConfig;
    
	private OlapEngineDriver olapDriver;

    @Resource(name = "yyyOlapInstanceConfig")
    protected OlapInstanceConfig olapYYYConfig;
	
	@Before
	@PostConstruct
	public void initOlapDriver() {
	    olapDriver = driverFactory.newOlapEngineDriver(olapConfig);
	}
	
	@Test
	//不推荐使用：应用方直接拼sql查询
	public void testQueryBySQL(){

//		String csql = "select * from INFORMATION_SCHEMA.tables;";
//		System.out.println("******" + olapDriver.executeSQLonOneRandomShard(csql));

		String sql = "SELECT UserId,SUM(Search) AS Search,SUM(Click) AS Click,SUM(Cost) AS Cost," +
				"SUM(Cost)/SUM(Search)/100 AS cpm,SUM(Click)/SUM(Search) AS ctr,SUM(Cost)/SUM(Click)/100000 AS acp " +
				"FROM olap.DailyGroupStats WHERE UserId = 8 AND date >= '2014-06-10' AND date < '2014-06-23' GROUP BY"
				+ " UserId ";

		int userId = 8;
		List<Map<String, BigInteger>> result = olapDriver.getStorageDataBySQL(sql, userId);
		System.out.println(result.toString());
	}
	
	@Test
	//不推荐使用：应用方直接查询
	public void testQueryByRequest(){
		
		@SuppressWarnings("rawtypes")
		OlapRequest request = new OlapRequest();

		request.setTable(YYY.TABLE.ADSIZE);
		request.setGroupByCols(new String[] {YYY.COLUMN.ADSIZEID});
		request.setBasicValueCols(new String[] {YYY.COLUMN.SRCHS, YYY.COLUMN.CLKS});
		request.setUserIds(new Integer[] {63});     			//userid
		request.setFrom(new Date(1402333200000L)); 			//查询起始时间
		request.setTo(new Date(1403370000000L));			//查询终止时间
		request.setTimeUnit(OlapConstants.TU_DAY);				//时间粒度

        olapDriver.setOlapConfig(olapYYYConfig);
		List<Map<String, BigInteger>> result = olapDriver.getStorageDataByRequest(request);
		System.out.println(result.toString());
	}
}
