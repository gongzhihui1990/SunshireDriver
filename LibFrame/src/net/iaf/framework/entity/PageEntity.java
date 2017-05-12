package net.iaf.framework.entity;

import java.util.ArrayList;

public class PageEntity<T> extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private ArrayList<T> dataList;
    private int totalRecord;
    private int totalPage;
    private int currentPage;
    private Object lastestRecordId;

    public PageEntity() {
        this.dataList = new ArrayList<T>();
    }

    public ArrayList<T> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<T> dataList) {
        this.dataList = dataList;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Object getLastestRecordId() {
        return lastestRecordId;
    }

    public void setLastestRecordId(Object lastestRecordId) {
        this.lastestRecordId = lastestRecordId;
    }

}
