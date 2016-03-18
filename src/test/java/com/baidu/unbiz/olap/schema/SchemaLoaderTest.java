package com.baidu.unbiz.olap.schema;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baidu.unbiz.olap.schema.SchemaLoader;

/**
 * SchemaLoader测试类
 * 
 * @author wangchongjie
 * @fileName SchemaLoaderTest.java
 * @dateTime 2014-7-16 下午6:58:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-olap.xml")
public class SchemaLoaderTest {

	@Resource(name="schemaLoader")
	private SchemaLoader schemaLoader;
	
	@Test
	public void testLoadSchema(){
		schemaLoader.loadSchemaInfo();
	}
	
	@Test
	public void testGetAllColumns(){
		Set<String> schemaInfo = schemaLoader.getAllOlapColumns("DailyUserStats");
		System.out.println(schemaInfo.toString());
		
		schemaInfo = schemaLoader.getAllOlapColumns("DailyPlanStats");
		System.out.println(schemaInfo.toString());
	}
	
}
