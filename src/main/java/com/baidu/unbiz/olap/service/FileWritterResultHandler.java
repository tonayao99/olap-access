package com.baidu.unbiz.olap.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.baidu.unbiz.olap.exception.OlapException;
import com.baidu.unbiz.olap.obj.BaseItem;

/**
 * 将结果写出为文件的处理器
 * 
 * @author wangchongjie
 * @fileName FileWritterResultHandler.java
 * @dateTime 2015-7-15 下午7:29:50
 */
public abstract class FileWritterResultHandler<T extends BaseItem> implements ResultHandler<T> {

    final FileWriter fw;

    public abstract String getItemContent(T item);

    /**
     * 构造方法
     * 
     * @param fileName 目标文件
     */
    public FileWritterResultHandler(String fileName) {
        try {
            fw = new FileWriter(fileName, true);
        } catch (IOException e) {
            throw new OlapException("write file fail:", e);
        }
    }

    /**
     * 资源清理
     */
    @Override
    public void cleanup() {
        try {
            fw.close();
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * 将查询结果分批写入到文件中
     */
    @Override
    public void process(List<T> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            return;
        }
        for (T item : itemList) {
            try {
                fw.write(this.getItemContent(item) + "\n");
                fw.flush();
            } catch (IOException e) {
                try {
                    fw.close();
                } catch (IOException e1) {
                    // do nothing
                }
                throw new OlapException("write file fail:", e);
            }
        }
    }
}