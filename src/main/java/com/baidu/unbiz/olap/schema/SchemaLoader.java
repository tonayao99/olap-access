package com.baidu.unbiz.olap.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.SmartLifecycle;

import com.baidu.unbiz.olap.config.OlapInstanceConfig;
import com.baidu.unbiz.olap.driver.OlapEngineDriver;
import com.baidu.unbiz.olap.util.StringUtils;

/**
 * 表元信息加载器
 * 
 * @author wangchongjie
 * @fileName SchemaLoader.java
 * @dateTime 2015-7-15 下午7:14:41
 */
// @Component
public class SchemaLoader implements SmartLifecycle {

    private OlapEngineDriver olapDriver;

    private boolean running = false;

    private volatile Set<Number> userIdDelegate;
    
    private AtomicBoolean userIdDelegateHasInit = new AtomicBoolean(false);

    private ConcurrentMap<String, Set<String>> schemaInfoCache = new ConcurrentHashMap<String, Set<String>>();

    public SchemaLoader() {
    }
    
    public SchemaLoader(OlapEngineDriver olapDriver) {
        this.olapDriver = olapDriver;
    }
    
    public void setOlapDriver(OlapEngineDriver olapDriver) {
        this.olapDriver = olapDriver;
    }
    

    /**
     * 获取olap表的所有column
     * 
     * @param table
     * @return 2014-7-17 下午6:28:00 created by wangchongjie
     */
    public Set<String> getAllOlapColumns(String table) {
        Set<String> columns = schemaInfoCache.get(table);
        return columns == null ? null : Collections.unmodifiableSet(columns);
    }

    private Set<String> loadOlapTables() {
        OlapInstanceConfig olapConfig = olapDriver.config();
        String databaseName = StringUtils.isLegalConfValue(olapConfig.database()) ? olapConfig.database() : "olap";
        String sql = "select TABLE_NAME from INFORMATION_SCHEMA.`TABLES` where TABLE_SCHEMA='" + databaseName + "'";
        List<Map<String, Object>> dataList = olapDriver.executeSQLonSameShard(sql);

        Set<String> tables = new HashSet<String>();
        for (Map<String, Object> data : dataList) {
            Object table = data.get("TABLE_NAME");
            if (table != null) {
                tables.add(table.toString());
            }
        }
        return tables;
    }

    private Set<String> loadOlapColumns(String table) {
        String sql = "select COLUMN_NAME from INFORMATION_SCHEMA.`COLUMNS` where table_name='" + table + "'";
        List<Map<String, Object>> dataList = olapDriver.executeSQLonSameShard(sql);

        Set<String> columns = new HashSet<String>();
        for (Map<String, Object> data : dataList) {
            Object column = data.get("COLUMN_NAME");
            if (column != null) {
                columns.add(column.toString());
            }
        }
        return columns;
    }

    /**
     * 加载Schema信息
     * 配合H2单测,去除@PostConstruct注解
     * 
     * @since 2015-7-28 by wangchongjie
     */
    // @PostConstruct
    public void loadSchemaInfo() {
        Map<String, Set<String>> tableColumnMap = new HashMap<String, Set<String>>();

        Set<String> tables = this.loadOlapTables();
        for (String table : tables) {
            tableColumnMap.put(table, this.loadOlapColumns(table));
        }
        schemaInfoCache.putAll(tableColumnMap);
    }

    /**
     * 切换各个分片的userId代表
     */
    public void changeUserIdDelegate(Set<Number> userIdDelegate) {
        this.userIdDelegate = userIdDelegate;
        this.userIdDelegateHasInit.set(true);
    }
    
    /**
     * 切换各个分片的userId代表
     */
    public void changeUserIdDelegate(Set<Number> userIdDelegate, boolean forceChange) {
        boolean hasNotInit = (! forceChange) && (! userIdDelegateHasInit.get());
        if (forceChange || hasNotInit) {
            this.changeUserIdDelegate(userIdDelegate);
        }
    }
    
    /**
     * 获取各个分片的userId代表是否已初始化
     */
    public boolean isUserIdDelegateHasInit() {
        return userIdDelegateHasInit.get();
    }

    /**
     * 获取各个分片的userId代表
     */
    public Set<Number> getUserIdDelegate() {
        return userIdDelegate;
    }

    @Override
    public void start() {
        loadSchemaInfo();
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        if (callback != null) {
            callback.run();
        }
        running = false;
    }
}
